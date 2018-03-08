
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Ranger;

@Repository
public interface RangerRepository extends JpaRepository<Ranger, Integer> {

	//Find a Ranger by an user account id
	@Query("select r from Ranger r where r.userAccount.id = ?1")
	public Ranger findByUserAccountId(int id);
	//B-30 Find a Ranger by a trip id
	@Query("select r from Ranger r join r.trips t where t.id= ?1")
	public Ranger findByTripId(int id);
	//Find all suspicious Rangers
	@Query("select r from Ranger r where r.isSuspicious is true")
	Collection<Ranger> findAllSuspicious();

	@Query("select r from Ranger r where r.isBanned = false")
	Collection<Ranger> findAllNotBanned();

	//C-14.6.4.1 The average number trips guided per ranger
	@Query("select avg(r.trips.size)from Ranger r")
	public Double findAverageTripPerRanger();

	//C-14.6.4.2 The minimum number trips guided per ranger
	@Query("select min(r.trips.size) from Ranger r")
	public Integer findMinTripsPerRanger();

	//C-14.6.4.3 The maximum number trips guided per ranger
	@Query("select max(r.trips.size) from Ranger r")
	public Integer findMaxTripsPerRanger();

	//C-14.6.4.4 The standard deviation of the number trips guided per ranger
	@Query("select sqrt(sum(r.trips.size * r.trips.size) / count(r.trips.size) - (avg(r.trips.size) * avg(r.trips.size))) from Ranger r")
	public Double findStandardDeviationOfTripsPerRanger();

	//B-16.4.4 The ratio of rangers who have registered their curricula
	@Query("select (select count(r.curriculum)*1.0 from Ranger r)/count(ra) from Ranger ra")
	public Double findRatioOfRangersWithCurricula();

	//B-16.4.5 The ratio of rangers whose curriculum’s been endorsed.
	@Query("select sum(case when r.curriculum.endorserRecords.size>0 then 1 else 0 end)*1.0/count(r) from Ranger r")
	public Double findRatioOfRangersWithCurriculumEndorsed();

	//B-16.4.7 The ratio of suspicious rangers

	@Query("select (select count(*) from Ranger r where r.isSuspicious = true)*1.0 / count(r) from Ranger r")
	public Double findRatioOfSuspiciousRangers();
}
