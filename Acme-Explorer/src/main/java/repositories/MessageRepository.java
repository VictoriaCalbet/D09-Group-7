
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {

	@Query("select m from Message m where m.senderFolder.id = ?1 OR m.recipientFolder.id = ?1")
	Collection<Message> findAllInFolder(int folderId);

	@Query("select m from Message m where m.senderFolder.id = ?1")
	Collection<Message> findAllInFolderAsSender(int folderId);

	@Query("select m from Message m where m.recipientFolder.id = ?1")
	Collection<Message> findAllInFolderAsRecipient(int folderId);

}
