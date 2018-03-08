
package services;

import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.LegalText;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class LegalTextServiceTest extends AbstractTest {

	// Service under test
	@Autowired
	private LegalTextService	legalTextService;


	//Supporting services

	// Tests
	@Test
	public void createLegalText() {
		super.authenticate("admin");
		final LegalText lt = this.legalTextService.create();

		Assert.isTrue(!(lt.getId() > 0));
		super.unauthenticate();

	}

	@Test
	public void saveLegalText() {
		super.authenticate("admin");
		final LegalText lt = this.legalTextService.create();

		Assert.isTrue(!(lt.getId() > 0));

		lt.setBody("Body of the legal text");
		lt.setIsDraft(false);
		lt.setNumberLaw("Laws");
		lt.setTitle("Legal text 8");

		final LegalText savedLegalT = this.legalTextService.save(lt);

		Assert.isTrue(savedLegalT.getId() > 0);
		super.unauthenticate();
	}

	@Test
	public void editLegalText() {

		super.authenticate("admin");
		final LegalText lt = this.legalTextService.create();

		Assert.isTrue(!(lt.getId() > 0));

		lt.setBody("Body of the legal text");
		lt.setIsDraft(true);
		lt.setNumberLaw("Laws");
		lt.setTitle("Legal text 8");

		final LegalText savedLegalT = this.legalTextService.save(lt);

		Assert.isTrue(savedLegalT.getId() > 0);

		savedLegalT.setBody("New body of the legal text");

		this.legalTextService.edit(savedLegalT);

		Assert.isTrue(!(savedLegalT.getBody().equals(lt.getBody())));

		super.unauthenticate();
	}

	@Test(expected = IllegalArgumentException.class)
	public void editLegalTextFail() {

		super.authenticate("admin");
		final LegalText lt = this.legalTextService.create();

		Assert.isTrue(!(lt.getId() > 0));

		lt.setBody("Body of the legal text");
		lt.setIsDraft(false);
		lt.setNumberLaw("Laws");
		lt.setTitle("Legal text 8");

		final LegalText savedLegalT = this.legalTextService.save(lt);

		Assert.isTrue(savedLegalT.getId() > 0);

		savedLegalT.setBody("New body of the legal text");

		this.legalTextService.edit(savedLegalT);

		Assert.isTrue(!(savedLegalT.getBody().equals(lt.getBody())));

		super.unauthenticate();
	}

	@Test
	public void delete() {
		super.authenticate("admin");
		final LegalText lt = this.legalTextService.create();

		Assert.isTrue(!(lt.getId() > 0));

		lt.setBody("Body of the legal text");
		lt.setIsDraft(true);
		lt.setNumberLaw("Laws");
		lt.setTitle("Legal text 8");

		final LegalText savedLegalT = this.legalTextService.save(lt);

		Assert.isTrue(savedLegalT.getId() > 0);

		this.legalTextService.delete(savedLegalT);

		Assert.isNull(this.legalTextService.findOne(savedLegalT));
		super.unauthenticate();
	}

	@Test
	public void getFinalLegalTexts() {
		this.authenticate("admin");
		final LegalText lt = this.legalTextService.create();

		Assert.isTrue(!(lt.getId() > 0));

		lt.setBody("Body of the legal text");
		lt.setIsDraft(false);
		lt.setNumberLaw("Laws");
		lt.setTitle("Legal text 8");

		final Collection<LegalText> lts = this.legalTextService.getFinalLegalTexts();

		Assert.notEmpty(lts);
		this.unauthenticate();
	}

	@Test
	public void getNumberOfReferences() {
		this.authenticate("admin");
		final LegalText lt = this.legalTextService.create();

		Assert.isTrue(!(lt.getId() > 0));

		lt.setBody("Body of the legal text");
		lt.setIsDraft(false);
		lt.setNumberLaw("Laws");
		lt.setTitle("Legal text 8");

		final LegalText savedLegalT = this.legalTextService.save(lt);

		Assert.notNull(this.legalTextService.getNumberOfReferences(savedLegalT));
		Assert.isTrue(this.legalTextService.getNumberOfReferences(savedLegalT) == 0);
		this.unauthenticate();
	}

}
