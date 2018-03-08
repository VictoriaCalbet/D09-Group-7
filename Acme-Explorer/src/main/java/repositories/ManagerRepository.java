
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Manager;

@Repository
public interface ManagerRepository extends JpaRepository<Manager, Integer> {

	//Find a Manager by a user account id
	@Query("select m from Manager m where m.userAccount.id = ?1")
	public Manager findByUserAccountId(int id);

	//Find a manager by an application id
	@Query("select t.manager from Trip t join t.applications a where a.id = ?1")
	Manager findOneByApplicationId(int applicationId);

	//Find all suspicious Managers
	@Query("select m from Manager m where m.isSuspicious is true")
	Collection<Manager> findAllSuspicious();

	//C-14.6.2.1 The average number of trips managed per manager
	@Query("select avg(m.trips.size) from Manager m")
	Double findAverageTripsPerManager();
	//C-14.6.2.2 The minimum number of trips managed per manager
	@Query("select min(m.trips.size) from Manager m")
	Integer findMinTripsPerManager();
	//C-14.6.2.3 The maximum number of trips managed per manager
	@Query("select max(m.trips.size) from Manager m")
	Integer findMaxTripsPerManager();
	//C-14.6.2.4 The standard deviation of the number of trips managed per manager
	@Query("select sqrt(sum(m.trips.size * m.trips.size) / count(m.trips.size) - (avg(m.trips.size) * avg(m.trips.size))) from Manager m")
	Double findStandardDeviationOfTripsPerManager();

	//B-16.4.6 The ratio of suspicious managers
	@Query("select (select count(*) from Manager m where m.isSuspicious = true)*1.0 / count(m) from Manager m")
	Double findRatioOfSuspiciousManagers();
}
