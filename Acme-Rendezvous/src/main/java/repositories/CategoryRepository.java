
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

	//@Query("select avg(cat.rendezvouses.size) from Category cat")
	//public Integer getAverageNumberOfCategoriesPerRendezvous();
	
	@Query("select count(cat.services.size)*1.0/(select count(ca.services.size) from Category ca) from Category cat")
	public Integer getRatioOfServicesPerEachCategory();
	
	@Query("select cat from Category cat where cat.parent.id = ?1")
	public Collection<Category> getCategoriesByParent(int id);
	
	@Query("select cat from Category cat where cat.parent = null")
	public Collection<Category> getRootCategories();
	
}
