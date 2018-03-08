
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Category;
import domain.Trip;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

	@Query("select c from Category c where c.parent.id = ?1")
	Collection<Category> findAllChildren(Integer id);

	@Query("select c.trips from Category c where c.id=?1")
	Collection<Trip> browseTripsByCategory(Integer id);

	@Query("select c from Category c where c.name != 'CATEGORY'")
	Collection<Category> findAllWithOutCATEGORY();

}
