
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.LegalText;

@Repository
public interface LegalTextRepository extends JpaRepository<LegalText, Integer> {

	@Query("select l from LegalText l where l.isDraft=false")
	Collection<LegalText> findFinalLegalTexts();

	//C-14.6.11 A table with the number of times that each legal texts been referenced
	@Query("select lt from LegalText lt")
	Collection<LegalText> legalTextName();

}
