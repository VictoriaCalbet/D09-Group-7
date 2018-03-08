
package services;

import java.util.Collection;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.FolderRepository;
import domain.Actor;
import domain.Folder;
import domain.Message;

@Service
@Transactional
public class FolderService {

	// Managed Repository
	@Autowired
	private FolderRepository	folderRepository;

	// Supporting Services
	@Autowired
	private ActorService		actorService;

	@Autowired
	private MessageService		messageService;


	// Constructor

	public FolderService() {
		super();
	}

	// Simple CRUD methods

	public Folder create() {
		Folder folder;

		folder = new Folder();

		folder.setSystemFolder(false); // Must change when creating an Actor 

		return folder;
	}

	public Folder save(final Folder folder) {
		Assert.notNull(folder, "message.error.folder.notNull");

		Folder result;

		result = this.folderRepository.save(folder);

		return result;
	}

	public Folder saveFromCreate(final Folder folder) {
		Assert.notNull(folder, "message.error.folder.notNull");
		Assert.isTrue(folder.getId() == 0, "message.error.folder.id.zero");
		Assert.notNull(folder.getName(), "message.error.folder.name");
		Assert.isTrue(this.checkSystemFolderNames(folder), "message.error.folder.system.name");
		Assert.isTrue(this.checkFolderNameNotRepeated(folder), "message.error.folder.name.repeated");

		Folder result;
		Actor principal;
		Boolean isSuspicious;

		principal = this.actorService.findByPrincipal();

		result = this.save(folder);
		principal = this.addFolderToActor(result, principal);

		isSuspicious = principal.getIsSuspicious();
		isSuspicious = isSuspicious || this.messageService.checkSpam(folder.getName());
		principal.setIsSuspicious(isSuspicious);
		this.actorService.save(principal);

		return result;
	}

	public Folder saveFromEdit(final Folder folder) {
		Assert.isTrue(this.actorService.checkLogin(), "message.error.folder.login");
		Assert.notNull(folder, "message.error.folder.notNull");
		Assert.isTrue(folder.getId() != 0, "message.error.folder.id.notZero");
		Assert.notNull(folder.getName(), "message.error.folder.name");
		Assert.isTrue(this.checkSystemFolderNames(folder), "message.error.folder.system.name");
		Assert.isTrue(this.checkFolderNameNotRepeated(folder), "message.error.folder.name.repeated");

		// Only the owner can delete a folder;
		Actor owner;
		Actor principal;
		Boolean isSuspiciuos;

		owner = this.folderRepository.findFolderOwner(folder.getId());
		Assert.notNull(owner, "message.error.folder.owner.exists");
		principal = this.actorService.findByPrincipal();
		Assert.notNull(principal, "message.error.folder.login");
		Assert.isTrue(owner.equals(principal), "message.error.folder.owner.principal");

		Folder result;
		result = this.save(folder);

		principal = this.actorService.findByPrincipal();
		isSuspiciuos = principal.getIsSuspicious();
		isSuspiciuos = isSuspiciuos || this.messageService.checkSpam(result.getName());
		principal.setIsSuspicious(isSuspiciuos);
		this.actorService.save(principal);

		return result;
	}

	public void delete(final Folder folder) {
		Assert.isTrue(this.actorService.checkLogin(), "message.error.folder.login"); // Cannot be deleted by a anonymous.

		Assert.notNull(folder, "message.error.folder.notNull");
		Assert.isTrue(folder.getId() != 0, "message.error.folder.id.notZero");
		Assert.isTrue(!folder.getSystemFolder(), "message.error.folder.system.delete"); // Cannot delete a system folder.

		// Only the owner can delete a folder;
		Actor owner;
		Actor principal;

		owner = this.folderRepository.findFolderOwner(folder.getId());
		Assert.notNull(owner, "message.error.folder.owner.exists");
		principal = this.actorService.findByPrincipal();
		Assert.notNull(principal, "message.error.folder.login");
		Assert.isTrue(owner.equals(principal), "message.error.folder.owner.principal");

		// Remove folder from Actor Folder Collection.
		Collection<Folder> ownerFolders;
		ownerFolders = owner.getFolders();
		Assert.notNull(ownerFolders, "message.error.folder.owner.folders");
		ownerFolders.remove(folder);
		owner.setFolders(ownerFolders);
		this.actorService.save(owner);

		// Move folder's messages to inbox folder;
		final Collection<Message> allMessagesInFolderAsSender;
		final Collection<Message> allMessagesInFolderAsRecipient;

		allMessagesInFolderAsSender = this.messageService.findAllInFolderAsSender(folder.getId());
		allMessagesInFolderAsRecipient = this.messageService.findAllInFolderAsRecipient(folder.getId());

		for (final Message m : allMessagesInFolderAsSender) {
			final Actor sender = m.getSender();
			final Folder senderTrashbox = this.folderRepository.findFolderByOwnerAndName(sender.getId(), "trashbox");
			m.setSenderFolder(senderTrashbox);
			this.messageService.saveFromEdit(m);
		}

		for (final Message m : allMessagesInFolderAsRecipient) {
			final Actor recipient = m.getRecipient();
			final Folder recipientTrashbox = this.folderRepository.findFolderByOwnerAndName(recipient.getId(), "trashbox");
			m.setRecipientFolder(recipientTrashbox);
			this.messageService.saveFromEdit(m);
		}

		this.folderRepository.delete(folder);
	}

