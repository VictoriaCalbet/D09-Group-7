
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Actor;
import domain.Folder;

@Repository
public interface FolderRepository extends JpaRepository<Folder, Integer> {

	@Query("select a from Actor a join a.folders f where f.id = ?1")
	Actor findFolderOwner(int folderId);

	@Query("select f from Actor a join a.folders f where a.id = ?1 and f.name = ?2")
	Folder findFolderByOwnerAndName(int actorId, String folderName);

	//	@Query("select f from Actor a join a.folders f join f.messages m where a.id = ?1 and m.id = ?2")
	//	Folder findByActorAndMessage(int actorId, int messageId);

	@Query("select f from Actor a join a.folders f where a.id = ?1 and f.parent is null")
	Collection<Folder> findAllRootFoldersByActorId(int actorId);

	@Query("select f from Folder f where f.parent.id = ?1")
	Collection<Folder> findAllChildrenByFolderId(int folderId);
}
