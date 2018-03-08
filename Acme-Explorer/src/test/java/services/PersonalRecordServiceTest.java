
package services;

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
import domain.Ranger;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class PersonalRecordServiceTest extends AbstractTest {

	// Service under test
	@Autowired
	private PersonalRecordService	personalRecordService;

	@Autowired
	private CurriculumService		curriculumService;

	@Autowired
	private RangerService			rangerService;


	// Tests
	@Test
	public void testCreate() {
		PersonalRecord personalRecord;
		personalRecord = this.personalRecordService.create();
		Assert.isNull(personalRecord.getEmail());
		Assert.isNull(personalRecord.getLinkedInLink());
		Assert.isNull(personalRecord.getName());
		Assert.isNull(personalRecord.getPhone());
		Assert.isNull(personalRecord.getPhoto());

	}
	@Test
	public void testSaved() {
		PersonalRecord personalRecord;
		personalRecord = this.personalRecordService.create();
		personalRecord.setEmail("ranger1@mail.com");
		personalRecord.setLinkedInLink("https://ae.linkedin.com/ranger1");
		personalRecord.setName("Ranger1 Rangerson");
		personalRecord.setPhone("349544322439");
		personalRecord.setPhoto("http://www.estosonfotos.com/foto1");
		PersonalRecord savedPersonalRecord;
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
		//Salvar record
		this.authenticate("Manuel");
		savedPersonalRecord = this.personalRecordService.save(personalRecord);
		this.unauthenticate();
		Assert.notNull(savedPersonalRecord.getEmail());
		Assert.notNull(savedPersonalRecord.getLinkedInLink());
		Assert.notNull(savedPersonalRecord.getName());
		Assert.notNull(savedPersonalRecord.getPhone());
		Assert.notNull(savedPersonalRecord.getPhoto());
		//Se prueba findOne
		Assert.isTrue(this.personalRecordService.findOne(savedPersonalRecord.getId()).equals(savedPersonalRecord));
	}
	@Test
	public void testSaved2() {
		//Record
		PersonalRecord personalRecord;
		personalRecord = this.personalRecordService.create();
		personalRecord.setEmail("ranger1@mail.com");
		personalRecord.setLinkedInLink("https://ae.linkedin.com/ranger1");
		personalRecord.setName("Ranger1 Rangerson");
		personalRecord.setPhone("349544322439");
		personalRecord.setPhoto("http://www.estosonfotos.com/foto1");
		PersonalRecord savedPersonalRecord;
		//ranger
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
		this.authenticate("Manuel");
		//salvar record
		savedPersonalRecord = this.personalRecordService.save(personalRecord);

		final Curriculum savedCurriculum = this.rangerService.findByPrincipal().getCurriculum();

		//Edit

		savedPersonalRecord.setPhoto("http://www.estosonfotos.com/foto2");
		this.personalRecordService.save(savedPersonalRecord);
		//Probamos que findByCurriculumId funciona
		Assert.isTrue(this.personalRecordService.findByCurriculumId(savedCurriculum.getId()).getId() == savedPersonalRecord.getId());
		Assert.isTrue(this.personalRecordService.findByCurriculumId(savedCurriculum.getId()).getPhoto().equals("http://www.estosonfotos.com/foto2"));
		this.unauthenticate();

	}

	@Test
	public void testdeleteByCurriculumId() {
		//Record
		PersonalRecord personalRecord;
		personalRecord = this.personalRecordService.create();
		personalRecord.setEmail("ranger1@mail.com");
		personalRecord.setLinkedInLink("https://ae.linkedin.com/ranger1");
		personalRecord.setName("Ranger1 Rangerson");
		personalRecord.setPhone("349544322439");
		personalRecord.setPhoto("http://www.estosonfotos.com/foto1");
		PersonalRecord savedPersonalRecord;
		//ranger
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
		this.authenticate("Manuel");
		//salvar record
		savedPersonalRecord = this.personalRecordService.save(personalRecord);

		final Curriculum savedCurriculum = this.rangerService.findByPrincipal().getCurriculum();

		//Comprobar que el PersonalRecord existe

		final int savedPersonalRecordId = savedPersonalRecord.getId();
		Assert.notNull(this.personalRecordService.findOne(savedPersonalRecordId));

		//Borrar, la �nica forma de borrar un PersonalRecord es borrar su curriculum

		this.curriculumService.delete(savedCurriculum);
		Assert.isNull(this.personalRecordService.findOne(savedPersonalRecordId));
		this.unauthenticate();

	}
}
