
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Service;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Integer> {

	@Query("select r.service from Request r join r.rendezvous rq join rq.creator s where s.id = ?1")
	public Collection<Service> findServicesByUserId(int userId);

	//The best-selling services
	@Query("select s from Service s  order by s.requests.size desc")
	public Collection<Service> findBestSellingServices();
}
