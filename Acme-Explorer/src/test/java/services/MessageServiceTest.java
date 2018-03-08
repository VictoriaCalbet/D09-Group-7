
package services;

import java.util.Collection;
import java.util.HashSet;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Actor;
import domain.Folder;
import domain.Message;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"
})
@Transactional
public class MessageServiceTest extends AbstractTest {

	// Service under test
	@Autowired
	private MessageService	messageService;

	// Supporting services
	@Autowired
	private ActorService	actorService;

	@Autowired
	private FolderService	folderService;


	// Tests

	@Test
	/**
	 * Positive test: Correct entity creation.
	 */
	public void testCreate1() {
		this.authenticate("sponsor1");

		Message messageToCreate;
		final Actor principal = this.actorService.findByPrincipal();
		final Folder outbox = this.folderService.findFolderByOwnerAndName(principal.getId(), "outbox");

		messageToCreate = this.messageService.create();
		Assert.isTrue(!(messageToCreate.getId() > 0));
		Assert.isTrue(messageToCreate.getSender().equals(principal));
		Assert.isTrue(messageToCreate.getSenderFolder().equals(outbox));

		Assert.isNull(messageToCreate.getRecipient());
		Assert.isNull(messageToCreate.getRecipientFolder());

		this.unauthenticate();
	}

	@Test(expected = IllegalArgumentException.class)
	/**
	 * Negative test: Unlogged user.
	 */
	public void testCreate2() {

		Message messageToCreate;
		final Actor principal = this.actorService.findByPrincipal();
		final Folder outbox = this.folderService.findFolderByOwnerAndName(principal.getId(), "outbox");

		messageToCreate = this.messageService.create();
		Assert.isTrue(!(messageToCreate.getId() > 0));
		Assert.isTrue(messageToCreate.getSender().equals(principal));
		Assert.isTrue(messageToCreate.getSenderFolder().equals(outbox));

		Assert.isNull(messageToCreate.getRecipient());
		Assert.isNull(messageToCreate.getRecipientFolder());
	}

	@Test
	/**
	 * Positive test: Correct creation and saving.
	 */
	public void testSaveFromCreate1() {
		this.authenticate("sponsor1");

		Message messageToCreate;
		Actor principal = this.actorService.findByPrincipal();
		Folder outbox = this.folderService.findFolderByOwnerAndName(principal.getId(), "outbox");

		messageToCreate = this.messageService.create();
		Assert.isTrue(!(messageToCreate.getId() > 0));
		Assert.isTrue(messageToCreate.getSender().equals(principal));
		Assert.isTrue(messageToCreate.getSenderFolder().equals(outbox));

		Assert.isNull(messageToCreate.getRecipient());
		Assert.isNull(messageToCreate.getRecipientFolder());

		this.unauthenticate();

		this.authenticate("sponsor1");

		final Message messageToSave;
		Actor recipient;

		messageToCreate.setSubject("New subject");
		messageToCreate.setBody("New body");
		messageToCreate.setPriority("LOW");
		recipient = this.actorService.findByName("sponsor2");
		Assert.notNull(recipient);
		messageToCreate.setRecipient(recipient);

		messageToSave = this.messageService.saveFromCreate(messageToCreate);

		principal = this.actorService.findByPrincipal();

		Assert.isTrue(messageToSave.getId() > 0);
		Assert.isTrue(messageToSave.getSubject().equals(messageToCreate.getSubject()));
		Assert.isTrue(messageToSave.getBody().equals(messageToCreate.getBody()));
		Assert.isTrue(messageToSave.getPriority().equals(messageToCreate.getPriority()));
		Assert.isTrue(messageToSave.getSender().equals(principal));
		Assert.isTrue(messageToSave.getRecipient().equals(messageToCreate.getRecipient()));
		Assert.notNull(messageToSave.getSendMoment());

		outbox = this.folderService.findFolderByOwnerAndName(messageToSave.getSender().getId(), "outbox");
		final Folder inbox = this.folderService.findFolderByOwnerAndName(messageToSave.getRecipient().getId(), "inbox");

		Assert.isTrue(messageToSave.getSenderFolder().equals(outbox));
		Assert.isTrue(messageToSave.getRecipientFolder().equals(inbox));

		Actor sender;
		sender = messageToSave.getSender();
		recipient = messageToSave.getRecipient();
		Assert.isTrue(sender.getSent().contains(messageToSave));
		Assert.isTrue(recipient.getReceived().contains(messageToSave));

		this.unauthenticate();
	}

