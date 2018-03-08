
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Application;
import domain.Trip;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Integer> {

	@Query("select e.applications from Explorer e where e.id = ?1")
	Collection<Application> findApplicationsByExplorer(int explorerId);
	//Find the applications accepted from a trip
	@Query("select a from Trip t join t.applications a where t.id = ?1 and a.status = 'ACCEPTED'")
	Collection<Application> findApplicationsByTripIdAndApplicationAccepted(int tripId);

	//12.2 
	@Query("select distinct t.applications from Manager m join m.trips t where m.id=?1")
	Collection<Application> listApplicationsOfManager(int idManager);

	@Query("select t from Trip t join t.applications a where a.id = ?1")
	Trip findTripByApplication(int applicationId);

	@Query("select e.applications from Explorer e right join e.applications a where e.id = ?1 group by a.status")
	Collection<Application> groupPrincipalApplicationsByStatus(int explorerId);

	//C-14.6.1.1  The average number of applications per trip
	@Query("select avg(t.applications.size) from Trip t")
	Double findAverageApplicationPerTrip();

	//C-14.6.1.2 The minimum number of applications per trip
	@Query("select min(t.applications.size) from Trip t")
	Integer findMinApplicationPerTrip();

	//C-14.6.1.3 The maximum number of applications per trip
	@Query("select max(t.applications.size) from Trip t")
	Integer findMaxApplicationPerTrip();

	//C-14.6.1.4 The standard deviation of the number of applications per trip
	@Query("select sqrt(sum(t.applications.size * t.applications.size) / count(t.applications.size) - (avg(t.applications.size) * avg(t.applications.size))) from Trip t")
	Double findStandardDeviationOfApplicationPerTrip();

	//C-14.6.5 The ratio of applications with status "PENDING"
	@Query("select count(a)*1.0/(select count (a1) from Application a1) from Application a where a.status = 'PENDING'")
	Double findRatioOfApplicationsWithPending();

	//C-14.6.6 The ratio of applications with status "DUE"

	@Query("select count(a)*1.0/(select count (a1) from Application a1) from Application a where a.status = 'DUE'")
	Double findRatioOfApplicationsWithDue();

	//C-14.6.7 The ratio of applications with status "ACCEPTED"

	@Query("select count(a)*1.0/(select count (a1) from Application a1) from Application a where a.status = 'ACCEPTED'")
	Double findRatioOfApplicationsWithAccepted();

	//C-14.6.8 The ratio of applications with status "CANCELLED"

	@Query("select count(a)*1.0/(select count (a1) from Application a1) from Application a where a.status = 'CANCELLED'")
	Double findRatioOfApplicationsWithCancelled();
}
