
package services;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.MiscellaneousRecordRepository;
import domain.Curriculum;
import domain.MiscellaneousRecord;
import domain.Ranger;

@Service
@Transactional
public class MiscellaneousRecordService {

	// Managed Repository
	@Autowired
	private MiscellaneousRecordRepository	miscellaneousRecordRepository;

	// Supporting Services
	@Autowired
	private CurriculumService				curriculumService;

	@Autowired
	private RangerService					rangerService;
	@Autowired
	private MessageService					messageService;


	// Constructor

	public MiscellaneousRecordService() {
		super();
	}

	// Simple CRUD methods
	public MiscellaneousRecord create() {
		final MiscellaneousRecord record = new MiscellaneousRecord();

		return record;
	}
	public MiscellaneousRecord save(final MiscellaneousRecord miscellaneousRecord) {
		this.isARangerAuthenticated();
		final Boolean edit = this.miscellaneousRecordRepository.findOne(miscellaneousRecord.getId()) != null;
		if (edit)
			this.isTheCorrectRanger(this.curriculumService.findByMiscellaneousRecordId(miscellaneousRecord.getId()).getId());
		Assert.notNull(miscellaneousRecord.getTitle());
		final MiscellaneousRecord savedMiscellaneousRecord = this.miscellaneousRecordRepository.save(miscellaneousRecord);
		Ranger principal;
		principal = this.rangerService.findByPrincipal();
		//checking is supicious
		final Collection<String> allStrings = new ArrayList<String>();
		allStrings.add(savedMiscellaneousRecord.getTitle());
		allStrings.add(savedMiscellaneousRecord.getComments());
		allStrings.add(savedMiscellaneousRecord.getAttachmentLink());
		Boolean isSuspicious;
		isSuspicious = this.isSuspicious(allStrings);
		if (isSuspicious)
			principal.setIsSuspicious(true);
		//end check

		if (!edit) {

			Curriculum curriculumPrincipal;
			curriculumPrincipal = principal.getCurriculum();
			curriculumPrincipal.getMiscellaneousRecords().add(savedMiscellaneousRecord);
			this.curriculumService.save(curriculumPrincipal);
		}
		this.rangerService.saveFromEdit(principal);
		return savedMiscellaneousRecord;
	}
	public void delete(final MiscellaneousRecord miscellaneousRecord) {

		Assert.notNull(this.miscellaneousRecordRepository.findOne(miscellaneousRecord.getId()));

		final Curriculum c = this.curriculumService.findByMiscellaneousRecordId(miscellaneousRecord.getId());

		this.isTheCorrectRanger(c.getId());
		;
		c.getMiscellaneousRecords().remove(miscellaneousRecord);
		this.curriculumService.save(c);

		this.miscellaneousRecordRepository.delete(miscellaneousRecord);
	}
	public MiscellaneousRecord findOne(final int recordId) {
		MiscellaneousRecord r;
		r = this.miscellaneousRecordRepository.findOne(recordId);
		return r;
	}

	// Other business methods
	public Collection<MiscellaneousRecord> findAllByCurriculumId(final int curriculumId) {
		final Curriculum c = this.curriculumService.findOne(curriculumId);
		final Collection<MiscellaneousRecord> r = new ArrayList<MiscellaneousRecord>();
		if (c != null)
			r.addAll(c.getMiscellaneousRecords());
		return r;
	}
	public void deleteAllByCurriculum(final Curriculum c) {
		this.isTheCorrectRanger(c.getId());

		final Collection<MiscellaneousRecord> records = new ArrayList<MiscellaneousRecord>();
		records.addAll(c.getMiscellaneousRecords());
		if (!records.isEmpty())
			for (final MiscellaneousRecord r : records)
				this.delete(r);
	}

	//Auxiliares
	private Ranger isARangerAuthenticated() {
		final Ranger r = this.rangerService.findByPrincipal();

		Assert.notNull(r);
		return r;
	}
	private void isTheCorrectRanger(final int CurriculumId) {
		Assert.notNull(this.isARangerAuthenticated().getCurriculum());
		Assert.isTrue(this.isARangerAuthenticated().getCurriculum().getId() == CurriculumId);
	}

	private Boolean isSuspicious(final Collection<String> texts) {
		Boolean isSuspicious = false;
		for (final String text : texts)
			if (text != null) {
				final Boolean check = this.messageService.checkSpam(text);
				if (check) {
					isSuspicious = true;
					break;
				}
			}
		return isSuspicious;
	}
}
