
package services;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.MessageRepository;
import security.Authority;
import domain.Actor;
import domain.Administrator;
import domain.Application;
import domain.Explorer;
import domain.Folder;
import domain.Manager;
import domain.Message;
import domain.SystemConfiguration;

@Service
@Transactional
public class MessageService {

	// Managed Repository
	@Autowired
	private MessageRepository			messageRepository;

	// Supporting Services
	@Autowired
	private ActorService				actorService;

	@Autowired
	private FolderService				folderService;

	@Autowired
	private SystemConfigurationService	systemConfigurationService;

	@Autowired
	private ApplicationService			applicationService;

	@Autowired
	private ManagerService				managerService;

	@Autowired
	private ExplorerService				explorerService;

	@Autowired
	private AdministratorService		administratorService;


	// Constructor

	public MessageService() {
		super();
	}

	// Simple CRUD methods

	public Message create() {
		Assert.isTrue(this.actorService.checkLogin());
		final Actor principal = this.actorService.findByPrincipal();
		final Folder outbox = this.folderService.findFolderByOwnerAndName(principal.getId(), "outbox");

		Message message;
		message = new Message();

		message.setSendMoment(new Date(System.currentTimeMillis() - 1));
		message.setSender(principal);
		message.setSenderFolder(outbox);

		return message;
	}

	private Message save(final Message message) {
		Assert.notNull(message);

		Message result;

		result = this.messageRepository.save(message);

		return result;
	}

	public Message saveFromCreate(final Message message) {
		Assert.isTrue(this.actorService.checkLogin());

		// Checking that no attribute is null.
		Assert.notNull(message);
		Assert.notNull(message.getSubject());
		Assert.notNull(message.getBody());
		Assert.notNull(message.getPriority());
		Assert.notNull(message.getRecipient());
		Assert.notNull(message.getSender());
		Assert.notNull(message.getSenderFolder());

		final Message result;
		Actor principal;
		final Folder outbox;
		final Folder inbox;
		Boolean isSuspicious;

		principal = this.actorService.findByPrincipal();
		outbox = this.folderService.findFolderByOwnerAndName(principal.getId(), "outbox");

		message.setSendMoment(new Date(System.currentTimeMillis() - 1));
		message.setSender(principal);
		message.setSenderFolder(outbox);

		// Select if the message will end in spambox or inbox.
		boolean isSpam = false;
		final String destinationFolder;
		isSpam = this.checkSpam(message.getSubject()) || this.checkSpam(message.getBody());
		destinationFolder = isSpam ? "spambox" : "inbox";

		inbox = this.folderService.findFolderByOwnerAndName(message.getRecipient().getId(), destinationFolder);
		message.setRecipientFolder(inbox);

		result = this.save(message);

		// Add message to sender
		Actor sender;
		sender = result.getSender();
		Assert.notNull(sender);
		Collection<Message> senderMessages;
		senderMessages = sender.getSent();
		Assert.notNull(senderMessages);
		senderMessages.add(result);
		sender.setSent(senderMessages);

		// Set suspicious state
		isSuspicious = sender.getIsSuspicious();
		isSuspicious = isSuspicious || isSpam;
		sender.setIsSuspicious(isSuspicious);

		this.actorService.save(sender);

		// Add message to recipient
		Actor recipient;
		recipient = result.getRecipient();
		Assert.notNull(recipient);
		Collection<Message> recipientMessages;
		recipientMessages = recipient.getReceived();
		Assert.notNull(recipientMessages);
		recipientMessages.add(result);
		recipient.setReceived(recipientMessages);
		this.actorService.save(recipient);

		return result;
	}

	public Message saveFromEdit(final Message message) {
		Assert.isTrue(this.actorService.checkLogin());

		// Checking that no attribute is null.
		Assert.notNull(message);
		Assert.notNull(message.getSubject());
		Assert.notNull(message.getBody());
		Assert.notNull(message.getPriority());
		Assert.notNull(message.getSender());
		Assert.notNull(message.getRecipient());

		final Message result;
		Actor principal;

		principal = this.actorService.findByPrincipal();

		Assert.isTrue(principal.getSent().contains(message) || principal.getReceived().contains(message));
		result = this.save(message);

		return result;
	}

