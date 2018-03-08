
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.LocationRepository;
import domain.Location;

@Service
@Transactional
public class LocationService {

	// Managed Repository
	@Autowired
	private LocationRepository	locationRepository;


	// Supporting Services

	// Constructor

	public LocationService() {
		super();
	}

	// Simple CRUD methods
	//Create a Location
	public Location create() {
		Location result;
		result = new Location();
		return result;

	}
	//Find one Location
	public Location findOne(final Location l) {

		return this.locationRepository.findOne(l.getId());
	}
	public Location findLocationById(final int id) {
		return this.locationRepository.findLocationById(id);
	}
	//List all Locations

	public Collection<Location> listAll() {
		return this.locationRepository.findAll();
	}
	//Save a Location
	public void save(final Location l) {

		Assert.notNull(l);
		this.locationRepository.save(l);

	}

	//Delete a Location
	public void delete(final Location l) {

		this.locationRepository.delete(l);
	}

	// Other business methods

}
