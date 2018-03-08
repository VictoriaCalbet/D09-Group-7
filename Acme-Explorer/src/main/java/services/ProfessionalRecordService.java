
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.ProfessionalRecordRepository;
import domain.Curriculum;
import domain.ProfessionalRecord;
import domain.Ranger;

@Service
@Transactional
public class ProfessionalRecordService {

	// Managed Repository
	@Autowired
	private ProfessionalRecordRepository	professionalRecordRepository;

	// Supporting Services
	@Autowired
	private CurriculumService				curriculumService;

	@Autowired
	private RangerService					rangerService;
	@Autowired
	private MessageService					messageService;


	// Constructor

	public ProfessionalRecordService() {
		super();
	}

	// Simple CRUD methods
	public ProfessionalRecord create() {
		final ProfessionalRecord record = new ProfessionalRecord();

		return record;

	}

	public ProfessionalRecord save(final ProfessionalRecord professionalRecord) {
		this.isARangerAuthenticated();
		final Boolean edit = this.professionalRecordRepository.findOne(professionalRecord.getId()) != null;
		if (edit)
			this.isTheCorrectRanger(this.curriculumService.findByProfessionalRecordId(professionalRecord.getId()).getId());
		Assert.notNull(professionalRecord.getCompanyName());
		Assert.notNull(professionalRecord.getRole());
		if (professionalRecord.getStartDate() != null) {
			Date startDate;
			startDate = professionalRecord.getStartDate();
			if (professionalRecord.getEndDate() != null) {
				Date endDate;
				endDate = professionalRecord.getEndDate();
				Boolean datesAreCorrect = false;
				datesAreCorrect = startDate.before(endDate);
				Assert.isTrue(datesAreCorrect);
			}
		}

		final ProfessionalRecord savedProfessionalRecord = this.professionalRecordRepository.save(professionalRecord);
		Ranger principal;
		principal = this.rangerService.findByPrincipal();
		//checking is supicious
		final Collection<String> allStrings = new ArrayList<String>();
		allStrings.add(savedProfessionalRecord.getComments());
		allStrings.add(savedProfessionalRecord.getAttachmentLink());
		allStrings.add(savedProfessionalRecord.getCompanyName());
		allStrings.add(savedProfessionalRecord.getRole());
		Boolean isSuspicious;
		isSuspicious = this.isSuspicious(allStrings);
		if (isSuspicious)
			principal.setIsSuspicious(true);
		//end check
		if (!edit) {
			Curriculum curriculumPrincipal;
			curriculumPrincipal = principal.getCurriculum();
			curriculumPrincipal.getProfessionalRecords().add(savedProfessionalRecord);
			this.curriculumService.save(curriculumPrincipal);

		}
		this.rangerService.saveFromEdit(principal);
		return savedProfessionalRecord;
	}
	public void delete(final ProfessionalRecord professionalRecord) {

		Assert.notNull(this.professionalRecordRepository.findOne(professionalRecord.getId()));

		final Curriculum c = this.curriculumService.findByProfessionalRecordId(professionalRecord.getId());

		this.isTheCorrectRanger(c.getId());
		c.getProfessionalRecords().remove(professionalRecord);
		this.curriculumService.save(c);

		this.professionalRecordRepository.delete(professionalRecord);

	}
	public ProfessionalRecord findOne(final int recordId) {
		ProfessionalRecord r;
		r = this.professionalRecordRepository.findOne(recordId);
		return r;
	}

	// Other business methods
	public Collection<ProfessionalRecord> findAllByCurriculumId(final int curriculumId) {
		final Curriculum c = this.curriculumService.findOne(curriculumId);
		final Collection<ProfessionalRecord> r = new ArrayList<ProfessionalRecord>();
		if (c != null)
			r.addAll(c.getProfessionalRecords());
		return r;
	}
	public void deleteAllByCurriculum(final Curriculum c) {
		this.isTheCorrectRanger(c.getId());

		final Collection<ProfessionalRecord> records = new ArrayList<ProfessionalRecord>(c.getProfessionalRecords());
		if (!records.isEmpty())
			for (final ProfessionalRecord r : records)
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