	public void delete(final Message message) {
		Assert.isTrue(this.actorService.checkLogin());
		Assert.notNull(message);

		Actor principal;
		principal = this.actorService.findByPrincipal();

		Assert.isTrue(principal.equals(message.getSender()) || principal.equals(message.getRecipient()));

		if (principal.equals(message.getSender()) && !message.getSenderFolder().getName().equals("trashbox")) {
			final Folder senderTrashbox = this.folderService.findFolderByOwnerAndName(principal.getId(), "trashbox");
			message.setSenderFolder(senderTrashbox);
			this.save(message);
		} else if (principal.equals(message.getSender()) && message.getSenderFolder().getName().equals("trashbox"))
			if (message.getRecipientFolder() == null)
				this.deleteComplete(message);
			else {
				message.setSenderFolder(null);
				this.save(message);
			}

		if (principal.equals(message.getRecipient()) && !message.getRecipientFolder().getName().equals("trashbox")) {
			final Folder recipientTrashbox = this.folderService.findFolderByOwnerAndName(principal.getId(), "trashbox");
			message.setRecipientFolder(recipientTrashbox);
			this.save(message);
		} else if (principal.equals(message.getRecipient()) && message.getRecipientFolder().getName().equals("trashbox"))
			if (message.getSenderFolder() == null)
				this.deleteComplete(message);
			else {
				message.setRecipientFolder(null);
				this.save(message);
			}
	}

	private void deleteComplete(final Message message) {
		Assert.isTrue(this.actorService.checkLogin());
		Assert.notNull(message);

		message.setSenderFolder(null);
		message.setRecipientFolder(null);

		Message result;
		result = this.save(message);

		// Remove the message from the sender and recipients collections.
		final Actor sender = result.getSender();
		final Actor recipient = result.getRecipient();

		Collection<Message> senderMessages;
		Collection<Message> recipientMessages;

		senderMessages = sender.getSent();
		senderMessages.remove(result);
		sender.setSent(senderMessages);
		this.actorService.save(sender);

		recipientMessages = recipient.getReceived();
		recipientMessages.remove(result);
		recipient.setReceived(recipientMessages);
		this.actorService.save(recipient);

		// Delete the message.
		this.messageRepository.delete(result);
	}

