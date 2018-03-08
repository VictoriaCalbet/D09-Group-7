
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Curriculum;

@Repository
public interface CurriculumRepository extends JpaRepository<Curriculum, Integer> {
	
	@Query("select c from Curriculum c where c.ticker= ?1")
	public Curriculum findOneByTicker(String ticker);
	@Query("select c from Curriculum c where c.personalRecord.id = ?1")
	public Curriculum findOneByPersonalRecordId(int recordId);
	@Query("select c from Curriculum c join c.endorserRecords r where r.id= ?1")
	public Curriculum findOneByEndorserRecordId(int recordId);
	@Query("select c from Curriculum c join c.educationRecords r where r.id= ?1")
	public Curriculum findOneByEducationRecordId(int recordId);
	@Query("select c from Curriculum c join c.professionalRecords r where r.id= ?1")
	public Curriculum findOneByProfessionalRecordId(int recordId);
	@Query("select c from Curriculum c join c.miscellaneousRecords r where r.id= ?1")
	public Curriculum findOneByMiscellaneousRecordId(int recordId);

}
