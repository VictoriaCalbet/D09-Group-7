
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
import domain.PersonalRecord;
import domain.ProfessionalRecord;
import domain.Ranger;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class ProfessionalRecordServiceTest extends AbstractTest {

	// Service under test
	@Autowired
	private ProfessionalRecordService	professionalRecordService;
	@Autowired
	private RangerService				rangerService;
	@Autowired
	private CurriculumService			curriculumService;

	@Autowired
	private PersonalRecordService		personalRecordService;


	// Tests
	@Test
	public void testCreate() {

		ProfessionalRecord professionalRecord;
		professionalRecord = this.professionalRecordService.create();
		Assert.isNull(professionalRecord.getAttachmentLink());
		Assert.isNull(professionalRecord.getCompanyName());
		Assert.isNull(professionalRecord.getEndDate());
		Assert.isNull(professionalRecord.getRole());
		Assert.isNull(professionalRecord.getStartDate());
		Assert.isNull(professionalRecord.getComments());

	}
	@Test
	public void testSaved() {
		ProfessionalRecord professionalRecord;
		professionalRecord = this.professionalRecordService.create();
		professionalRecord.setAttachmentLink("http://www.thisisaattachmentlink.com");
		professionalRecord.setCompanyName("Company");
		professionalRecord.setRole("Role");
		professionalRecord.setStartDate(new Date(System.currentTimeMillis() - 1));
		professionalRecord.setComments("comments...");

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
		final ProfessionalRecord savedProfessionalRecord = this.professionalRecordService.save(professionalRecord);
		final Curriculum curriculum = this.rangerService.findByPrincipal().getCurriculum();

		final ProfessionalRecord professionalRecordInDB = this.professionalRecordService.findOne(savedProfessionalRecord.getId());

		Assert.notNull(professionalRecordInDB.getAttachmentLink());
		Assert.notNull(professionalRecordInDB.getCompanyName());
		Assert.notNull(professionalRecordInDB.getStartDate());
		Assert.notNull(professionalRecordInDB.getRole());

		//Usando save para editar

		savedProfessionalRecord.setAttachmentLink("http://www.thisisaattachmentlink.com/attachment");

		this.professionalRecordService.save(savedProfessionalRecord);
		Assert.isTrue(new ArrayList<ProfessionalRecord>(this.curriculumService.findOne(curriculum.getId()).getProfessionalRecords()).get(0).getAttachmentLink().equals("http://www.thisisaattachmentlink.com/attachment"));
		this.unauthenticate();

	}

	@Test
	public void testDelete() {
		ProfessionalRecord professionalRecord;
		professionalRecord = this.professionalRecordService.create();
		professionalRecord.setAttachmentLink("http://www.thisisaattachmentlink.com");
		professionalRecord.setCompanyName("Company");
		professionalRecord.setRole("Role");
		professionalRecord.setStartDate(new Date(System.currentTimeMillis() - 1));
		professionalRecord.setComments("comments...");

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
		// Salvar professionalRecord y comment
		this.authenticate("Manuel");
		PersonalRecord personalRecord;
		personalRecord = this.personalRecordService.create();
		personalRecord.setEmail("ranger1@mail.com");
		personalRecord.setLinkedInLink("https://ae.linkedin.com/ranger1");
		personalRecord.setName("Ranger1 Rangerson");
		personalRecord.setPhone("349544322439");
		personalRecord.setPhoto("http://www.estosonfotos.com/foto1");
		this.personalRecordService.save(personalRecord);
		final ProfessionalRecord savedProfessionalRecord = this.professionalRecordService.save(professionalRecord);

		;

		final int professionalRecordId = savedProfessionalRecord.getId();

		Assert.notNull(this.professionalRecordService.findOne(professionalRecordId));

		this.professionalRecordService.delete(savedProfessionalRecord);
		Assert.isNull(this.professionalRecordService.findOne(professionalRecordId));

		this.unauthenticate();

	}
}
