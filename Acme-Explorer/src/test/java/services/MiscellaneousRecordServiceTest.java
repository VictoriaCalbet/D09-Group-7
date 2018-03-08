
package services;

import java.util.ArrayList;

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
import domain.MiscellaneousRecord;
import domain.PersonalRecord;
import domain.Ranger;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class MiscellaneousRecordServiceTest extends AbstractTest {

	// Service under test
	@Autowired
	private MiscellaneousRecordService	miscellaneousRecordService;
	@Autowired
	private RangerService				rangerService;
	@Autowired
	private CurriculumService			curriculumService;

	@Autowired
	private PersonalRecordService		personalRecordService;


	// Tests
	@Test
	public void testCreate() {

		MiscellaneousRecord miscellaneousRecord;
		miscellaneousRecord = this.miscellaneousRecordService.create();
		Assert.isNull(miscellaneousRecord.getAttachmentLink());
		Assert.isNull(miscellaneousRecord.getTitle());
		Assert.isNull(miscellaneousRecord.getComments());

	}
	@Test
	public void testSaved() {
		MiscellaneousRecord miscellaneousRecord;
		miscellaneousRecord = this.miscellaneousRecordService.create();
		miscellaneousRecord.setAttachmentLink("http://www.thisisaattachmentlink.com");
		miscellaneousRecord.setTitle("Miscellaneous Record");
		miscellaneousRecord.setComments("comments...");

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
		final MiscellaneousRecord savedMiscellaneousRecord = this.miscellaneousRecordService.save(miscellaneousRecord);
		final Curriculum curriculum = this.rangerService.findByPrincipal().getCurriculum();

		final MiscellaneousRecord miscellaneousRecordInDB = this.miscellaneousRecordService.findOne(savedMiscellaneousRecord.getId());

		Assert.notNull(miscellaneousRecordInDB.getAttachmentLink());
		Assert.notNull(miscellaneousRecordInDB.getTitle());

		//Usando save para editar

		savedMiscellaneousRecord.setAttachmentLink("http://www.thisisaattachmentlink.com/attachment");

		this.miscellaneousRecordService.save(savedMiscellaneousRecord);
		Assert.isTrue(new ArrayList<MiscellaneousRecord>(this.curriculumService.findOne(curriculum.getId()).getMiscellaneousRecords()).get(0).getAttachmentLink().equals("http://www.thisisaattachmentlink.com/attachment"));
		this.unauthenticate();

	}
	@Test
	public void testDelete() {
		MiscellaneousRecord miscellaneousRecord;
		miscellaneousRecord = this.miscellaneousRecordService.create();
		miscellaneousRecord.setAttachmentLink("http://www.thisisaattachmentlink.com");
		miscellaneousRecord.setTitle("Miscellaneous Record");
		miscellaneousRecord.setComments("comments...");

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
		// Salvar miscellaneousRecord y comment
		this.authenticate("Manuel");
		PersonalRecord personalRecord;
		personalRecord = this.personalRecordService.create();
		personalRecord.setEmail("ranger1@mail.com");
		personalRecord.setLinkedInLink("https://ae.linkedin.com/ranger1");
		personalRecord.setName("Ranger1 Rangerson");
		personalRecord.setPhone("349544322439");
		personalRecord.setPhoto("http://www.estosonfotos.com/foto1");
		this.personalRecordService.save(personalRecord);
		final MiscellaneousRecord savedMiscellaneousRecord = this.miscellaneousRecordService.save(miscellaneousRecord);

		//Borrar
		final int miscellaneousRecordId = savedMiscellaneousRecord.getId();

		Assert.notNull(this.miscellaneousRecordService.findOne(miscellaneousRecordId));

		this.miscellaneousRecordService.delete(savedMiscellaneousRecord);
		Assert.isNull(this.miscellaneousRecordService.findOne(miscellaneousRecordId));

		this.unauthenticate();

	}
}
