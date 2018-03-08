
package services;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.PersonalRecordRepository;
import domain.Curriculum;
import domain.PersonalRecord;
import domain.Ranger;

@Service
@Transactional
public class PersonalRecordService {

	// Managed Repository
	@Autowired
	private PersonalRecordRepository	personalRecordRepository;

	// Supporting Services
	@Autowired
	private CurriculumService			curriculumService;
	@Autowired
	private RangerService				rangerService;
	@Autowired
	private MessageService				messageService;


	// Constructor

	public PersonalRecordService() {
		super();
	}

	// Simple CRUD methods
	public PersonalRecord create() {

		return new PersonalRecord();
	}
	public PersonalRecord save(final PersonalRecord personalRecord) {
		this.isARangerAuthenticated();
		final Boolean edit = this.personalRecordRepository.findOne(personalRecord.getId()) != null;
		if (edit)
			this.isTheCorrectRanger(this.curriculumService.findByPersonalRecordId(personalRecord.getId()).getId());
		Assert.notNull(personalRecord.getEmail());
		Assert.notNull(personalRecord.getLinkedInLink());
		Assert.notNull(personalRecord.getPhone());
		Assert.notNull(personalRecord.getName());
		Assert.notNull(personalRecord.getPhoto());

		final PersonalRecord savedPersonalRecord = this.personalRecordRepository.save(personalRecord);
		Ranger principal;
		principal = this.rangerService.findByPrincipal();
		//checking is supicious
		final Collection<String> allStrings = new ArrayList<String>();
		allStrings.add(savedPersonalRecord.getEmail());
		allStrings.add(savedPersonalRecord.getLinkedInLink());
		allStrings.add(savedPersonalRecord.getName());
		allStrings.add(savedPersonalRecord.getPhone());
		allStrings.add(savedPersonalRecord.getPhoto());
		Boolean isSuspicious;
		isSuspicious = this.isSuspicious(allStrings);
		if (isSuspicious)
			principal.setIsSuspicious(true);
		//end check
		if (!edit) {
			final Curriculum c = this.curriculumService.create();
			c.setPersonalRecord(savedPersonalRecord);
			final Curriculum savedCurriculum = this.curriculumService.save(c);
			principal.setCurriculum(savedCurriculum);
		}
		this.rangerService.saveFromEdit(principal);
		return savedPersonalRecord;
	}
	public PersonalRecord findOne(final int personalRecordId) {
		PersonalRecord record = null;
		record = this.personalRecordRepository.findOne(personalRecordId);
		return record;
	}
	/*
	 * Un curriculum siempre tiene que tener un PersonalRecord,
	 * no se ha puesto un met�do delete que permita borrar directamente a partir del propio record.
	 */
	// Other business methods
	public PersonalRecord findByCurriculumId(final int curriculumId) {
		final Curriculum c = this.curriculumService.findOne(curriculumId);
		PersonalRecord r = null;
		if (c != null)
			r = c.getPersonalRecord();
		return r;
	}
	public void deleteByCurriculumId(final int curriculumId) {
		this.isTheCorrectRanger(curriculumId);
		final Curriculum c = this.curriculumService.findOne(curriculumId);

		this.personalRecordRepository.delete(c.getPersonalRecord().getId());

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
