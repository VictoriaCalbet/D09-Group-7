
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

	@Query("select cat from Category cat where cat.parent.id = ?1")
	public Collection<Category> getCategoriesByParent(int id);

	@Query("select cat from Category cat where cat.parent = null")
	public Collection<Category> getRootCategories();

	@Query("select cat from Category cat where cat.parent is not null and cat.parent.id=?1")
	public Collection<Category> replaceParentCategories(int id);

	// Dashboard queries

	// Acme-Rendezvous 2.0 - Requisito 11.2.1
	@Query("select avg(srv.categories.size) from Rendezvous rvs join rvs.requests req join req.service srv")
	public Double findAvgCategoriesCreatedPerRendezvous();

	// Acme-Rendezvous 2.0 - Requisito 11.2.2
	@Query("select avg(cat.services.size) from Category cat")
	public Double getAvgOfServicesPerEachCategory();

	@Query("select count(cat.services.size)*1.0/(select count(ca.services.size) from Category ca) from Category cat")
	public Double getRatioOfServicesPerEachCategory();
}
