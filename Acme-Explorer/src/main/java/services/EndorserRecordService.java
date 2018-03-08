
package services;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.EndorserRecordRepository;
import domain.Curriculum;
import domain.EndorserRecord;
import domain.Ranger;

@Service
@Transactional
public class EndorserRecordService {

	// Managed Repository
	@Autowired
	private EndorserRecordRepository	endorserRecordRepository;

	// Supporting Services
	@Autowired
	private CurriculumService			curriculumService;
	@Autowired
	private RangerService				rangerService;
	@Autowired
	private MessageService				messageService;
	@Autowired
	private ActorService				actorService;


	// Constructor

	public EndorserRecordService() {
		super();
	}

	// Simple CRUD methods
	public EndorserRecord create() {
		final EndorserRecord record = new EndorserRecord();

		return record;
	}
	public EndorserRecord save(final EndorserRecord endorserRecord) {
		this.isARangerAuthenticated();
		final Boolean edit = this.endorserRecordRepository.findOne(endorserRecord.getId()) != null;
		if (edit)
			this.isTheCorrectRanger(this.curriculumService.findByEndorserRecordId(endorserRecord.getId()).getId());
		Assert.notNull(endorserRecord.getName());
		Assert.notNull(endorserRecord.getPhone());
		Assert.notNull(endorserRecord.getEmail());
		Assert.notNull(endorserRecord.getLinkedInLink());
		if (endorserRecord.getPhone().matches("^(\\d{4,})$")) {
			final String updatedPhone = this.actorService.updatePhoneFromString(endorserRecord.getPhone());
			endorserRecord.setPhone(updatedPhone);
		}
		final EndorserRecord savedEndorserRecord = this.endorserRecordRepository.save(endorserRecord);
		Ranger principal;
		principal = this.rangerService.findByPrincipal();
		//checking is supicious
		final Collection<String> allStrings = new ArrayList<String>();
		allStrings.add(savedEndorserRecord.getEmail());
		allStrings.add(savedEndorserRecord.getLinkedInLink());
		allStrings.add(savedEndorserRecord.getName());
		allStrings.add(savedEndorserRecord.getPhone());
		allStrings.add(savedEndorserRecord.getComments());
		Boolean isSuspicious;
		isSuspicious = this.isSuspicious(allStrings);
		if (isSuspicious)
			principal.setIsSuspicious(true);
		//end check

		if (!edit) {
			Curriculum curriculumPrincipal;
			curriculumPrincipal = principal.getCurriculum();
			curriculumPrincipal.getEndorserRecords().add(savedEndorserRecord);
			this.curriculumService.save(curriculumPrincipal);

		}
		this.rangerService.saveFromEdit(principal);
		return savedEndorserRecord;

	}
	public void delete(final EndorserRecord endorserRecord) {
		Assert.notNull(this.endorserRecordRepository.findOne(endorserRecord.getId()));
		final Curriculum c = this.curriculumService.findByEndorserRecordId(endorserRecord.getId());

		this.isTheCorrectRanger(c.getId());

		c.getEndorserRecords().remove(endorserRecord);
		this.curriculumService.save(c);

		this.endorserRecordRepository.delete(endorserRecord);

	}
	public EndorserRecord findOne(final int recordId) {
		EndorserRecord r;
		r = this.endorserRecordRepository.findOne(recordId);
		return r;
	}

	// Other business methods
	public Collection<EndorserRecord> findAllByCurriculumId(final int curriculumId) {
		final Curriculum c = this.curriculumService.findOne(curriculumId);
		final Collection<EndorserRecord> r = new ArrayList<EndorserRecord>();
		if (c != null)
			r.addAll(c.getEndorserRecords());
		return r;
	}
	public void deleteAllByCurriculum(final Curriculum c) {
		this.isTheCorrectRanger(c.getId());

		final Collection<EndorserRecord> records = new ArrayList<EndorserRecord>(c.getEndorserRecords());
		if (!records.isEmpty())
			for (final EndorserRecord r : records)
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
