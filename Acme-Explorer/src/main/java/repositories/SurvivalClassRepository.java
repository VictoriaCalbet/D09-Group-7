
package repositories;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.SurvivalClass;

@Repository
public interface SurvivalClassRepository extends JpaRepository<SurvivalClass, Integer> {

	//Find the survivalClasses of a trip by an application id
	@Query("select t.survivalClasses from Trip t join t.applications a where a.id=?1")
	Set<SurvivalClass> findSurvivalClassesFromExplorerApplicationsId(int applicationId);

}