	@Test
	/**
	 * Positive test: Correct creation and saving.
	 */
	public void testSaveFromCreate2() {
		this.authenticate("sponsor1");

		Message messageToCreate;
		Actor principal = this.actorService.findByPrincipal();
		Folder outbox = this.folderService.findFolderByOwnerAndName(principal.getId(), "outbox");

		messageToCreate = this.messageService.create();
		Assert.isTrue(!(messageToCreate.getId() > 0));
		Assert.isTrue(messageToCreate.getSender().equals(principal));
		Assert.isTrue(messageToCreate.getSenderFolder().equals(outbox));

		Assert.isNull(messageToCreate.getRecipient());
		Assert.isNull(messageToCreate.getRecipientFolder());

		this.unauthenticate();

		this.authenticate("sponsor1");

		final Message messageToSave;
		Actor recipient;

		messageToCreate.setSubject("New subject");
		messageToCreate.setBody("sex");
		messageToCreate.setPriority("LOW");
		recipient = this.actorService.findByName("sponsor2");
		Assert.notNull(recipient);
		messageToCreate.setRecipient(recipient);

		messageToSave = this.messageService.saveFromCreate(messageToCreate);

		principal = this.actorService.findByPrincipal();

		Assert.isTrue(messageToSave.getId() > 0);
		Assert.isTrue(messageToSave.getSubject().equals(messageToCreate.getSubject()));
		Assert.isTrue(messageToSave.getBody().equals(messageToCreate.getBody()));
		Assert.isTrue(messageToSave.getPriority().equals(messageToCreate.getPriority()));
		Assert.isTrue(messageToSave.getSender().equals(principal));
		Assert.isTrue(messageToSave.getRecipient().equals(messageToCreate.getRecipient()));
		Assert.notNull(messageToSave.getSendMoment());

		outbox = this.folderService.findFolderByOwnerAndName(messageToSave.getSender().getId(), "outbox");
		final Folder spambox = this.folderService.findFolderByOwnerAndName(messageToSave.getRecipient().getId(), "spambox");

		Assert.isTrue(messageToSave.getSenderFolder().equals(outbox));
		Assert.isTrue(messageToSave.getRecipientFolder().equals(spambox));

		Actor sender;
		sender = messageToSave.getSender();
		recipient = messageToSave.getRecipient();
		Assert.isTrue(sender.getSent().contains(messageToSave));
		Assert.isTrue(recipient.getReceived().contains(messageToSave));

		this.unauthenticate();
	}

