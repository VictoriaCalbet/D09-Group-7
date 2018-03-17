
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

	// Dashboard queries

	// Acme-Rendezvous 2.0 - Requisito 6.2.2
	@Query("select m from Manager m where m.services.size > (select avg(m.services.size) from Manager m)")
	Collection<Manager> findManagersWithMoreServicesThanAverage();

	// Acme-Rendezvous 2.0 - Requisito 6.2.3
	@Query("select s.manager, count(s.manager) from Service s where s.isInappropriate=true group by s.manager")
	Collection<Manager> findManagersWithMoreServicesCancelled();
}
