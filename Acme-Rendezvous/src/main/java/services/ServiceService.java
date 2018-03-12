
package services;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.ServiceRepository;
import domain.Administrator;
import domain.Category;
import domain.Manager;
import domain.Request;
import domain.Service;

@org.springframework.stereotype.Service
@Transactional
public class ServiceService {

	// Managed Repository -----------------------------------------------------

	@Autowired
	private ServiceRepository		serviceRepository;

	// Supporting services ----------------------------------------------------

	@Autowired
	private AdministratorService	administratorService;

	@Autowired
	private ManagerService			managerService;


	// Constructors -----------------------------------------------------------

	public ServiceService() {
		super();
	}

	// Simple CRUD methods ----------------------------------------------------

	public Service create() {
		Service result = null;

		result = new Service();

		result.setCategories(new ArrayList<Category>());
		result.setRequests(new ArrayList<Request>());
		result.setIsInappropriate(false);

		return result;
	}

	public Collection<Service> findAll() {
		Collection<Service> result = null;
		result = this.serviceRepository.findAll();
		return result;
	}

	public Service findOne(final int serviceId) {
		Service result = null;
		result = this.serviceRepository.findOne(serviceId);
		return result;
	}

	public Service save(final Service service) {
		Service result = null;

		Assert.notNull(service, "message.error.service.null");
		result = this.serviceRepository.save(service);

		return result;
	}

	public Service saveFromCreate(final Service service) {
		Service result = null;
		Manager manager = null;

		manager = this.managerService.findByPrincipal();

		Assert.notNull(service, "message.error.service.null");
		Assert.notNull(manager, "message.error.service.principal.null");
		Assert.notNull(service.getManager().equals(manager), "message.error.service.userNotPrincipal");

		// Paso 1: realizo la entidad del servicio Service

		result = this.save(service);

		// Paso 2: persisto el resto de relaciones a las que el objeto Service está relacionada

		manager.getServices().add(result);
		this.saveServiceInCategoriesCollection(result);

		return result;
	}

	public Service saveFromEdit(final Service service) {
		Service result = null;
		Manager manager = null;

		manager = this.managerService.findByPrincipal();

		Assert.notNull(service, "message.error.service.null");
		Assert.notNull(manager, "message.error.service.principal.null");
		Assert.notNull(service.getManager().equals(manager), "message.error.service.userNotPrincipal");
		Assert.isTrue(service.getIsInappropriate(), "message.error.service.cancelled.true");

		// Persisto la entidad del servicio service

		result = this.save(service);

		return result;
	}

	public void delete(final Service service) {
		Manager manager = null;

		manager = this.managerService.findByPrincipal();

		Assert.notNull(service, "message.error.service.null");
		Assert.notNull(manager, "message.error.service.principal.null");
		Assert.isTrue(service.getRequests().isEmpty(), "message.error.service.requests.notEmpty");
		Assert.notNull(service.getManager().equals(manager), "message.error.service.userNotPrincipal");

		// Paso 1: actualizamos el resto de relaciones con la entidad Announcement

		service.getManager().getServices().remove(service);
		this.deleteServiceInCategoriesCollection(service);
		this.deleteServiceInRequestsCollection(service);

		// Paso 2: borramos el objeto

		this.serviceRepository.delete(service);
	}

	// Other business methods -------------------------------------------------

	public Service markAsInappropriate(final Service service) {
		Service result = null;
		Administrator administrator = null;
		Boolean isInappropriate = null;

		administrator = this.administratorService.findByPrincipal();
		Assert.notNull(service, "message.error.service.null");
		Assert.notNull(administrator, "message.error.service.principal.null");

		isInappropriate = true;
		service.setIsInappropriate(isInappropriate);

		result = this.serviceRepository.save(service);

		return result;
	}

	public void saveServiceInCategoriesCollection(final Service service) {
		Collection<Category> categories = null;

		categories = service.getCategories();

		for (final Category category : categories)
			category.getServices().add(service);
	}

	public void deleteServiceInCategoriesCollection(final Service service) {
		Collection<Category> categories = null;

		categories = service.getCategories();

		for (final Category category : categories)
			category.getServices().remove(service);
	}

	public void deleteServiceInRequestsCollection(final Service service) {
		Collection<Request> requests = null;

		requests = service.getRequests();

		for (final Request request : requests)
			request.setService(null);
	}
}
