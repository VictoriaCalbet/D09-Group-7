
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.RequestRepository;
import domain.Request;

@Service
@Transactional
public class RequestService {

	// Managed Repository -----------------------------------------------------

	@Autowired
	private RequestRepository	requestRepository;


	// Supporting services ----------------------------------------------------

	// Constructors -----------------------------------------------------------

	public RequestService() {
		super();
	}

	// Simple CRUD methods ----------------------------------------------------

	public Request create() {
		return null;
	}

	public Request save(final Request request) {
		Assert.notNull(request);
		Request result;

		result = this.requestRepository.save(request);

		return result;

	}

	public Request saveFromCreate(final Request request) {
		return null;
	}

	public Request saveFromEdit(final Request request) {
		return null;
	}

	// Other business methods -------------------------------------------------

	public Collection<Request> findAll() {
		Collection<Request> result = null;
		result = this.requestRepository.findAll();
		return result;
	}

	public Request findOne(final int requestId) {
		Request result = null;
		result = this.requestRepository.findOne(requestId);
		return result;
	}
}
