
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.SystemConfigurationRepository;
import domain.Administrator;
import domain.SystemConfiguration;

@Service
@Transactional
public class SystemConfigurationService {

	// Managed Repository -----------------------------------------------------

	@Autowired
	private SystemConfigurationRepository	systemConfigurationRepository;

	// Supporting services ----------------------------------------------------
	@Autowired
	private AdministratorService			administratorService;


	// Constructors -----------------------------------------------------------

	public SystemConfigurationService() {
		super();
	}

	// Simple CRUD methods ----------------------------------------------------

	public SystemConfiguration create() {
		SystemConfiguration result;

		result = new SystemConfiguration();

		return result;
	}

	public SystemConfiguration save(final SystemConfiguration systemConfiguration) {
		Assert.notNull(systemConfiguration, "message.error.systemConfiguration.null");
		SystemConfiguration result;

		result = this.systemConfigurationRepository.save(systemConfiguration);

		return result;

	}

	public SystemConfiguration saveFromCreate(final SystemConfiguration systemConfiguration) {
		Assert.notNull(systemConfiguration, "message.error.systemConfiguration.null");

		final Administrator administrator = this.administratorService.findByPrincipal();
		Assert.notNull(administrator, "message.error.systemConfiguration.authority");

		SystemConfiguration result;

		result = this.save(systemConfiguration);

		return result;
	}
	public SystemConfiguration saveFromEdit(final SystemConfiguration systemConfiguration) {
		Assert.notNull(systemConfiguration, "message.error.systemConfiguration.null");

		final Administrator administrator = this.administratorService.findByPrincipal();
		Assert.notNull(administrator, "message.error.systemConfiguration.authority");

		SystemConfiguration result;

		result = this.save(systemConfiguration);

		return result;
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

	public SystemConfiguration findMain() {
		SystemConfiguration result;

		result = this.findAll().iterator().next();

		return result;
	}
}
