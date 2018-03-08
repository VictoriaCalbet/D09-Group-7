
package services;

import java.util.ArrayList;
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
import domain.PersonalRecord;
import domain.Ranger;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class EducationRecordServiceTest extends AbstractTest {

	// Service under test
	@Autowired
	private EducationRecordService	educationRecordService;
	@Autowired
	private RangerService			rangerService;
	@Autowired
	private CurriculumService		curriculumService;

	@Autowired
	private PersonalRecordService	personalRecordService;


	// Tests
	@Test
	public void testCreate() {

		EducationRecord educationRecord;
		educationRecord = this.educationRecordService.create();
		Assert.isNull(educationRecord.getAttachmentLink());
		Assert.isNull(educationRecord.getDiplomaTitle());
		Assert.isNull(educationRecord.getInstitution());
		Assert.isNull(educationRecord.getEndDate());
		Assert.isNull(educationRecord.getStartDate());
		Assert.isNull(educationRecord.getComments());

	}
	@Test
	public void testSaved() {
		EducationRecord educationRecord;
		educationRecord = this.educationRecordService.create();
		educationRecord.setAttachmentLink("http://www.thisisaattachmentlink.com");
		educationRecord.setDiplomaTitle("Diploma1");
		educationRecord.setInstitution("Institution1");
		educationRecord.setStartDate(new Date(System.currentTimeMillis() - 1));
		educationRecord.setComments("comments...");

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
		//Comienzan las operaciones de guardado
		this.authenticate("Manuel");
		PersonalRecord personalRecord;
		personalRecord = this.personalRecordService.create();
		personalRecord.setEmail("ranger1@mail.com");
		personalRecord.setLinkedInLink("https://ae.linkedin.com/ranger1");
		personalRecord.setName("Ranger1 Rangerson");
		personalRecord.setPhone("349544322439");
		personalRecord.setPhoto("http://www.estosonfotos.com/foto1");
		this.personalRecordService.save(personalRecord);
		final EducationRecord savedEducationRecord = this.educationRecordService.save(educationRecord);
		final Curriculum curriculum = this.rangerService.findByPrincipal().getCurriculum();

		final EducationRecord educationRecordInDB = this.educationRecordService.findOne(savedEducationRecord.getId());

		Assert.notNull(educationRecordInDB.getAttachmentLink());
		Assert.notNull(educationRecordInDB.getDiplomaTitle());
		Assert.notNull(educationRecordInDB.getStartDate());
		Assert.notNull(educationRecordInDB.getInstitution());

		//Usando save para editar

		savedEducationRecord.setAttachmentLink("http://www.thisisaattachmentlink.com/attachment");
		this.educationRecordService.save(savedEducationRecord);
		Assert.isTrue(new ArrayList<EducationRecord>(this.curriculumService.findOne(curriculum.getId()).getEducationRecords()).get(0).getAttachmentLink().equals("http://www.thisisaattachmentlink.com/attachment"));
		this.unauthenticate();

	}

	@Test
	public void testDelete() {
		EducationRecord educationRecord;
		educationRecord = this.educationRecordService.create();
		educationRecord.setAttachmentLink("http://www.thisisaattachmentlink.com");
		educationRecord.setDiplomaTitle("Diploma1");
		educationRecord.setInstitution("Institution1");
		educationRecord.setStartDate(new Date(System.currentTimeMillis() - 1));
		educationRecord.setComments("comments...");
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
		// Salvar educationRecord y comment
		this.authenticate("Manuel");
		PersonalRecord personalRecord;
		personalRecord = this.personalRecordService.create();
		personalRecord.setEmail("ranger1@mail.com");
		personalRecord.setLinkedInLink("https://ae.linkedin.com/ranger1");
		personalRecord.setName("Ranger1 Rangerson");
		personalRecord.setPhone("349544322439");
		personalRecord.setPhoto("http://www.estosonfotos.com/foto1");
		this.personalRecordService.save(personalRecord);
		final EducationRecord savedEducationRecord = this.educationRecordService.save(educationRecord);

		//Borrar
		final int educationRecordId = savedEducationRecord.getId();

		Assert.notNull(this.educationRecordService.findOne(educationRecordId));

		this.educationRecordService.delete(savedEducationRecord);
		Assert.isNull(this.educationRecordService.findOne(educationRecordId));

		this.unauthenticate();

	}
}
