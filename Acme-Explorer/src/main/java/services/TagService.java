
package services;

import java.util.Collection;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.TagRepository;
import domain.Actor;
import domain.Tag;
import domain.TagValue;

@Service
@Transactional
public class TagService {

	// Managed Repository
	@Autowired
	private TagRepository	tagRepository;

	// Supporting Services
	@Autowired
	private ActorService	actorService;
	@Autowired
	private TagValueService	tagValueService;


	// Constructor

	public TagService() {
		super();
	}

	// Simple CRUD methods

	public Tag create() {
		Actor principal;
		principal = this.actorService.findByPrincipal();
		Assert.notNull(principal, "message.error.tag.login");
		Assert.isTrue(this.actorService.checkActorWithAuthority(principal, "ADMIN"), "message.error.tag.admin");

		Tag result;
		final Collection<TagValue> tagValues = new HashSet<>();
		final Collection<String> values = new HashSet<>();

		result = new Tag();
		result.setGroupOfValues(values);
		result.setTagValues(tagValues);

		return result;
	}

	public Tag save(final Tag tag) {
		Assert.notNull(tag, "message.error.tag.null");

		Tag result;

		result = this.tagRepository.save(tag);

		return result;
	}

	public Tag saveFromCreate(final Tag tag) {
		Assert.notNull(tag, "message.error.tag.null");
		Assert.notNull(tag.getName(), "message.error.tag.null.name");
		Assert.notNull(tag.getGroupOfValues(), "message.error.tag.null.values");

		Actor principal;
		principal = this.actorService.findByPrincipal();
		Assert.notNull(principal, "message.error.tag.login");
		Assert.isTrue(this.actorService.checkActorWithAuthority(principal, "ADMIN"), "message.error.tag.admin");
		Assert.isTrue(!this.checkTagNameRepeated(tag), "message.error.tag.name.repeated");

		Tag result;

		result = this.save(tag);

		return result;
	}

	public Tag saveFromEdit(final Tag tag) {
		Assert.notNull(tag, "message.error.tag.null");
		Assert.notNull(tag.getName(), "message.error.tag.null.name");
		Assert.isTrue(tag.getTagValues().isEmpty(), "message.error.tag.null.values.empty"); // The tag is been used by a trip.

		Actor principal;
		principal = this.actorService.findByPrincipal();
		Assert.notNull(principal, "message.error.tag.login");
		Assert.isTrue(this.actorService.checkActorWithAuthority(principal, "ADMIN"), "message.error.tag.admin");
		Assert.isTrue(!this.checkTagNameRepeated(tag), "message.error.tag.name.repeated");

		Tag result;

		result = this.save(tag);

		return result;

	}

	public void delete(final Tag tag) {
		Assert.notNull(tag, "message.error.tag.null");

		Actor principal;
		principal = this.actorService.findByPrincipal();
		Assert.notNull(principal, "message.error.tag.login");
		Assert.isTrue(this.actorService.checkActorWithAuthority(principal, "ADMIN"), "message.error.tag.admin");

		// Remove reference from all tagValues assigned.
		final Collection<TagValue> tagValues = tag.getTagValues();

		for (final TagValue tv : tagValues) {
			tv.setTag(null);
			this.tagValueService.save(tv);
		}

		this.tagRepository.delete(tag);
	}

	public Collection<Tag> findAll() {
		final Collection<Tag> tags;

		tags = this.tagRepository.findAll();

		return tags;
	}

	public Tag findOne(final Integer id) {
		final Tag tag;

		tag = this.tagRepository.findOne(id);

		return tag;
	}

	public Integer count() {
		Integer result;

		result = (int) this.tagRepository.count();

		return result;
	}

	// Other business methods

	public boolean checkTagNameRepeated(final Tag tag) {
		Assert.notNull(tag, "message.error.tag.null");

		boolean result = false;
		Collection<Tag> allTags;

		allTags = this.findAll();

		for (final Tag t : allTags)
			if (t.getId() != tag.getId() && t.getName().equals(tag.getName())) {
				result = true;
				break;
			}

		return result;
	}
}
