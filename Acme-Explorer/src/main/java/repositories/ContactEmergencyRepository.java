
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import domain.ContactEmergency;

@Repository
public interface ContactEmergencyRepository extends JpaRepository<ContactEmergency, Integer> {

}
