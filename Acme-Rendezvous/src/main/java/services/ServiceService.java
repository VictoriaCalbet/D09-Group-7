
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

		result.setManager(this.managerService.findByPrincipal());
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
		Assert.isTrue(service.getManager().equals(manager), "message.error.service.userNotPrincipal");
		Assert.isTrue(service.getIsInappropriate() == false, "message.error.service.cancelled.true");
		Assert.isTrue(service.getRequests().isEmpty(), "message.error.service.requests.notEmpty");

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
		Assert.isTrue(service.getManager().equals(manager), "message.error.service.userNotPrincipal");
		Assert.isTrue(service.getIsInappropriate() == false, "message.error.service.cancelled.true");
		Assert.isTrue(service.getRequests().isEmpty(), "message.error.service.requests.notEmpty");

		// Persisto la entidad del servicio service

		result = this.save(service);

		return result;
	}

	public void flush() {
		this.serviceRepository.flush();
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

	public Service markingServiceAsAppropriateOrNot(final Service service, final boolean changeTo) {
		Service result = null;
		Administrator administrator = null;

		administrator = this.administratorService.findByPrincipal();
		Assert.notNull(service, "message.error.service.null");
		Assert.notNull(administrator, "message.error.service.principal.null");
		Assert.isTrue(!service.getIsInappropriate() == changeTo, "message.error.markEquals");

		service.setIsInappropriate(changeTo);

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

	public Collection<Service> findAvailableServicesByUserId(final int userId) {
		return this.serviceRepository.findAvailableServicesByUserId(userId);
	}

	public Collection<Service> findServicesByUserId(final int userId) {
		return this.serviceRepository.findServicesByUserId(userId);
	}

	public Collection<Service> findServicesByRendezvousId(final int rendezvousId) {
		return this.serviceRepository.findServicesByRendezvousId(rendezvousId);
	}

	public Collection<Service> findAvailableServicesToRequest(final int rendezvousId) {
		return this.serviceRepository.findAvailableServicesToRequest(rendezvousId);
	}

	public Collection<Service> findAvailableServices() {
		return this.serviceRepository.findAvailableServices();
	}

	// Dashboard services ------------------------------------------------------

	// Acme-Rendezvous 2.0 - Requisito 6.2.1
	public Collection<Service> findBestSellingServices() {
		return this.serviceRepository.findBestSellingServices();
	}

	// Acme-Rendezvous 2.0 - Requisito 11.2.3
	public Double findAvgServicesRequestedPerRendezvous() {
		return this.serviceRepository.findAvgServicesRequestedPerRendezvous();
	}

	public Double findMinServicesRequestedPerRendezvous() {
		return this.serviceRepository.findMinServicesRequestedPerRendezvous();
	}

	public Double findMaxServicesRequestedPerRendezvous() {
		return this.serviceRepository.findMaxServicesRequestedPerRendezvous();
	}

	public Double findStdServicesRequestedPerRendezvous() {
		return this.serviceRepository.findStdServicesRequestedPerRendezvous();
	}

	// Acme-Rendezvous 2.0 - Requisito 11.2.4
	public Collection<Service> findTopSellingServices() {
		Collection<Service> result = null;
		result = this.serviceRepository.findTopSellingServices();

		if (result != null && result.size() > 7)
			result = new ArrayList<Service>(result).subList(0, 7);

		return result;
	}
}
