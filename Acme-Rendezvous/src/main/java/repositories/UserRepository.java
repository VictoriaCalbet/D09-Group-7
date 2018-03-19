
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	@Query("select u from User u where u.userAccount.id = ?1")
	public User findByUserAccountId(int id);

	@Query("select u from RSVP rv join rv.user u where rv.rendezvous.id=?1 and rv.isCancelled = false")
	Collection<User> findAttendantsOfRendezvous(int rendezvousId);

	// Dashboard queries

	// Acme-Rendezvous 1.0 - Requisito 6.3.2
	@Query("select count(usr)*1.0/(select count(u) from User u where u.rendezvoussesCreated is empty) from User usr where usr.rendezvoussesCreated is not empty")
	public Double findRatioUserRendezvousesCreatedVsNeverCreated();

	// Acme-Rendezvous 1.0 - Requisito 6.3.3
	@Query("select avg(rvs.rsvps.size) from Rendezvous rvs")
	public Double findAvgUsersRSVPsPerRendezvous();

	@Query("select sqrt(sum(rvs.rsvps.size * rvs.rsvps.size) / count(rvs.rsvps.size) - (avg(rvs.rsvps.size) * avg(rvs.rsvps.size))) from Rendezvous rvs")
	public Double findStdUsersRSVPsPerRendezvous();

}