	@Test(expected = IllegalArgumentException.class)
	/**
	 * Positive test: Unlogged user
	 */
	public void testSaveFromCreate3() {
		this.authenticate("sponsor1");

		Message messageToCreate;
		Actor principal = this.actorService.findByPrincipal();
		Folder outbox = this.folderService.findFolderByOwnerAndName(principal.getId(), "outbox");

		messageToCreate = this.messageService.create();
		Assert.isTrue(!(messageToCreate.getId() > 0));
		Assert.isTrue(messageToCreate.getSender().equals(principal));
		Assert.isTrue(messageToCreate.getSenderFolder().equals(outbox));

		Assert.isNull(messageToCreate.getRecipient());
		Assert.isNull(messageToCreate.getRecipientFolder());

		this.unauthenticate();

		final Message messageToSave;
		Actor recipient;

		messageToCreate.setSubject("New subject");
		messageToCreate.setBody("New body");
		messageToCreate.setPriority("LOW");
		recipient = this.actorService.findByName("sponsor2");
		Assert.notNull(recipient);
		messageToCreate.setRecipient(recipient);

		messageToSave = this.messageService.saveFromCreate(messageToCreate);

		principal = this.actorService.findByPrincipal();

		Assert.isTrue(messageToSave.getId() > 0);
		Assert.isTrue(messageToSave.getSubject().equals(messageToCreate.getSubject()));
		Assert.isTrue(messageToSave.getBody().equals(messageToCreate.getBody()));
		Assert.isTrue(messageToSave.getPriority().equals(messageToCreate.getPriority()));
		Assert.isTrue(messageToSave.getSender().equals(principal));
		Assert.isTrue(messageToSave.getRecipient().equals(messageToCreate.getRecipient()));
		Assert.notNull(messageToSave.getSendMoment());

		outbox = this.folderService.findFolderByOwnerAndName(messageToSave.getSender().getId(), "outbox");
		final Folder inbox = this.folderService.findFolderByOwnerAndName(messageToSave.getRecipient().getId(), "inbox");

		Assert.isTrue(messageToSave.getSenderFolder().equals(outbox));
		Assert.isTrue(messageToSave.getRecipientFolder().equals(inbox));

		Actor sender;
		sender = messageToSave.getSender();
		recipient = messageToSave.getRecipient();
		Assert.isTrue(sender.getSent().contains(messageToSave));
		Assert.isTrue(recipient.getReceived().contains(messageToSave));

	}

	@Test
	public void testDelete1() {
		this.authenticate("sponsor1");

		Message messageToCreate;
		Actor principal = this.actorService.findByPrincipal();
		Folder outbox = this.folderService.findFolderByOwnerAndName(principal.getId(), "outbox");

		messageToCreate = this.messageService.create();
		Assert.isTrue(!(messageToCreate.getId() > 0));
		Assert.isTrue(messageToCreate.getSender().equals(principal));
		Assert.isTrue(messageToCreate.getSenderFolder().equals(outbox));

		Assert.isNull(messageToCreate.getRecipient());
		Assert.isNull(messageToCreate.getRecipientFolder());

		this.unauthenticate();

		this.authenticate("sponsor1");

		final Message messageToSave;
		Actor recipient;

		messageToCreate.setSubject("New subject");
		messageToCreate.setBody("New body");
		messageToCreate.setPriority("LOW");
		recipient = this.actorService.findByName("sponsor2");
		Assert.notNull(recipient);
		messageToCreate.setRecipient(recipient);

		messageToSave = this.messageService.saveFromCreate(messageToCreate);

		principal = this.actorService.findByPrincipal();

		Assert.isTrue(messageToSave.getId() > 0);
		Assert.isTrue(messageToSave.getSubject().equals(messageToCreate.getSubject()));
		Assert.isTrue(messageToSave.getBody().equals(messageToCreate.getBody()));
		Assert.isTrue(messageToSave.getPriority().equals(messageToCreate.getPriority()));
		Assert.isTrue(messageToSave.getSender().equals(principal));
		Assert.isTrue(messageToSave.getRecipient().equals(messageToCreate.getRecipient()));
		Assert.notNull(messageToSave.getSendMoment());

		outbox = this.folderService.findFolderByOwnerAndName(messageToSave.getSender().getId(), "outbox");
		final Folder inbox = this.folderService.findFolderByOwnerAndName(messageToSave.getRecipient().getId(), "inbox");

		Assert.isTrue(messageToSave.getSenderFolder().equals(outbox));
		Assert.isTrue(messageToSave.getRecipientFolder().equals(inbox));

		Actor sender;
		sender = messageToSave.getSender();
		recipient = messageToSave.getRecipient();
		Assert.isTrue(sender.getSent().contains(messageToSave));
		Assert.isTrue(recipient.getReceived().contains(messageToSave));

		this.unauthenticate();

		this.authenticate("sponsor1");

		this.messageService.delete(messageToSave); // Move the message from outbox to trashbox

		Message messageToSenderTrashbox;
		messageToSenderTrashbox = this.messageService.findOne(messageToSave.getId());

		Assert.isTrue(messageToSenderTrashbox.getSenderFolder().getName().equals("trashbox"));
		Assert.isTrue(messageToSenderTrashbox.getRecipientFolder().getName().equals("inbox"));

		this.unauthenticate();

		this.authenticate("sponsor1");

		this.messageService.delete(messageToSenderTrashbox); // Delete the message from trashbox

		Message messageDeletedBySender;
		messageDeletedBySender = this.messageService.findOne(messageToSenderTrashbox.getId());

		Assert.isNull(messageDeletedBySender.getSenderFolder());
		Assert.isTrue(messageDeletedBySender.getRecipientFolder().getName().equals("inbox"));

		this.unauthenticate();

		this.authenticate("sponsor2");

		this.messageService.delete(messageDeletedBySender); // Move the message from inbox to trashbox.

		Message messageToRecipientTrashbox;
		messageToRecipientTrashbox = this.messageService.findOne(messageDeletedBySender.getId());

		Assert.isNull(messageToRecipientTrashbox.getSenderFolder());
		Assert.isTrue(messageToRecipientTrashbox.getRecipientFolder().getName().equals("trashbox"));

		this.unauthenticate();

		this.authenticate("sponsor2");

		this.messageService.delete(messageToRecipientTrashbox);

		Message messageDeleted;
		messageDeleted = this.messageService.findOne(messageToRecipientTrashbox.getId());
		Assert.isNull(messageDeleted);

		final Actor senderFinal = this.actorService.findByName("sponsor1");
		final Actor recipientFinal = this.actorService.findByName("sponsor2");

		Assert.isTrue(!senderFinal.getSent().contains(messageToRecipientTrashbox));
		Assert.isTrue(!recipientFinal.getReceived().contains(messageToRecipientTrashbox));

		this.unauthenticate();
	}

