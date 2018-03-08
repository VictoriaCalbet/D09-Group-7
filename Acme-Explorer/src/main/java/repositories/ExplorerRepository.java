
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Explorer;

@Repository
public interface ExplorerRepository extends JpaRepository<Explorer, Integer> {

	@Query("select e from Explorer e where e.userAccount.id = ?1")
	public Explorer findByUserAccountId(int id);

	@Query("select e from Explorer e join e.applications a where a.id = ?1")
	Explorer findOneByApplicationId(int applicationId);

}
