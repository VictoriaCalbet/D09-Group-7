
package services;

import java.util.Collection;
import java.util.HashSet;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Tag;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class TagServiceTest extends AbstractTest {

	// Service under test
	@Autowired
	private TagService	tagService;


	// Tests

	@Test
	public void testCreate1() {
		this.authenticate("admin");

		Tag tagToCreate;

		tagToCreate = this.tagService.create();

		Assert.isNull(tagToCreate.getName());
		Assert.isTrue(tagToCreate.getGroupOfValues().isEmpty());
		Assert.isTrue(tagToCreate.getTagValues().isEmpty());

		this.unauthenticate();
	}

	@Test
	public void testSaveFromCreate1() {
		this.authenticate("admin");

		Tag tagToCreate;

		tagToCreate = this.tagService.create();

		Assert.isNull(tagToCreate.getName());
		Assert.isTrue(tagToCreate.getGroupOfValues().isEmpty());
		Assert.isTrue(tagToCreate.getTagValues().isEmpty());

		this.unauthenticate();

		this.authenticate("admin");

		final Tag tagToSave;
		final Collection<String> values = new HashSet<>();

		tagToCreate.setName("Wrestling");
		values.add("WWE");
		values.add("NJPW");
		values.add("ROH");
		tagToCreate.setGroupOfValues(values);

		tagToSave = this.tagService.saveFromCreate(tagToCreate);

		Assert.isTrue(tagToSave.getId() > 0);
		Assert.isTrue(tagToSave.getName().equals(tagToCreate.getName()));
		Assert.isTrue(tagToSave.getGroupOfValues().size() == 3);
		Assert.isTrue(tagToSave.getTagValues().isEmpty());

		this.unauthenticate();
	}

	@Test
	public void testSaveFromEdit1() {
		this.authenticate("admin");

		Tag tagToCreate;

		tagToCreate = this.tagService.create();

		Assert.isNull(tagToCreate.getName());
		Assert.isTrue(tagToCreate.getGroupOfValues().isEmpty());
		Assert.isTrue(tagToCreate.getTagValues().isEmpty());

		this.unauthenticate();

		this.authenticate("admin");

		final Tag tagToSave;
		final Collection<String> values = new HashSet<>();

		tagToCreate.setName("Wrestling");
		values.add("WWE");
		values.add("NJPW");
		values.add("ROH");
		tagToCreate.setGroupOfValues(values);

		tagToSave = this.tagService.saveFromCreate(tagToCreate);

		Assert.isTrue(tagToSave.getId() > 0);
		Assert.isTrue(tagToSave.getName().equals(tagToCreate.getName()));
		Assert.isTrue(tagToSave.getGroupOfValues().size() == 3);
		Assert.isTrue(tagToSave.getTagValues().isEmpty());

		this.unauthenticate();

		this.authenticate("admin");

		Tag tagToEdit;

		tagToSave.setName("Lucha libre");
		values.add("TNA");
		tagToSave.setGroupOfValues(values);

		tagToEdit = this.tagService.saveFromEdit(tagToSave);

		Assert.isTrue(tagToEdit.getId() > 0);
		Assert.isTrue(tagToEdit.getName().equals(tagToSave.getName()));
		Assert.isTrue(tagToEdit.getGroupOfValues().size() == 4);
		Assert.isTrue(tagToEdit.getTagValues().isEmpty());

		this.unauthenticate();
	}

	@Test
	public void testSaveDelete1() {
		this.authenticate("admin");

		Tag tagToCreate;

		tagToCreate = this.tagService.create();

		Assert.isNull(tagToCreate.getName());
		Assert.isTrue(tagToCreate.getGroupOfValues().isEmpty());
		Assert.isTrue(tagToCreate.getTagValues().isEmpty());

		this.unauthenticate();

		this.authenticate("admin");

		final Tag tagToSave;
		final Collection<String> values = new HashSet<>();

		tagToCreate.setName("Wrestling");
		values.add("WWE");
		values.add("NJPW");
		values.add("ROH");
		tagToCreate.setGroupOfValues(values);

		tagToSave = this.tagService.saveFromCreate(tagToCreate);

		Assert.isTrue(tagToSave.getId() > 0);
		Assert.isTrue(tagToSave.getName().equals(tagToCreate.getName()));
		Assert.isTrue(tagToSave.getGroupOfValues().size() == 3);
		Assert.isTrue(tagToSave.getTagValues().isEmpty());

		this.unauthenticate();

		this.authenticate("admin");

		Tag tagToEdit;

		tagToSave.setName("Lucha libre");
		values.add("TNA");
		tagToSave.setGroupOfValues(values);

		tagToEdit = this.tagService.saveFromEdit(tagToSave);

		Assert.isTrue(tagToEdit.getId() > 0);
		Assert.isTrue(tagToEdit.getName().equals(tagToSave.getName()));
		Assert.isTrue(tagToEdit.getGroupOfValues().size() == 4);
		Assert.isTrue(tagToEdit.getTagValues().isEmpty());

		this.unauthenticate();

		this.authenticate("admin");

		Tag tagToDelete;

		this.tagService.delete(tagToEdit);

		tagToDelete = this.tagService.findOne(tagToEdit.getId());

		Assert.isNull(tagToDelete);

		this.unauthenticate();
	}

}
