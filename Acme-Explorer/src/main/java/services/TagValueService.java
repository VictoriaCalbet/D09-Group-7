
package services;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.TagValueRepository;
import domain.Manager;
import domain.Tag;
import domain.TagValue;
import domain.Trip;

@Service
@Transactional
public class TagValueService {

	// Managed Repository
	@Autowired
	private TagValueRepository	tagValueRepository;

	// Supporting services
	@Autowired
	private ManagerService		managerService;
	@Autowired
	private TagService			tagService;
	@Autowired
	private TripService			tripService;


	// Constructor
	public TagValueService() {
		super();
	}

	// Simple CRUD Methods

	public TagValue create() {
		Manager principal;
		principal = this.managerService.findByPrincipal();
		Assert.notNull(principal, "message.error.tagValue.login");

		TagValue result;

		result = new TagValue();

		return result;
	}

	public TagValue createFromTrip(final int tripId) {
		Manager principal;
		principal = this.managerService.findByPrincipal();
		Assert.notNull(principal, "message.error.tagValue.login");

		final Trip trip = this.tripService.findOne(tripId);
		final Date now = new Date();

		Assert.notNull(trip, "message.error.tagValue.trip.null");
		Assert.isTrue(principal.getTrips().contains(trip), "message.error.tagValue.trip.manager");
		Assert.isTrue(trip.getPublicationDate().after(now), "message.error.tagValue.trip.notPublished");

		TagValue result;

		result = new TagValue();
		result.setTrip(trip);

		return result;
	}

	public TagValue createFromTag(final int tagId) {
		Manager principal;
		principal = this.managerService.findByPrincipal();
		Assert.notNull(principal, "message.error.tagValue.login");

		final Tag tag = this.tagService.findOne(tagId);
		Assert.notNull(tag);

		TagValue result;

		result = new TagValue();
		result.setName(tag.getName());
		result.setTag(tag);

		return result;
	}

	public TagValue save(final TagValue tagValue) {
		Assert.notNull(tagValue, "message.error.tagValue.null");

		TagValue result;

		result = this.tagValueRepository.save(tagValue);

		return result;
	}

	public TagValue saveFromCreate(final TagValue tagValue) {
		Manager principal;
		principal = this.managerService.findByPrincipal();
		Assert.notNull(principal, "message.error.tagValue.login");
		Assert.isTrue(principal.getTrips().contains(tagValue.getTrip()), "message.error.tagValue.trip.manager");
		Assert.isTrue(!this.checkRepeatedTag(tagValue), "message.error.tagValue.repeatedTag");
		Assert.isTrue(tagValue.getTag().getGroupOfValues().contains(tagValue.getValue()), "message.error.tagValue.value.tag");

		final Date now = new Date();

		Assert.isTrue(tagValue.getTrip().getPublicationDate().after(now), "message.error.tagValue.trip.notPublished");

		TagValue result;
		tagValue.setName(tagValue.getTag().getName());

		result = this.save(tagValue);

		// Add the tagValue to the Tag.
		final Tag tag = result.getTag();
		final Collection<TagValue> tagTagValues = tag.getTagValues();
		tagTagValues.add(result);
		tag.setTagValues(tagTagValues);
		this.tagService.save(tag);

		// Add the tagValue to the Trip.
		final Trip trip = result.getTrip();
		final Collection<TagValue> tripTagValues = trip.getTagValues();
		tripTagValues.add(result);
		trip.setTagValues(tripTagValues);
		this.tripService.saveByOtherActors(trip);

		return result;

	}

	public TagValue saveFromEdit(final TagValue tagValue) {
		Manager principal;
		principal = this.managerService.findByPrincipal();
		Assert.notNull(principal, "message.error.tagValue.login");
		Assert.isTrue(principal.getTrips().contains(tagValue.getTrip()), "message.error.tagValue.trip.manager");
		Assert.isTrue(tagValue.getTag().getGroupOfValues().contains(tagValue.getValue()), "message.error.tagValue.value.tag");

		final Date now = new Date();

		Assert.isTrue(tagValue.getTrip().getPublicationDate().after(now), "message.error.tagValue.trip.notPublished");

		TagValue result;

		result = this.save(tagValue);

		return result;

	}

	public void delete(final TagValue tagValue) {
		Manager principal;
		principal = this.managerService.findByPrincipal();
		Assert.notNull(principal, "message.error.tagValue.login");
		Assert.isTrue(principal.getTrips().contains(tagValue.getTrip()), "message.error.tagValue.trip.manager");

		final Trip trip = tagValue.getTrip();
		final Tag tag = tagValue.getTag();

		final Collection<TagValue> tripTagValues = trip.getTagValues();
		tripTagValues.remove(tagValue);
		trip.setTagValues(tripTagValues);
		this.tripService.saveByOtherActors(trip);

		if (tag != null) {
			Collection<TagValue> tagTagValues = tag.getTagValues();
			tagTagValues = tag.getTagValues();
			tagTagValues.remove(tagValue);
			tag.setTagValues(tagTagValues);
			this.tagService.save(tag);
		}

		this.tagValueRepository.delete(tagValue);

	}

	public Collection<TagValue> findAll() {
		Collection<TagValue> result;

		result = this.tagValueRepository.findAll();

		return result;
	}

	public TagValue findOne(final int tagValueId) {
		TagValue result;

		result = this.tagValueRepository.findOne(tagValueId);

		return result;
	}

	public Integer count() {
		Integer result;

		result = (int) this.tagValueRepository.count();

		return result;
	}

	public Collection<TagValue> findAllByTagId(final int tagId) {
		Collection<TagValue> result;

		result = this.tagValueRepository.findAllByTagId(tagId);

		return result;
	}

	// Other bussiness methods

	public Boolean checkRepeatedTag(final TagValue tagValue) {
		Boolean result = false;

		final Trip trip = tagValue.getTrip();

		for (final TagValue tv : trip.getTagValues())
			if (tv.getTag() != null && tv.getTag().equals(tagValue.getTag())) {
				result = true;
				break;
			}

		return result;
	}

}
