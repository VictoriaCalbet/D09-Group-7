
package services.form;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import services.AnnouncementService;
import services.RendezvousService;
import domain.Announcement;
import domain.form.AnnouncementForm;

@Service
@Transactional
public class AnnouncementFormService {

	// Supporting services ----------------------------------------------------

	@Autowired
	private AnnouncementService	announcementService;

	@Autowired
	private RendezvousService	rendezvousService;


	// Constructors -----------------------------------------------------------

	public AnnouncementFormService() {
		super();
	}

	// Creación de formularios ------------------------------------------------

	// Utilizado al crear una nueva entidad de Announcement

	public AnnouncementForm createFromCreate() {
		AnnouncementForm result = null;

		result = new AnnouncementForm();

		return result;
	}

	// Utilizado al editar una nueva entidad de Announcement

	public AnnouncementForm createFromEdit(final int announcementId) {
		Announcement announcement = null;
		AnnouncementForm result = null;

		announcement = this.announcementService.findOne(announcementId);
		result = new AnnouncementForm();

		result.setId(announcement.getId());
		result.setTitle(announcement.getTitle());
		result.setDescription(announcement.getDescription());
		result.setRendezvousId(announcement.getRendezvous().getId());

		return result;
	}

	// Reconstrucción de objetos (Reconstruct) --------------------------------

	public Announcement saveFromCreate(final AnnouncementForm announcementForm) {
		Announcement announcement = null;
		Announcement result = null;

		Assert.notNull(announcementForm, "message.error.announcementForm.null");

		announcement = this.announcementService.create();

		// announcement.setId(announcementForm.getId());
		announcement.setTitle(announcementForm.getTitle());
		announcement.setDescription(announcementForm.getDescription());
		announcement.setRendezvous(this.rendezvousService.findOne(announcementForm.getRendezvousId()));

		result = this.announcementService.saveFromCreate(announcement);

		return result;
	}

	public Announcement saveFromEdit(final AnnouncementForm announcementForm) {
		Announcement announcement = null;
		Announcement result = null;

		Assert.notNull(announcementForm, "message.error.announcementForm.null");

		announcement = this.announcementService.findOne(announcementForm.getId());

		// announcement.setId(announcementForm.getId());
		announcement.setTitle(announcementForm.getTitle());
		announcement.setDescription(announcementForm.getDescription());

		result = this.announcementService.saveFromEdit(announcement);

		return result;
	}
}