	@Test
	public void testBroadcast1() {
		this.authenticate("admin");

		Collection<Message> allMessages;
		String subject;
		String body;
		String priority;

		allMessages = new HashSet<>();
		subject = "Welcome to Acme Explorer.";
		body = "Welcome everyone.";
		priority = "NEUTRAL";

		allMessages = this.messageService.broadcastNotification(subject, body, priority);

		Integer numberOfActors;

		numberOfActors = this.actorService.count() - 1;
		Assert.isTrue(allMessages.size() == numberOfActors);

		for (final Message message : allMessages) {
			Assert.notNull(message.getSubject());
			Assert.notNull(message.getBody());
			Assert.notNull(message.getSendMoment());
			Assert.notNull(message.getPriority());
			Assert.notNull(message.getSender());
			Assert.notNull(message.getSenderFolder());
			Assert.notNull(message.getRecipient());
			Assert.notNull(message.getRecipientFolder());

			Actor sender;
			Actor recipient;

			sender = message.getSender();
			recipient = message.getRecipient();

			Assert.isTrue(sender.getSent().contains(message));
			Assert.isTrue(recipient.getReceived().contains(message));

		}

		this.unauthenticate();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testBroadcast2() {

		Collection<Message> allMessages;
		String subject;
		String body;
		String priority;

		allMessages = new HashSet<>();
		subject = "Welcome to Acme Explorer.";
		body = "Welcome everyone.";
		priority = "NEUTRAL";

		allMessages = this.messageService.broadcastNotification(subject, body, priority);

		Integer numberOfActors;

		numberOfActors = this.actorService.count() - 1;
		Assert.isTrue(allMessages.size() == numberOfActors);

		for (final Message message : allMessages) {
			Assert.notNull(message.getSubject());
			Assert.notNull(message.getBody());
			Assert.notNull(message.getSendMoment());
			Assert.notNull(message.getPriority());
			Assert.notNull(message.getSender());
			Assert.notNull(message.getSenderFolder());
			Assert.notNull(message.getRecipient());
			Assert.notNull(message.getRecipientFolder());

			Actor sender;
			Actor recipient;

			sender = message.getSender();
			recipient = message.getRecipient();

			Assert.isTrue(sender.getSent().contains(message));
			Assert.isTrue(recipient.getReceived().contains(message));

		}

	}
}
