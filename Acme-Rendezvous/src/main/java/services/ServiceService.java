
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.ServiceRepository;

@Service
@Transactional
public class ServiceService {

	// Managed Repository -----------------------------------------------------

	@Autowired
	private ServiceRepository	serviceRepository;


	// Supporting services ----------------------------------------------------

	// Constructors -----------------------------------------------------------

	public ServiceService() {
		super();
	}

	// Simple CRUD methods ----------------------------------------------------

	public domain.Service create() {
		return null;
	}

	public domain.Service save(final domain.Service service) {
		Assert.notNull(service);
		domain.Service result;

		result = this.serviceRepository.save(service);

		return result;

	}

	public domain.Service saveFromCreate(final domain.Service service) {
		return null;
	}

	public domain.Service saveFromEdit(final domain.Service service) {
		return null;
	}

	// Other business methods -------------------------------------------------

	public Collection<domain.Service> findAll() {
		Collection<domain.Service> result = null;
		result = this.serviceRepository.findAll();
		return result;
	}

	public domain.Service findOne(final int serviceId) {
		domain.Service result = null;
		result = this.serviceRepository.findOne(serviceId);
		return result;
	}
}
