
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Service;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Integer> {

	@Query("select distinct r.service from Request r join r.rendezvous rq join rq.creator s where s.id = ?1 and r.service.isInappropriate = false")
	public Collection<Service> findAvailableServicesByUserId(int userId);

	@Query("select distinct req.service from Rendezvous rvs join rvs.requests req where rvs.creator.id = ?1")
	public Collection<Service> findServicesByUserId(int userId);

	@Query("select distinct req.service from Rendezvous rvs join rvs.requests req where rvs.id = ?1")
	public Collection<Service> findServicesByRendezvousId(int rendezvousId);

	@Query("select srv from Service srv where srv.isInappropriate = false")
	public Collection<Service> findAvailableServices();

	@Query("select srv from Service srv where srv.id not IN (select r.service.id from Request r where r.rendezvous.id = ?1) and srv.isInappropriate = false")
	public Collection<Service> findAvailableServicesToRequest(int rendezvousId);

	// Dashboard queries

	// Acme-Rendezvous 2.0 - Requisito 6.2.1
	@Query("select s from Service s where s.requests.size > (select avg(sr.requests.size) from Service sr) order by s.requests.size desc")
	public Collection<Service> findBestSellingServices();

	// Acme-Rendezvous 2.0 - Requisito 11.2.3
	@Query("select avg(rvs.requests.size) from Rendezvous rvs")
	public Double findAvgServicesRequestedPerRendezvous();

	@Query("select min(rvs.requests.size) from Rendezvous rvs")
	public Double findMinServicesRequestedPerRendezvous();

	@Query("select max(rvs.requests.size) from Rendezvous rvs")
	public Double findMaxServicesRequestedPerRendezvous();

	@Query("select sqrt(sum(rvs.requests.size * rvs.requests.size) / count(rvs.requests.size) - (avg(rvs.requests.size) * avg(rvs.requests.size))) from Rendezvous rvs")
	public Double findStdServicesRequestedPerRendezvous();

	// Acme-Rendezvous 2.0 - Requisito 11.2.4

	@Query("select s from Service s order by s.requests.size desc")
	public Collection<Service> findTopSellingServices();
}
