
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Finder;
import domain.Trip;

@Repository
public interface FinderRepository extends JpaRepository<Finder, Integer> {

	@Query("select f from Finder f where f.explorer.id = ?1")
	Finder findByExplorerId(int finderId);

	@Query("select f.found from Finder f, SystemConfiguration sc where f.id = ?1 and ROWNUM <= sc.defaultFinderNumber and sc.id = ?2")
	Collection<Trip> getFound(int finder, int scId);

}
