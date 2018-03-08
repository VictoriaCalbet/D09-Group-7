
package services;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.AnnouncementRepository;
import domain.Administrator;
import domain.Announcement;
import domain.Rendezvous;
import domain.User;

@Service
@Transactional
public class AnnouncementService {

	// Managed Repository -----------------------------------------------------

	@Autowired
	private AnnouncementRepository	announcementRepository;

	// Supporting services ----------------------------------------------------

	@Autowired
	private UserService				userService;

	@Autowired
	private RendezvousService		rendezvousService;

	@Autowired
	private AdministratorService	administratorService;


	// Constructors -----------------------------------------------------------

	public AnnouncementService() {
		super();
	}

	// Simple CRUD methods ----------------------------------------------------

	public Announcement create() {
		Announcement result = null;
		result = new Announcement();

		result.setMomentMade(new Date());

		return result;
	}

	public Collection<Announcement> findAll() {
		Collection<Announcement> result = null;
		result = this.announcementRepository.findAll();
		return result;
	}

	public Announcement findOne(final int announcementId) {
		Announcement result = null;
		result = this.announcementRepository.findOne(announcementId);
		return result;
	}

	// saveFromCreate, saveFromEdit

	public Announcement save(final Announcement announcement) {
		Announcement result = null;

		Assert.notNull(announcement, "message.error.announcement.null");

		result = this.announcementRepository.save(announcement);

		return result;
	}

	public Announcement saveFromCreate(final Announcement announcement) {
		Announcement result = null;
		User user = null;
		Rendezvous rendezvous = null;

		user = this.userService.findByPrincipal();

		Assert.notNull(announcement, "message.error.announcement.null");
		Assert.notNull(user, "message.error.announcement.principal.null");
		Assert.isTrue(user.getId() == announcement.getRendezvous().getCreator().getId(), "message.error.announcement.rendezvous.userNotPrincipal");
		Assert.isTrue(announcement.getRendezvous().getMeetingMoment().after(new Date()), "message.error.announcement.rendezvous.meetingMomment.isPast");
		Assert.isTrue(!announcement.getRendezvous().getIsDraft(), "message.error.announcement.rendezvous.isDraft.isTrue");
		Assert.isTrue(!announcement.getRendezvous().getIsDeleted(), "message.error.announcement.rendezvous.isDeleted.isTrue");

		announcement.setMomentMade(new Date(System.currentTimeMillis() - 1000));

		// Paso 1: realizo la entidad del servicio Announcement

		result = this.save(announcement);

		// Paso 2: persisto el resto de relaciones a las que el objeto Announcement está relacionada.

		rendezvous = result.getRendezvous();
		rendezvous.getAnnouncements().add(result);
		this.rendezvousService.save(rendezvous);

		//result.getRendezvous().getAnnouncements().add(result);

		return result;
	}

	public Announcement saveFromEdit(final Announcement announcement) {
		Announcement result = null;
		Announcement announcementFromDB = null;
		User user = null;

		user = this.userService.findByPrincipal();
		announcementFromDB = this.findOne(announcement.getId());

		Assert.notNull(announcement, "message.error.announcement.null");
		Assert.notNull(user, "message.error.announcement.principal.null");
		Assert.isTrue(announcement.getRendezvous().getId() == announcementFromDB.getRendezvous().getId(), "message.error.announcement.rendezvous.isModified");
		Assert.isTrue(user.getId() == announcement.getRendezvous().getCreator().getId(), "message.error.announcement.rendezvous.userNotPrincipal");
		Assert.isTrue(announcement.getRendezvous().getMeetingMoment().after(new Date()), "message.error.announcement.rendezvous.meetingMomment.isPast");
		Assert.isTrue(!announcement.getRendezvous().getIsDraft(), "message.error.announcement.rendezvous.isDraft.isTrue");
		Assert.isTrue(!announcement.getRendezvous().getIsDeleted(), "message.error.announcement.rendezvous.isDeleted.isTrue");

		// Paso 1: realizo la entidad del servicio Announcement

		result = this.save(announcement);

		// Revisión futura: posibilidad de modificar un rendezvous de un Announcement

		return result;
	}

	public void delete(final Announcement announcement) {
		Administrator administrator = null;
		Rendezvous rendezvous = null;

		administrator = this.administratorService.findByPrincipal();

		Assert.notNull(announcement, "message.error.announcement.null");
		Assert.notNull(administrator, "message.error.announcement.principal.null");

		// Paso 1: actualizamos el resto de relaciones con la entidad Announcement

		rendezvous = announcement.getRendezvous();
		rendezvous.getAnnouncements().remove(announcement);
		this.rendezvousService.save(rendezvous);

		// Paso 2: borramos el objeto

		this.announcementRepository.delete(announcement);

	}

	// Other business methods -------------------------------------------------

	public Collection<Announcement> getAnnouncementsCreatedByUser(final int userId) {
		Collection<Announcement> announcements = null;
		announcements = this.announcementRepository.getAnnouncementsCreatedByUser(userId);
		return announcements;
	}

	public Collection<Announcement> getAnnouncementsPostedAndAcceptedByUser(final int userId) {
		Collection<Announcement> announcements = null;
		announcements = this.announcementRepository.getAnnouncementsPostedAndAcceptedByUser(userId);
		return announcements;
	}

	// Dashboard methods ------------------------------------------------------

	public Double findAvgAnnouncementPerRendezvous() {
		Double result = null;
		result = this.announcementRepository.findAvgAnnouncementPerRendezvous();
		return result;
	}

	public Double findStdAnnouncementPerRendezvous() {
		Double result = null;
		result = this.announcementRepository.findStdAnnouncementPerRendezvous();
		return result;
	}
}
