
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Question;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Integer> {

	// Dashboard queries

	@Query("select avg(rvs.questions.size) from Rendezvous rvs")
	public Double findAvgNoQuestionsPerRendezvous();

	@Query("select  sqrt(sum(rvs.questions.size * rvs.questions.size) / count(rvs.questions.size) - (avg(rvs.questions.size) * avg(rvs.questions.size))) from Rendezvous rvs")
	public Double findStdNoQuestionsPerRendezvous();

}
