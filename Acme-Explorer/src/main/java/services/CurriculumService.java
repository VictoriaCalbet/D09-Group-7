
package services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.CurriculumRepository;
import security.Authority;
import domain.Actor;
import domain.Curriculum;
import domain.EducationRecord;
import domain.EndorserRecord;
import domain.MiscellaneousRecord;
import domain.ProfessionalRecord;
import domain.Ranger;

@Service
@Transactional
public class CurriculumService {

	// Managed Repository
	@Autowired
	private CurriculumRepository		curriculumRepository;

	// Supporting Services
	@Autowired
	private RangerService				rangerService;
	@Autowired
	private ActorService				actorService;
	@Autowired
	private EducationRecordService		educationRecordService;
	@Autowired
	private PersonalRecordService		personalRecordService;
	@Autowired
	private ProfessionalRecordService	professionalRecordService;
	@Autowired
	private EndorserRecordService		endorserRecordService;
	@Autowired
	private MiscellaneousRecordService	miscellaneousRecordService;


	// Constructor

	public CurriculumService() {
		super();
	}

	// Simple CRUD methods
	public Curriculum create() {
		final Curriculum newCurriculum = new Curriculum();
		newCurriculum.setEducationRecords(new ArrayList<EducationRecord>());
		newCurriculum.setEndorserRecords(new ArrayList<EndorserRecord>());
		newCurriculum.setProfessionalRecords(new ArrayList<ProfessionalRecord>());
		newCurriculum.setMiscellaneousRecords(new ArrayList<MiscellaneousRecord>());
		return newCurriculum;
	}

	public Curriculum save(final Curriculum curriculum) {
		this.isARangerAuthenticated();
		Assert.notNull(curriculum.getPersonalRecord());
		final Curriculum posibleCurriculumInDB = this.curriculumRepository.findOne(curriculum.getId());
		final Boolean edit = posibleCurriculumInDB != null;
		if (edit) {
			Assert.isTrue(curriculum.getTicker().equals(posibleCurriculumInDB.getTicker()));
			this.isTheCorrectRanger(curriculum.getId());
		} else
			curriculum.setTicker(this.getTicker());
		final Curriculum savedCurriculum = this.curriculumRepository.save(curriculum);

		Ranger principal;
		principal = this.rangerService.findByPrincipal();
		if (!edit)
			principal.setCurriculum(savedCurriculum);
		this.rangerService.saveFromEdit(principal);

		return savedCurriculum;
	}
	public void delete(final Curriculum curriculum) {
		this.isTheCorrectRanger(curriculum.getId());
		final Ranger r = this.rangerService.findByPrincipal();
		Assert.notNull(this.curriculumRepository.findOne(curriculum.getId()));
		final Curriculum curriculumInDataBase = this.curriculumRepository.findOne(curriculum.getId());
		Assert.notNull(curriculumInDataBase);
		this.miscellaneousRecordService.deleteAllByCurriculum(curriculumInDataBase);
		this.educationRecordService.deleteAllByCurriculum(curriculumInDataBase);
		this.endorserRecordService.deleteAllByCurriculum(curriculumInDataBase);
		this.professionalRecordService.deleteAllByCurriculum(curriculumInDataBase);
		this.personalRecordService.deleteByCurriculumId(curriculumInDataBase.getId());

		r.setCurriculum(null);
		this.rangerService.saveFromEdit(r);

		this.curriculumRepository.delete(curriculumInDataBase);

	}
	public Curriculum findOne(final int curriculumId) {

		final Curriculum c = this.curriculumRepository.findOne(curriculumId);

		return c;
	}
	// Other business methods
	//B-30 Find a curriculum by ranger by trip id
	public Curriculum findCurriculumByRangerByTripId(final int tripId) {
		final Ranger r = this.rangerService.findByTripId(tripId);
		Curriculum c = null;
		if (r != null)
			c = r.getCurriculum();
		return c;

	}

	public Curriculum findCurriculumByRangerId(final int rangerId) {
		final Ranger r = this.rangerService.findOne(rangerId);
		Curriculum c = null;
		if (r != null)
			c = r.getCurriculum();
		return c;

	}
	public Curriculum findCurriculumByTicker(final String ticker) {
		final Curriculum c = this.curriculumRepository.findOneByTicker(ticker);
		return c;
	}
	public Curriculum findByPersonalRecordId(final int recordId) {
		Curriculum c;
		c = this.curriculumRepository.findOneByPersonalRecordId(recordId);

		return c;
	}
	public Curriculum findByEducationRecordId(final int recordId) {
		Curriculum c;
		c = this.curriculumRepository.findOneByEducationRecordId(recordId);
		return c;
	}
	public Curriculum findByEndorserRecordId(final int recordId) {
		Curriculum c;
		c = this.curriculumRepository.findOneByEndorserRecordId(recordId);
		return c;
	}
	public Curriculum findByProfessionalRecordId(final int recordId) {
		Curriculum c;
		c = this.curriculumRepository.findOneByProfessionalRecordId(recordId);
		return c;
	}
	public Curriculum findByMiscellaneousRecordId(final int recordId) {
		Curriculum c;
		c = this.curriculumRepository.findOneByMiscellaneousRecordId(recordId);
		return c;
	}
	//Auxiliares
	private Ranger isARangerAuthenticated() {
		final Actor actor = this.actorService.findByPrincipal();

		final Collection<Authority> authorities = actor.getUserAccount().getAuthorities();
		Boolean isRanger = false;
		for (final Authority a : authorities)
			if (a.getAuthority().equals("RANGER")) {
				isRanger = true;
				break;
			}

		Assert.isTrue(isRanger);
		return (Ranger) actor;
	}
	private void isTheCorrectRanger(final int CurriculumId) {
		Assert.isTrue(this.isARangerAuthenticated().getCurriculum().getId() == CurriculumId);
	}
	private String getTicker() {
		final String year = new Integer(Calendar.getInstance().get(Calendar.YEAR)).toString().substring(2, 4);
		final Integer nMonth = new Integer(Calendar.getInstance().get(Calendar.MONTH) + 1);
		final Integer nDay = new Integer(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
		String month = nMonth.toString();
		String day = nDay.toString();
		if (nMonth < 10)
			month = "0" + month;
		if (nDay < 10)
			day = "0" + day;
		final String date = year + month + day;

		final String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		String randomString;
		String ticker;
		do {
			randomString = "";
			final Random random = new Random();
			for (int i = 0; i < 4; i++) {
				final int randomIndex = random.nextInt(alphabet.length());
				randomString = randomString + alphabet.charAt(randomIndex);
			}
			ticker = date + "-" + randomString;
		} while (this.curriculumRepository.findOneByTicker(ticker) != null);
		return ticker;
	}
}
