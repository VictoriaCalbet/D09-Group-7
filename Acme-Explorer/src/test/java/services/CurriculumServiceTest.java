
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import security.UserAccount;
import utilities.AbstractTest;
import domain.Curriculum;
import domain.EducationRecord;
import domain.EndorserRecord;
import domain.MiscellaneousRecord;
import domain.PersonalRecord;
import domain.ProfessionalRecord;
import domain.Ranger;
import domain.Trip;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class CurriculumServiceTest extends AbstractTest {

	// Service under test
	@Autowired
	private CurriculumService			curriculumService;
	@Autowired
	private TripService					tripService;
	@Autowired
	private RangerService				rangerService;
	@Autowired
	private PersonalRecordService		personalRecordService;
	@Autowired
	private EducationRecordService		educationRecordService;
	@Autowired
	private EndorserRecordService		endorserRecordService;
	@Autowired
	private MiscellaneousRecordService	miscellaneousRecordService;
	@Autowired
	private ProfessionalRecordService	professionalRecordService;


	// Tests
	@Test
	public void testCreate() {
		final Curriculum curriculum;
		curriculum = this.curriculumService.create();
		Assert.isNull(curriculum.getPersonalRecord());
		Assert.isTrue(curriculum.getEducationRecords().isEmpty());
		Assert.isTrue(curriculum.getEndorserRecords().isEmpty());
		Assert.isTrue(curriculum.getProfessionalRecords().isEmpty());
		Assert.isTrue(curriculum.getMiscellaneousRecords().isEmpty());
		Assert.isNull(curriculum.getTicker());
	}

	@Test
	public void testFindCurriculumByRangerId() {
		//Ranger
		this.authenticate("admin");
		final Ranger ranger = this.rangerService.create();
		ranger.setName("New ranger name");
		ranger.setSurname("New ranger surname");
		ranger.setEmail("newranger@ranger.com");
		ranger.setPhone("+34954954954");
		ranger.setAddress("New ranger address");

		UserAccount userAccountToSave;
		userAccountToSave = ranger.getUserAccount();
		userAccountToSave.setUsername("Manuel");
		userAccountToSave.setPassword("Manuel");
		ranger.setUserAccount(userAccountToSave);

		Ranger savedRanger;
		savedRanger = this.rangerService.saveFromCreate(ranger);
		this.unauthenticate();
		//Personal record
		this.authenticate("Manuel");
		PersonalRecord personalRecord;
		personalRecord = this.personalRecordService.create();
		personalRecord.setEmail("ranger1@mail.com");
		personalRecord.setLinkedInLink("https://ae.linkedin.com/ranger1");
		personalRecord.setName("Ranger1 Rangerson");
		personalRecord.setPhone("349544322439");
		personalRecord.setPhoto("http://www.estosonfotos.com/foto1");
		final PersonalRecord savedPersonalRecord = this.personalRecordService.save(personalRecord);
		//Curriculum
		Curriculum curriculum;
		curriculum = this.curriculumService.create();
		curriculum.setPersonalRecord(savedPersonalRecord);
		final Curriculum savedCurriculum = this.curriculumService.save(curriculum);
		savedRanger.setCurriculum(savedCurriculum);
		this.rangerService.saveFromEdit(savedRanger);

		Assert.isTrue(this.curriculumService.findCurriculumByRangerId(savedRanger.getId()).equals(savedCurriculum));

	}
	@Test
	public void testFindCurriculumByRangerByTripId() {
		//Trip
		final Trip savedTrip = this.tripService.findAll().iterator().next();
		//Personal record
		this.authenticate(savedTrip.getRanger().getUserAccount().getUsername());
		PersonalRecord personalRecord;
		personalRecord = this.personalRecordService.create();
		personalRecord.setEmail("ranger1@mail.com");
		personalRecord.setLinkedInLink("https://ae.linkedin.com/ranger1");
		personalRecord.setName("Ranger1 Rangerson");
		personalRecord.setPhone("349544322439");
		personalRecord.setPhoto("http://www.estosonfotos.com/foto1");
		final PersonalRecord savedPersonalRecord = this.personalRecordService.save(personalRecord);
		//Curriculum
		Curriculum curriculum;
		curriculum = this.curriculumService.create();
		curriculum.setPersonalRecord(savedPersonalRecord);
		final Curriculum savedCurriculum = this.curriculumService.save(curriculum);
		this.unauthenticate();

		//this.categoryService.referenceTrip(category, savedTrip);

		//		savedRanger.getTrips().add(savedTrip);
		//		this.rangerService.saveFromEdit(savedRanger);
		Assert.isTrue(this.curriculumService.findCurriculumByRangerByTripId(savedTrip.getId()).equals(savedCurriculum));
	}

	@Test
	public void testSave() {
		//Ranger
		this.authenticate("admin");
		final Ranger ranger = this.rangerService.create();
		ranger.setName("New ranger name");
		ranger.setSurname("New ranger surname");
		ranger.setEmail("newranger@ranger.com");
		ranger.setPhone("+34954954954");
		ranger.setAddress("New ranger address");

		UserAccount userAccountToSave;
		userAccountToSave = ranger.getUserAccount();
		userAccountToSave.setUsername("Manuel");
		userAccountToSave.setPassword("Manuel");
		ranger.setUserAccount(userAccountToSave);

		this.rangerService.saveFromCreate(ranger);
		this.unauthenticate();

		//Personal record
		this.authenticate("Manuel");
		PersonalRecord personalRecord;
		personalRecord = this.personalRecordService.create();
		personalRecord.setEmail("ranger1@mail.com");
		personalRecord.setLinkedInLink("https://ae.linkedin.com/ranger1");
		personalRecord.setName("Ranger1 Rangerson");
		personalRecord.setPhone("349544322439");
		personalRecord.setPhoto("http://www.estosonfotos.com/foto1");
		this.personalRecordService.save(personalRecord);

		Ranger principal;
		principal = this.rangerService.findByPrincipal();

		//
		//Curriculum
		final Curriculum curriculumInDB = principal.getCurriculum();
		Assert.notNull(curriculumInDB.getPersonalRecord());
		Assert.notNull(curriculumInDB.getTicker());

		//Editar

		//EducationRecord
		EducationRecord educationRecord;
		educationRecord = this.educationRecordService.create();
		educationRecord.setAttachmentLink("http://www.thisisaattachmentlink.com");
		educationRecord.setDiplomaTitle("Diploma1");
		educationRecord.setInstitution("Institution1");
		educationRecord.setStartDate(new Date(System.currentTimeMillis() - 1));
		final EducationRecord savedEducationRecord = this.educationRecordService.save(educationRecord);
		Assert.isTrue(new ArrayList<EducationRecord>(this.curriculumService.findByEducationRecordId(savedEducationRecord.getId()).getEducationRecords()).get(0).equals(savedEducationRecord));
		this.unauthenticate();
	}
	@Test
	public void testDelete() {
		//Ranger
		this.authenticate("admin");
		final Ranger ranger = this.rangerService.create();
		ranger.setName("New ranger name");
		ranger.setSurname("New ranger surname");
		ranger.setEmail("newranger@ranger.com");
		ranger.setPhone("+34954954954");
		ranger.setAddress("New ranger address");

		UserAccount userAccountToSave;
		userAccountToSave = ranger.getUserAccount();
		userAccountToSave.setUsername("Manuel");
		userAccountToSave.setPassword("Manuel");
		ranger.setUserAccount(userAccountToSave);

		this.rangerService.saveFromCreate(ranger);
		this.unauthenticate();
		//Personal record
		this.authenticate("Manuel");
		PersonalRecord personalRecord;
		personalRecord = this.personalRecordService.create();
		personalRecord.setEmail("ranger1@mail.com");
		personalRecord.setLinkedInLink("https://ae.linkedin.com/ranger1");
		personalRecord.setName("Ranger1 Rangerson");
		personalRecord.setPhone("349544322439");
		personalRecord.setPhoto("http://www.estosonfotos.com/foto1");
		final PersonalRecord savedPersonalRecord = this.personalRecordService.save(personalRecord);
		//Curriculum
		final Curriculum curriculum = this.rangerService.findByPrincipal().getCurriculum();
		Assert.notNull(curriculum);
		//EducationRecord

		EducationRecord educationRecord;
		educationRecord = this.educationRecordService.create();
		educationRecord.setAttachmentLink("http://www.thisisaattachmentlink.com");
		educationRecord.setDiplomaTitle("Diploma1");
		educationRecord.setInstitution("Institution1");
		educationRecord.setStartDate(new Date(System.currentTimeMillis() - 1));
		educationRecord.setComments("comment");
		final EducationRecord savedEducationRecord = this.educationRecordService.save(educationRecord);

		//EndorserRecord

		EndorserRecord endorserRecord;
		endorserRecord = this.endorserRecordService.create();
		endorserRecord.setEmail("endorser@mail.com");
		endorserRecord.setLinkedInLink("http://www.linkedin.com/endorser");
		endorserRecord.setName("Endorser");
		endorserRecord.setPhone("+34954333222");
		endorserRecord.setComments("comment");
		final EndorserRecord savedEndorserRecord = this.endorserRecordService.save(endorserRecord);

		//MiscellaneousRecord
		MiscellaneousRecord miscellaneousRecord;
		miscellaneousRecord = this.miscellaneousRecordService.create();
		miscellaneousRecord.setAttachmentLink("http://www.thisisaattachmentlink.com");
		miscellaneousRecord.setTitle("Miscellaneous Record");
		miscellaneousRecord.setComments("comment");
		final MiscellaneousRecord savedMiscellaneousRecord = this.miscellaneousRecordService.save(miscellaneousRecord);

		//ProfessionalRecord
		ProfessionalRecord professionalRecord;
		professionalRecord = this.professionalRecordService.create();
		professionalRecord.setAttachmentLink("http://www.thisisaattachmentlink.com");
		professionalRecord.setCompanyName("Company");
		professionalRecord.setRole("Role");
		professionalRecord.setStartDate(new Date(System.currentTimeMillis() - 1));
		professionalRecord.setComments("comment");
		final ProfessionalRecord savedProfessionalRecord = this.professionalRecordService.save(professionalRecord);

		Assert.notNull(curriculum.getPersonalRecord());
		Assert.notNull(curriculum.getTicker());
		Assert.notNull(this.educationRecordService.findOne(savedEducationRecord.getId()));
		Assert.notNull(this.endorserRecordService.findOne(savedEndorserRecord.getId()));

		Assert.notNull(this.miscellaneousRecordService.findOne(savedMiscellaneousRecord.getId()));
		Assert.notNull(this.professionalRecordService.findOne(savedProfessionalRecord.getId()));

		//Borrado
		final int curriculumId = curriculum.getId();
		final int personalRecordId = savedPersonalRecord.getId();
		final int professionalRecordId = savedProfessionalRecord.getId();
		final int endorserRecordId = savedEndorserRecord.getId();
		final int miscellaneousRecordId = savedMiscellaneousRecord.getId();
		final int educationRecordId = savedEducationRecord.getId();

		this.curriculumService.delete(curriculum);
		Assert.isNull(this.curriculumService.findOne(curriculumId));
		Assert.isNull(this.personalRecordService.findOne(personalRecordId));
		Assert.isNull(this.miscellaneousRecordService.findOne(miscellaneousRecordId));
		Assert.isNull(this.professionalRecordService.findOne(professionalRecordId));
		Assert.isNull(this.endorserRecordService.findOne(endorserRecordId));
		Assert.isNull(this.educationRecordService.findOne(educationRecordId));
	}

	@Test
	public void testSuspicious1() {

		Collection<Ranger> allRangers;
		allRangers = this.rangerService.findAll();
		Ranger principal = null;

		for (final Ranger ranger : allRangers)
			if (!ranger.getIsSuspicious() && ranger.getCurriculum() == null) {
				principal = ranger;
				break;
			}

		Assert.notNull(principal);

		// Ranger3
		this.authenticate(principal.getUserAccount().getUsername());

		PersonalRecord personalRecordToSave;

		Assert.isTrue(!principal.getIsSuspicious());

		PersonalRecord personalRecord;
		personalRecord = this.personalRecordService.create();
		personalRecord.setEmail("ranger3@mail.com");
		personalRecord.setLinkedInLink("https://ae.linkedin.com/ranger3");
		personalRecord.setName("Ranger3 Rangerson");
		personalRecord.setPhone("349544322439");
		personalRecord.setPhoto("http://www.estosonfotos.com/foto1");
		personalRecordToSave = this.personalRecordService.save(personalRecord);

		Assert.notNull(personalRecordToSave);

		principal = this.rangerService.findByPrincipal();
		Assert.notNull(principal);
		Assert.notNull(principal.getCurriculum());
		Assert.notNull(principal.getCurriculum().getPersonalRecord());
		Assert.isTrue(!principal.getIsSuspicious());

		this.unauthenticate();

		// Ranger3
		this.authenticate(principal.getUserAccount().getUsername());

		PersonalRecord personalRecordToRetrieve;

		Assert.isTrue(!principal.getIsSuspicious());

		personalRecordToSave.setName("sex");
		personalRecordToRetrieve = this.personalRecordService.save(personalRecordToSave);

		Assert.notNull(personalRecordToRetrieve);

		Assert.isTrue(principal.getIsSuspicious());

		this.unauthenticate();

	}

	@Test
	public void testSuspicious2() {

		Collection<Ranger> allRangers;
		allRangers = this.rangerService.findAll();
		Ranger principal = null;

		for (final Ranger ranger : allRangers)
			if (!ranger.getIsSuspicious() && ranger.getCurriculum() == null) {
				principal = ranger;
				break;
			}

		Assert.notNull(principal);

		// Ranger3
		this.authenticate(principal.getUserAccount().getUsername());

		PersonalRecord personalRecordToSave;
		EducationRecord educationRecordToSave;

		Assert.isTrue(!principal.getIsSuspicious());

		PersonalRecord personalRecord;
		personalRecord = this.personalRecordService.create();
		personalRecord.setEmail("ranger3@mail.com");
		personalRecord.setLinkedInLink("https://ae.linkedin.com/ranger3");
		personalRecord.setName("Ranger3 Rangerson");
		personalRecord.setPhone("349544322439");
		personalRecord.setPhoto("http://www.estosonfotos.com/foto1");
		personalRecordToSave = this.personalRecordService.save(personalRecord);

		Assert.notNull(personalRecordToSave);

		principal = this.rangerService.findByPrincipal();
		Assert.notNull(principal);
		Assert.notNull(principal.getCurriculum());
		Assert.notNull(principal.getCurriculum().getPersonalRecord());
		Assert.isTrue(!principal.getIsSuspicious());

		EducationRecord educationRecord;
		educationRecord = this.educationRecordService.create();
		educationRecord.setAttachmentLink("http://www.thisisaattachmentlink.com");
		educationRecord.setDiplomaTitle("Diploma1");
		educationRecord.setInstitution("Institution1");
		educationRecord.setStartDate(new Date(System.currentTimeMillis() - 1));

		educationRecordToSave = this.educationRecordService.save(educationRecord);

		Assert.notNull(educationRecordToSave);
		Assert.notNull(principal.getCurriculum().getEducationRecords());
		Assert.isTrue(!principal.getIsSuspicious());

		this.unauthenticate();

		// Ranger3
		this.authenticate(principal.getUserAccount().getUsername());

		PersonalRecord personalRecordToRetrieve;

		Assert.isTrue(!principal.getIsSuspicious());

		personalRecordToSave.setName("sex");
		personalRecordToRetrieve = this.personalRecordService.save(personalRecordToSave);

		Assert.notNull(personalRecordToRetrieve);

		Assert.isTrue(principal.getIsSuspicious());

		this.unauthenticate();

	}

	@Test
	public void testSuspicious3() {

		Collection<Ranger> allRangers;
		allRangers = this.rangerService.findAll();
		Ranger principal = null;

		for (final Ranger ranger : allRangers)
			if (!ranger.getIsSuspicious() && ranger.getCurriculum() == null) {
				principal = ranger;
				break;
			}

		Assert.notNull(principal);

		// Ranger3
		this.authenticate(principal.getUserAccount().getUsername());

		PersonalRecord personalRecordToSave;
		EducationRecord educationRecordToSave;

		Assert.isTrue(!principal.getIsSuspicious());

		PersonalRecord personalRecord;
		personalRecord = this.personalRecordService.create();
		personalRecord.setEmail("ranger3@mail.com");
		personalRecord.setLinkedInLink("https://ae.linkedin.com/ranger3");
		personalRecord.setName("Ranger3 Rangerson");
		personalRecord.setPhone("349544322439");
		personalRecord.setPhoto("http://www.estosonfotos.com/foto1");
		personalRecordToSave = this.personalRecordService.save(personalRecord);

		Assert.notNull(personalRecordToSave);

		principal = this.rangerService.findByPrincipal();
		Assert.notNull(principal);
		Assert.notNull(principal.getCurriculum());
		Assert.notNull(principal.getCurriculum().getPersonalRecord());
		Assert.isTrue(!principal.getIsSuspicious());

		EducationRecord educationRecord;
		educationRecord = this.educationRecordService.create();
		educationRecord.setAttachmentLink("http://www.thisisaattachmentlink.com");
		educationRecord.setDiplomaTitle("Diploma1");
		educationRecord.setInstitution("Institution1");
		educationRecord.setStartDate(new Date(System.currentTimeMillis() - 1));
		educationRecordToSave = this.educationRecordService.save(educationRecord);

		Assert.notNull(principal.getCurriculum().getEducationRecords());
		Assert.isTrue(!principal.getIsSuspicious());

		this.unauthenticate();

		// Ranger3
		this.authenticate(principal.getUserAccount().getUsername());

		EducationRecord educationRecordToRetrieve;

		Assert.isTrue(!principal.getIsSuspicious());

		educationRecordToSave.setDiplomaTitle("sex");
		educationRecordToRetrieve = this.educationRecordService.save(educationRecordToSave);

		Assert.notNull(educationRecordToRetrieve);

		Assert.isTrue(principal.getIsSuspicious());

		this.unauthenticate();

	}

	@Test
	public void testSuspicious4() {

		Collection<Ranger> allRangers;
		allRangers = this.rangerService.findAll();
		Ranger principal = null;

		for (final Ranger ranger : allRangers)
			if (!ranger.getIsSuspicious() && ranger.getCurriculum() == null) {
				principal = ranger;
				break;
			}

		Assert.notNull(principal);

		// Ranger3
		this.authenticate(principal.getUserAccount().getUsername());

		PersonalRecord personalRecordToSave;
		EducationRecord educationRecordToSave;

		Assert.isTrue(!principal.getIsSuspicious());

		PersonalRecord personalRecord;
		personalRecord = this.personalRecordService.create();
		personalRecord.setEmail("ranger3@mail.com");
		personalRecord.setLinkedInLink("https://ae.linkedin.com/ranger3");
		personalRecord.setName("Ranger3 Rangerson");
		personalRecord.setPhone("349544322439");
		personalRecord.setPhoto("http://www.estosonfotos.com/foto1");

		personalRecordToSave = this.personalRecordService.save(personalRecord);

		Assert.notNull(personalRecordToSave);

		principal = this.rangerService.findByPrincipal();
		Assert.notNull(principal);
		Assert.notNull(principal.getCurriculum());
		Assert.notNull(principal.getCurriculum().getPersonalRecord());
		Assert.isTrue(!principal.getIsSuspicious());

		EducationRecord educationRecord;
		educationRecord = this.educationRecordService.create();
		educationRecord.setAttachmentLink("http://www.thisisaattachmentlink.com");
		educationRecord.setDiplomaTitle("Diploma1");
		educationRecord.setInstitution("Institution1");
		educationRecord.setStartDate(new Date(System.currentTimeMillis() - 1));
		educationRecord.setComments("This is a comment");
		educationRecordToSave = this.educationRecordService.save(educationRecord);

		Assert.notNull(principal.getCurriculum().getEducationRecords());
		Assert.isTrue(!principal.getIsSuspicious());

		this.unauthenticate();

		// Ranger3
		this.authenticate(principal.getUserAccount().getUsername());

		EducationRecord educationRecordInDB;

		educationRecordInDB = this.educationRecordService.findOne(educationRecordToSave.getId());

		Assert.isTrue(!principal.getIsSuspicious());
		educationRecordInDB.setComments("sex");
		this.educationRecordService.save(educationRecordInDB);

		Assert.isTrue(principal.getIsSuspicious());

		this.unauthenticate();

	}
}
