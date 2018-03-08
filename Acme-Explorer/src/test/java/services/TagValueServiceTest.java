
package services;

import java.util.Collection;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Manager;
import domain.Tag;
import domain.TagValue;
import domain.Trip;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class TagValueServiceTest extends AbstractTest {

	// Service under test
	@Autowired
	private TagValueService	tagValueService;

	// Supporting Services
	@Autowired
	private ManagerService	managerService;
	@Autowired
	private TagService		tagService;
	@Autowired
	private TripService		tripService;


	// Tests

	@Test
	public void testCreate1() {
		this.authenticate("manager1");

		final TagValue tagValueToCreate;

		tagValueToCreate = this.tagValueService.create();

		Assert.isNull(tagValueToCreate.getName());
		Assert.isNull(tagValueToCreate.getValue());
		Assert.isNull(tagValueToCreate.getTag());
		Assert.isNull(tagValueToCreate.getTrip());

		this.unauthenticate();
	}

	@Test
	public void testSaveFromCreate1() {
		this.authenticate("manager3");

		final TagValue tagValueToCreate;

		tagValueToCreate = this.tagValueService.create();

		Assert.isNull(tagValueToCreate.getName());
		Assert.isNull(tagValueToCreate.getValue());
		Assert.isNull(tagValueToCreate.getTag());
		Assert.isNull(tagValueToCreate.getTrip());

		this.unauthenticate();

		this.authenticate("manager3");

		Manager principal;
		principal = this.managerService.findByPrincipal();

		final Collection<Trip> trips = principal.getTrips();
		Trip trip = null;

		for (final Trip t : trips)
			if (t.getPublicationDate().compareTo(new Date()) > 0) {
				trip = t; // Editable trip.
				break;
			}

		final Tag tag = this.tagService.findAll().iterator().next();

		tagValueToCreate.setValue("Tanzania");
		tagValueToCreate.setTrip(trip);
		tagValueToCreate.setTag(tag);

		TagValue tagValueToSave;
		tagValueToSave = this.tagValueService.saveFromCreate(tagValueToCreate);

		Assert.isTrue(tagValueToSave.getId() > 0);
		Assert.isTrue(tagValueToSave.getName().equals(tagValueToSave.getTag().getName()));
		Assert.isTrue(tagValueToSave.getValue().equals(tagValueToCreate.getValue()));
		Assert.isTrue(tagValueToSave.getTag().equals(tagValueToCreate.getTag()));
		Assert.isTrue(tagValueToSave.getTrip().equals(tagValueToCreate.getTrip()));

		final Trip tripPostSave = this.tripService.findOne(tagValueToSave.getTrip().getId());
		final Tag tagPostSave = this.tagService.findOne(tagValueToSave.getTag().getId());

		Assert.isTrue(tripPostSave.getTagValues().contains(tagValueToSave));
		Assert.isTrue(tagPostSave.getTagValues().contains(tagValueToSave));

		this.unauthenticate();
	}

	@Test
	public void testSaveFromEdit1() {
		this.authenticate("manager3");

		final TagValue tagValueToCreate;

		tagValueToCreate = this.tagValueService.create();

		Assert.isNull(tagValueToCreate.getName());
		Assert.isNull(tagValueToCreate.getValue());
		Assert.isNull(tagValueToCreate.getTag());
		Assert.isNull(tagValueToCreate.getTrip());

		this.unauthenticate();

		this.authenticate("manager3");

		Manager principal;
		principal = this.managerService.findByPrincipal();

		final Collection<Trip> trips = principal.getTrips();
		Trip trip = null;

		for (final Trip t : trips)
			if (t.getPublicationDate().compareTo(new Date()) > 0) {
				trip = t; // Editable trip.
				break;
			}

		final Tag tag = this.tagService.findAll().iterator().next();

		tagValueToCreate.setValue("Tanzania");
		tagValueToCreate.setTrip(trip);
		tagValueToCreate.setTag(tag);

		TagValue tagValueToSave;
		tagValueToSave = this.tagValueService.saveFromCreate(tagValueToCreate);

		Assert.isTrue(tagValueToSave.getId() > 0);
		Assert.isTrue(tagValueToSave.getName().equals(tagValueToSave.getTag().getName()));
		Assert.isTrue(tagValueToSave.getValue().equals(tagValueToCreate.getValue()));
		Assert.isTrue(tagValueToSave.getTag().equals(tagValueToCreate.getTag()));
		Assert.isTrue(tagValueToSave.getTrip().equals(tagValueToCreate.getTrip()));

		final Trip tripPostSave = this.tripService.findOne(tagValueToSave.getTrip().getId());
		final Tag tagPostSave = this.tagService.findOne(tagValueToSave.getTag().getId());

		Assert.isTrue(tripPostSave.getTagValues().contains(tagValueToSave));
		Assert.isTrue(tagPostSave.getTagValues().contains(tagValueToSave));

		this.unauthenticate();

		this.authenticate("manager3");

		tagValueToSave.setValue("Australia");

		final TagValue tagValueToEdit;

		tagValueToEdit = this.tagValueService.saveFromEdit(tagValueToSave);

		Assert.isTrue(tagValueToEdit.getValue().equals(tagValueToSave.getValue()));

		this.unauthenticate();

	}

	@Test
	public void testDelete1() {
		this.authenticate("manager3");

		final TagValue tagValueToCreate;

		tagValueToCreate = this.tagValueService.create();

		Assert.isNull(tagValueToCreate.getName());
		Assert.isNull(tagValueToCreate.getValue());
		Assert.isNull(tagValueToCreate.getTag());
		Assert.isNull(tagValueToCreate.getTrip());

		this.unauthenticate();

		this.authenticate("manager3");

		Manager principal;
		principal = this.managerService.findByPrincipal();

		final Collection<Trip> trips = principal.getTrips();
		Trip trip = null;

		for (final Trip t : trips)
			if (t.getPublicationDate().compareTo(new Date()) > 0) {
				trip = t; // Editable trip.
				break;
			}

		final Tag tag = this.tagService.findAll().iterator().next();

		tagValueToCreate.setValue("Tanzania");
		tagValueToCreate.setTrip(trip);
		tagValueToCreate.setTag(tag);

		TagValue tagValueToSave;
		tagValueToSave = this.tagValueService.saveFromCreate(tagValueToCreate);

		Assert.isTrue(tagValueToSave.getId() > 0);
		Assert.isTrue(tagValueToSave.getName().equals(tagValueToSave.getTag().getName()));
		Assert.isTrue(tagValueToSave.getValue().equals(tagValueToCreate.getValue()));
		Assert.isTrue(tagValueToSave.getTag().equals(tagValueToCreate.getTag()));
		Assert.isTrue(tagValueToSave.getTrip().equals(tagValueToCreate.getTrip()));

		final Trip tripPostSave = this.tripService.findOne(tagValueToSave.getTrip().getId());
		final Tag tagPostSave = this.tagService.findOne(tagValueToSave.getTag().getId());

		Assert.isTrue(tripPostSave.getTagValues().contains(tagValueToSave));
		Assert.isTrue(tagPostSave.getTagValues().contains(tagValueToSave));

		this.unauthenticate();

		this.authenticate("manager3");

		tagValueToSave.setValue("Australia");

		final TagValue tagValueToEdit;

		tagValueToEdit = this.tagValueService.saveFromEdit(tagValueToSave);

		Assert.isTrue(tagValueToEdit.getValue().equals(tagValueToSave.getValue()));

		this.unauthenticate();

		this.authenticate("manager3");

		this.tagValueService.delete(tagValueToEdit);

		Trip tripPostDelete;
		Tag tagPostDelete;

		tripPostDelete = this.tripService.findOne(tripPostSave.getId());
		tagPostDelete = this.tagService.findOne(tagPostSave.getId());

		Assert.isTrue(!tripPostDelete.getTagValues().contains(tagValueToEdit));
		Assert.isTrue(!tagPostDelete.getTagValues().contains(tagValueToEdit));

		this.unauthenticate();

	}
}
