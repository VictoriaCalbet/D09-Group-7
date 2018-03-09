
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.SystemConfigurationRepository;
import domain.SystemConfiguration;

@Service
@Transactional
public class SystemConfigurationService {

	// Managed Repository -----------------------------------------------------

	@Autowired
	private SystemConfigurationRepository	systemConfigurationRepository;


	// Supporting services ----------------------------------------------------

	// Constructors -----------------------------------------------------------

	public SystemConfigurationService() {
		super();
	}

	// Simple CRUD methods ----------------------------------------------------

	public SystemConfiguration create() {
		return null;
	}

	public SystemConfiguration save(final SystemConfiguration systemConfiguration) {
		Assert.notNull(systemConfiguration);
		SystemConfiguration result;

		result = this.systemConfigurationRepository.save(systemConfiguration);

		return result;

	}

	public SystemConfiguration saveFromCreate(final SystemConfiguration systemConfiguration) {
		return null;
	}

	public SystemConfiguration saveFromEdit(final SystemConfiguration systemConfiguration) {
		return null;
	}

	// Other business methods -------------------------------------------------

	public Collection<SystemConfiguration> findAll() {
		Collection<SystemConfiguration> result = null;
		result = this.systemConfigurationRepository.findAll();
		return result;
	}

	public SystemConfiguration findOne(final int systemConfigurationId) {
		SystemConfiguration result = null;
		result = this.systemConfigurationRepository.findOne(systemConfigurationId);
		return result;
	}
}
