
package services.form;

import java.util.ArrayList;
import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import services.CategoryService;
import services.ServiceService;
import domain.Category;
import domain.Service;
import domain.form.ServiceForm;

@org.springframework.stereotype.Service
@Transactional
public class ServiceFormService {

	// Supporting services ----------------------------------------------------

	@Autowired
	private ServiceService	serviceService;

	@Autowired
	private CategoryService	categoryService;


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
		result.setIsInappropriate(service.getIsInappropriate());
		result.setNoRequests(service.getRequests().size());

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

		// Añadido: al enviar una colección vacía en la vista, no envía una 
		// lista vacía, sino null. Por ello, hacemos esta comprobación. 
		if (serviceForm.getCategories() == null)
			service.setCategories(new ArrayList<Category>());
		else
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

		// Añadido: al enviar una colección vacía en la vista, no envía una 
		// lista vacía, sino null. Por ello, hacemos esta comprobación. 
		if (serviceForm.getCategories() == null) {
			final Service serviceInDB = this.serviceService.findOne(service.getId());
			final Collection<Category> categories = serviceInDB.getCategories();
			for (final Category category : categories) {
				category.getServices().remove(serviceInDB);
				this.categoryService.save(category);
			}
			service.setCategories(new ArrayList<Category>());
		} else
			service.setCategories(serviceForm.getCategories());

		result = this.serviceService.saveFromEdit(service);

		return result;
	}
}
