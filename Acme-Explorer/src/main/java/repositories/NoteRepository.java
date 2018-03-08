
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Note;
import domain.Trip;

@Repository
public interface NoteRepository extends JpaRepository<Note, Integer> {

	//B-16.4.1.1The minimum number of notes per trip

	@Query("select min(t.notes.size) from Trip t")
	public Integer findMinNotesPerTrip();

	//B-16.4.1.2The maximum number of notes per trip
	@Query("select max(t.notes.size) from Trip t")
	public Integer findMaxNotesPerTrip();

	//B-16.4.1.3 The average number of notes per trip
	@Query("select avg(t.notes.size) from Trip t")
	public Double findAverageNumberOfNotesPerTrip();

	//B-16.4.1.4 The standard deviation of the number of notes per trip
	@Query("select sqrt(sum(t.notes.size * t.notes.size) / count(t.notes.size) - (avg(t.notes.size) * avg(t.notes.size))) from Trip t")
	public Double findStandardDeviationOfNotesPerTrip();
	//Get a trip by a note
	@Query("select t from Trip t join t.notes n where n.id=?1")
	public Trip getTripByNote(Integer id);

}