	public Collection<Message> broadcastNotification(final String subject, final String body, final String priority) {
		Assert.notNull(subject);
		Assert.notNull(body);
		Assert.notNull(priority);

		final Collection<Message> result = new HashSet<Message>();
		final Collection<Actor> allActors;

		Actor principal;
		principal = this.actorService.findByPrincipal();
		Assert.notNull(principal);
		Assert.isTrue(this.actorService.checkActorWithAuthority(principal, Authority.ADMIN));

		allActors = this.actorService.findAll();
		allActors.remove(principal);

		for (final Actor a : allActors) {
			// Entity creation.
			Message notificationToCreate;
			Message notificationToSave;

			final Folder senderFolder = this.folderService.findFolderByOwnerAndName(principal.getId(), "outbox");
			final Folder recipientFolder = this.folderService.findFolderByOwnerAndName(a.getId(), "notificationbox");

			notificationToCreate = this.create();

			// Add attributes
			notificationToCreate.setSubject(subject);
			notificationToCreate.setBody(body);
			notificationToCreate.setPriority(priority);
			notificationToCreate.setSendMoment(new Date(System.currentTimeMillis() - 1));
			notificationToCreate.setSender(principal);
			notificationToCreate.setSenderFolder(senderFolder);
			notificationToCreate.setRecipient(a);
			notificationToCreate.setRecipientFolder(recipientFolder);

			// Saving the message.
			notificationToSave = this.save(notificationToCreate);

			// Add message to sender
			Actor sender;
			sender = notificationToSave.getSender();
			Assert.notNull(sender);
			Collection<Message> senderMessages;
			senderMessages = sender.getSent();
			Assert.notNull(senderMessages);
			senderMessages.add(notificationToSave);
			sender.setSent(senderMessages);
			this.actorService.save(sender);

			// Add message to recipient
			Actor recipient;
			recipient = notificationToSave.getRecipient();
			Assert.notNull(recipient);
			Collection<Message> recipientMessages;
			recipientMessages = recipient.getReceived();
			Assert.notNull(recipientMessages);
			recipientMessages.add(notificationToSave);
			recipient.setReceived(recipientMessages);
			this.actorService.save(recipient);

			result.add(notificationToSave);

		}

		return result;
	}
	public Collection<Message> updateMessageToActorsInvolvedInApplication(final int applicationId, final String subject, final String body, final String priority) {
		Assert.notNull(subject);
		Assert.notNull(body);
		Assert.notNull(priority);

		final Collection<Message> result = new HashSet<>();

		Application application;
		application = this.applicationService.findOne(applicationId);
		Assert.notNull(application);

		Manager managerInvolved;
		Explorer explorerInvolved;
		Administrator administrator;
		final Collection<Actor> actorsInvolved = new HashSet<Actor>();

		managerInvolved = this.managerService.findOneByApplicationId(applicationId);
		explorerInvolved = this.explorerService.findOneByApplicationId(applicationId);
		administrator = this.administratorService.findAll().iterator().next();

		Assert.notNull(managerInvolved);
		Assert.notNull(explorerInvolved);

		actorsInvolved.add(managerInvolved);
		actorsInvolved.add(explorerInvolved);

		for (final Actor a : actorsInvolved) {
			// Entity creation.
			Message notificationToCreate;
			Message notificationToSave;

			final Folder senderFolder = this.folderService.findFolderByOwnerAndName(administrator.getId(), "outbox");
			final Folder recipientFolder = this.folderService.findFolderByOwnerAndName(a.getId(), "notificationbox");

			notificationToCreate = this.create();

			// Add attributes
			notificationToCreate.setSubject(subject);
			notificationToCreate.setBody(body);
			notificationToCreate.setPriority(priority);
			notificationToCreate.setSendMoment(new Date(System.currentTimeMillis() - 1));
			notificationToCreate.setSender(administrator);
			notificationToCreate.setSenderFolder(senderFolder);
			notificationToCreate.setRecipient(a);
			notificationToCreate.setRecipientFolder(recipientFolder);

			// Saving the message.
			notificationToSave = this.save(notificationToCreate);

			// Add message to sender
			Actor sender;
			sender = notificationToSave.getSender();
			Assert.notNull(sender);
			Collection<Message> senderMessages;
			senderMessages = sender.getSent();
			Assert.notNull(senderMessages);
			senderMessages.add(notificationToSave);
			sender.setSent(senderMessages);
			this.actorService.save(sender);

			// Add message to recipient
			Actor recipient;
			recipient = notificationToSave.getRecipient();
			Assert.notNull(recipient);
			Collection<Message> recipientMessages;
			recipientMessages = recipient.getReceived();
			Assert.notNull(recipientMessages);
			recipientMessages.add(notificationToSave);
			recipient.setReceived(recipientMessages);
			this.actorService.save(recipient);

			result.add(notificationToSave);

		}

		return result;

	}
	// Other business methods

	public Collection<Message> findAll() {
		Collection<Message> result;

		result = this.messageRepository.findAll();

		return result;
	}

	public Message findOne(final int messageId) {
		Message result;

		result = this.messageRepository.findOne(messageId);

		return result;
	}

	public Integer count() {
		Integer result;

		result = (int) this.messageRepository.count();

		return result;
	}

	public Collection<Message> findAllInFolder(final int folderId) {
		Collection<Message> result;

		result = this.messageRepository.findAllInFolder(folderId);

		return result;
	}

	public Collection<Message> findAllInFolderAsSender(final int folderId) {
		Collection<Message> result;

		result = this.messageRepository.findAllInFolderAsSender(folderId);

		return result;
	}

	public Collection<Message> findAllInFolderAsRecipient(final int folderId) {
		Collection<Message> result;

		result = this.messageRepository.findAllInFolderAsRecipient(folderId);

		return result;
	}

	public boolean checkSpam(final String string) {
		Assert.notNull(string);

		boolean result = false;

		Collection<SystemConfiguration> systemConfigurations;
		final Collection<String> spamWords;
		SystemConfiguration systemConfiguration;

		systemConfigurations = this.systemConfigurationService.findAll();
		Assert.notNull(systemConfigurations);
		systemConfiguration = systemConfigurations.iterator().next();
		Assert.notNull(systemConfiguration);
		spamWords = systemConfiguration.getSpamWords();
		Assert.notNull(spamWords);

		for (final String spamWord : spamWords)
			if (string.toLowerCase().contains(spamWord)) {
				result = true;
				break;
			}

		return result;
	}
}
