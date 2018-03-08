
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Audit;

@Repository
public interface AuditRepository extends JpaRepository<Audit, Integer> {

	//B-16.4.2.1 The minimum number of audit records per trip
	@Query("select min(t.audits.size) from Trip t")
	public Integer findMinAuditsPerTrip();

	//B-16.4.2.2 The maximum number of audit records per trip
	@Query("select max(t.audits.size) from Trip t")
	public Integer findMaxAuditsPerTrip();

	//B-16.4.2.3 The average number of audit records per trip
	@Query("select avg(t.audits.size) from Trip t")
	public Double findAverageAuditsPerTrip();

	//B-16.4.2.4 The standard deviation of the number of audit records per trip

	@Query("select sqrt(sum(t.audits.size * t.audits.size) / count(t.audits.size) - (avg(t.audits.size) * avg(t.audits.size))) from Trip t")
	public Double findStandardDeviationOfAuditsPerTrip();

	@Query("select a from Audit a where a.isDraft=false and a.trip.id=?1")
	public Collection<Audit> findAllFinalAudits(int id);

}
