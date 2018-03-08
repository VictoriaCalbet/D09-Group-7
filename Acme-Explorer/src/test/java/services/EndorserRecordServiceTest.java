
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
import domain.EndorserRecord;
import domain.PersonalRecord;
import domain.Ranger;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class EndorserRecordServiceTest extends AbstractTest {

	// Service under test
	@Autowired
	private EndorserRecordService	endorserRecordService;
	@Autowired
	private RangerService			rangerService;
	@Autowired
	private CurriculumService		curriculumService;

	@Autowired
	private PersonalRecordService	personalRecordService;


	// Tests
	@Test
	public void testCreate() {

		EndorserRecord endorserRecord;
		endorserRecord = this.endorserRecordService.create();
		Assert.isNull(endorserRecord.getEmail());
		Assert.isNull(endorserRecord.getLinkedInLink());
		Assert.isNull(endorserRecord.getName());
		Assert.isNull(endorserRecord.getPhone());
		Assert.isNull(endorserRecord.getComments());

	}
	@Test
	public void testSaved() {
		EndorserRecord endorserRecord;
		endorserRecord = this.endorserRecordService.create();
		endorserRecord.setEmail("endorser@mail.com");
		endorserRecord.setLinkedInLink("http://www.linkedin.com/endorser");
		endorserRecord.setName("Endorser");
		endorserRecord.setPhone("+34954333222");
		endorserRecord.setComments("comments...");

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
		final EndorserRecord savedEndorserRecord = this.endorserRecordService.save(endorserRecord);
		final Curriculum curriculum = this.rangerService.findByPrincipal().getCurriculum();

		final EndorserRecord endorserRecordInDB = this.endorserRecordService.findOne(savedEndorserRecord.getId());

		Assert.notNull(endorserRecordInDB.getEmail());
		Assert.notNull(endorserRecordInDB.getLinkedInLink());
		Assert.notNull(endorserRecordInDB.getName());
		Assert.notNull(endorserRecordInDB.getPhone());

		//Usando save para editar

		savedEndorserRecord.setLinkedInLink("http://www.linkedin.com/perfil2");

		this.endorserRecordService.save(savedEndorserRecord);

		Assert.isTrue(new ArrayList<EndorserRecord>(this.curriculumService.findOne(curriculum.getId()).getEndorserRecords()).get(0).getLinkedInLink().equals("http://www.linkedin.com/perfil2"));
		this.unauthenticate();

	}

	@Test
	public void testDelete() {
		EndorserRecord endorserRecord;
		endorserRecord = this.endorserRecordService.create();
		endorserRecord = this.endorserRecordService.create();
		endorserRecord.setEmail("endorser@mail.com");
		endorserRecord.setLinkedInLink("http://www.linkedin.com/endorser");
		endorserRecord.setName("Endorser");
		endorserRecord.setPhone("+34954333222");
		endorserRecord.setComments("comments...");

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
		// Salvar endorserRecord y comment
		this.authenticate("Manuel");
		PersonalRecord personalRecord;
		personalRecord = this.personalRecordService.create();
		personalRecord.setEmail("ranger1@mail.com");
		personalRecord.setLinkedInLink("https://ae.linkedin.com/ranger1");
		personalRecord.setName("Ranger1 Rangerson");
		personalRecord.setPhone("349544322439");
		personalRecord.setPhoto("http://www.estosonfotos.com/foto1");
		this.personalRecordService.save(personalRecord);
		final EndorserRecord savedEndorserRecord = this.endorserRecordService.save(endorserRecord);

		//Borrar
		final int endorserRecordId = savedEndorserRecord.getId();

		Assert.notNull(this.endorserRecordService.findOne(endorserRecordId));

		this.endorserRecordService.delete(savedEndorserRecord);
		Assert.isNull(this.endorserRecordService.findOne(endorserRecordId));

		this.unauthenticate();

	}
}
