
package services.form;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import services.ServiceService;
import domain.Service;
import domain.form.ServiceForm;

@org.springframework.stereotype.Service
@Transactional
public class ServiceFormService {

	// Supporting services ----------------------------------------------------

	@Autowired
	private ServiceService	serviceService;


	// Constructors -----------------------------------------------------------

	public ServiceFormService() {
		super();
	}

	// Creación de formularios ------------------------------------------------

	// Utilizado al crear una nueva entidad de Service

	public ServiceForm createFromCreate() {
		ServiceForm result = null;

		result = new ServiceForm();

		return result;
	}

	// Utilizado al editar una nueva entidad de Service

	public ServiceForm createFromEdit(final int serviceId) {
		Service service = null;
		ServiceForm result = null;

		service = this.serviceService.findOne(serviceId);
		result = new ServiceForm();

		result.setId(service.getId());
		result.setName(service.getName());
		result.setDescription(service.getDescription());
		result.setPictureURL(service.getPictureURL());
		result.setCategories(service.getCategories());

		return result;
	}

	// Reconstrucción de objetos (Reconstruct) --------------------------------

	public Service saveFromCreate(final ServiceForm serviceForm) {
		Service service = null;
		Service result = null;

		Assert.notNull(serviceForm, "message.error.serviceForm.null");
		service = this.serviceService.create();

		//service.setId(serviceForm.getId());
		service.setName(serviceForm.getName());
		service.setDescription(serviceForm.getDescription());
		service.setPictureURL(serviceForm.getPictureURL());
		service.setCategories(serviceForm.getCategories());

		result = this.serviceService.saveFromCreate(service);

		return result;
	}

	public Service saveFromEdit(final ServiceForm serviceForm) {
		Service service = null;
		Service result = null;

		Assert.notNull(serviceForm, "message.error.serviceForm.null");
		service = this.serviceService.findOne(serviceForm.getId());

		//service.setId(serviceForm.getId());
		service.setName(serviceForm.getName());
		service.setDescription(serviceForm.getDescription());
		service.setPictureURL(serviceForm.getPictureURL());
		service.setCategories(serviceForm.getCategories());

		result = this.serviceService.saveFromEdit(service);

		return result;
	}
}
