
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.RSVP;

@Repository
public interface RSVPRepository extends JpaRepository<RSVP, Integer> {

	@Query("select rsvp from RSVP rsvp where rsvp.rendezvous.id= ?1 and rsvp.user.id=?2")
	RSVP findRSVPByRendezvousAndUserId(int rendezvousId, int userId);
	@Query("select rsvp from RSVP rsvp where rsvp.isCancelled = true and rsvp.user = ?1")
	Collection<RSVP> findRSVPsCancelled(int userId);
}