	// Other business methods

	public Collection<Folder> findAll() {
		final Collection<Folder> result;

		result = this.folderRepository.findAll();

		return result;
	}

	public Folder findOne(final int folderId) {
		final Folder result;

		result = this.folderRepository.findOne(folderId);

		return result;
	}

	public Integer count() {
		Integer result;

		result = (int) this.folderRepository.count();

		return result;
	}

	public Actor findFolderOwner(final int folderId) {
		Actor result;

		result = this.folderRepository.findFolderOwner(folderId);

		return result;
	}

	public Folder findFolderByOwnerAndName(final int actorId, final String folderName) {
		final Folder result;

		result = this.folderRepository.findFolderByOwnerAndName(actorId, folderName);

		return result;
	}

	public Collection<Folder> findAllRootFoldersByActorId(final int actorId) {
		Collection<Folder> result;

		result = this.folderRepository.findAllRootFoldersByActorId(actorId);

		return result;
	}

	public Collection<Folder> findAllChildrenByFolderId(final int folderId) {
		final Collection<Folder> result;
		Actor principal;
		Folder folder;

		principal = this.actorService.findByPrincipal();
		Assert.notNull(principal, "message.error.folder.login");
		folder = this.folderRepository.findOne(folderId);
		Assert.notNull(folder, "message.error.folder.notNull");
		Assert.isTrue(principal.getFolders().contains(folder), "message.error.folder.owner.principal");

		result = this.folderRepository.findAllChildrenByFolderId(folderId);

		return result;
	}

	// DO NOT USE TO INITIALISE A USER
	public Actor addFolderToActor(final Folder folder, final Actor actor) {
		Assert.notNull(folder);
		Assert.notNull(actor);

		Actor result;

		Collection<Folder> currentFolders;

		currentFolders = actor.getFolders();
		Assert.notNull(currentFolders);
		currentFolders.add(folder);
		actor.setFolders(currentFolders);
		result = this.actorService.save(actor);

		return result;
	}

	public Collection<Folder> initializeActor(final Actor actor) {
		final Collection<Folder> result = new HashSet<Folder>();
		final Collection<String> folderNames = new HashSet<String>();

		folderNames.add("inbox");
		folderNames.add("outbox");
		folderNames.add("notificationbox");
		folderNames.add("trashbox");
		folderNames.add("spambox");

		for (final String name : folderNames) {
			Folder folderToCreate;
			Folder folderToSave;

			folderToCreate = this.create();
			folderToCreate.setName(name);
			folderToCreate.setSystemFolder(true);
			folderToSave = this.save(folderToCreate);
			result.add(folderToSave);
		}

		actor.setFolders(result);

		return result;
	}

	private boolean checkSystemFolderNames(final Folder folder) {
		Assert.notNull(folder);

		boolean result = true;
		final Collection<String> folderNames = new HashSet<String>();

		folderNames.add("inbox");
		folderNames.add("outbox");
		folderNames.add("notificationbox");
		folderNames.add("trashbox");
		folderNames.add("spambox");

		for (final String f : folderNames)
			if (folder.getName().equals(f))
				result = false;

		return result;
	}

	private boolean checkFolderNameNotRepeated(final Folder folder) {
		Assert.notNull(folder);

		boolean result = true;
		Actor principal;
		Collection<Folder> principalFolders;

		principal = this.actorService.findByPrincipal();
		Assert.notNull(principal, "message.error.folder.login");
		principalFolders = principal.getFolders();
		Assert.notNull(principalFolders, "message.error.folder.owner.folders");

		for (final Folder f : principalFolders)
			if (f.getId() != folder.getId() && f.getName().equals(folder.getName())) {
				result = false;
				break;
			}

		return result;
	}
}
