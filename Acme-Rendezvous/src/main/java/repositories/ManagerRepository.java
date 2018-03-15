
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Manager;

@Repository
public interface ManagerRepository extends JpaRepository<Manager, Integer> {

	@Query("select m from Manager m where m.userAccount.id = ?1")
	Manager findByUserAccountId(int userAccountId);

	//Managers who provide more services than average
	//C-14.6.10 The listing of trips that have got at least 10% more applications than the average, ordered by number of applications

	@Query("select m from Manager m where m.services.size > (select avg(m.services.size) from Manager m)")
	Collection<Manager> findManagersWithMoreServicesThanAverage();

	@Query("select s.manager from Service s where s.isInappropriate=true")
	Collection<Manager> findManagersWithMoreServicesCancelled();
}
