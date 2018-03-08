
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.TagValue;

@Repository
public interface TagValueRepository extends JpaRepository<TagValue, Integer> {

	@Query("select tv from TagValue tv where tv.tag = ?1")
	Collection<TagValue> findAllByTagId(int tagId);
}
