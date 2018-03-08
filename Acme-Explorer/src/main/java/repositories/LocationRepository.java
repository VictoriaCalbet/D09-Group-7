
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Location;

@Repository
public interface LocationRepository extends JpaRepository<Location, Integer> {

	@Query("select l from Location l where l.id=?1")
	public Location findLocationById(int id);

}
