
package services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.StoryRepository;
import domain.Explorer;
import domain.Story;

@Service
@Transactional
public class StoryService {

	// Managed Repository
	@Autowired
	private StoryRepository	storyRepository;

	// Supporting Services

	@Autowired
	private MessageService	messageService;

	@Autowired
	private ExplorerService	explorerService;


	// Constructor

	public StoryService() {
		super();
	}

	// Simple CRUD methods

	public Story create() {
		final Explorer e = this.explorerService.findByPrincipal();
		Assert.notNull(e, "message.error.explorer.login");
		final Story s = new Story();

		final Collection<String> attatchments = new ArrayList<String>();

		s.setAttachmentUrls(attatchments);

		return s;
	}

	public Story save(final Story s) {
		final Explorer e = this.explorerService.findByPrincipal();
		Boolean isSuspicious;

		Assert.notNull(e, "message.error.explorer.login");
		Assert.notNull(s, "message.error.story.null");
		Assert.notNull(s.getTitle(), "message.error.story.title");
		Assert.notNull(s.getPieceOfText(), "message.error.story.pieceOfText");
		
		Pattern p = Pattern.compile("^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]");  

		isSuspicious = e.getIsSuspicious();
		isSuspicious = isSuspicious || this.messageService.checkSpam(s.getTitle()) || this.messageService.checkSpam(s.getPieceOfText());
		
		if(!(s.getAttachmentUrls().isEmpty())){
		Collection<String> attachments = new ArrayList<String>(Arrays.asList(s.getAttachmentUrls().iterator().next().split(" ")));
		
		for (final String st : attachments) {
			
			Matcher m = p.matcher(st);
			Assert.isTrue(m.matches());
			isSuspicious = isSuspicious || this.messageService.checkSpam(st);
			
			if (isSuspicious == true)
				break;
			}
			s.setAttachmentUrls(attachments);
		}
		e.setIsSuspicious(isSuspicious);
		
		final Story savedS = this.storyRepository.save(s);
		
		final Collection<Story> stories = e.getStories();
		stories.add(savedS);
		e.setStories(stories);
		
		this.explorerService.saveFromEdit(e);

		return savedS;
	}

	public Collection<Story> findAll() {

		final Collection<Story> stories = this.storyRepository.findAll();
		return stories;
	}

	// Other business methods

}
