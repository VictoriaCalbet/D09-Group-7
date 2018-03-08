
package services;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.NoteRepository;
import domain.Auditor;
import domain.Manager;
import domain.Note;
import domain.Trip;

@Service
@Transactional
public class NoteService {

	// Managed Repository
	@Autowired
	private NoteRepository	noteRepository;

	// Supporting Services

	@Autowired
	private AuditorService	auditorService;

	@Autowired
	private ManagerService	managerService;

	@Autowired
	private MessageService	messageService;


	// Constructor

	public NoteService() {
		super();
	}

	// Simple CRUD methods

	public Note findOne(final int noteId) {

		final Note n = this.noteRepository.findOne(noteId);
		return n;
	}

	public Collection<Note> findAll() {

		final Collection<Note> notes = this.noteRepository.findAll();
		return notes;
	}

	public Note create() {

		final Auditor principal = this.auditorService.findByPrincipal();
		Assert.notNull(principal, "message.error.audit.login");

		final Note n = new Note();
		n.setCreationMoment(new Date(System.currentTimeMillis() - 1));

		return n;
	}

	public Note saveFromCreate(final Note n) {
		//Esto es lo que salva el auditor
		final Auditor principal = this.auditorService.findByPrincipal();
		Boolean isSuspicious;

		Assert.notNull(principal, "message.error.audit.login");
		Assert.notNull(n, "message.error.note.null");
		Assert.notNull(n.getRemark(), "message.error.note.remark");
		Assert.isTrue(n.getId() <= 0, "message.error.note.edit");
		Note result = null;
		n.setCreationMoment(new Date(System.currentTimeMillis() - 1));

		isSuspicious = principal.getIsSuspicious();
		isSuspicious = isSuspicious || this.messageService.checkSpam(n.getRemark());
		principal.setIsSuspicious(isSuspicious);

		this.auditorService.saveFromEdit(principal);

		result = this.noteRepository.save(n);
		// TODO anadir al auditor
		// TODO anadir al trip

		final Auditor au = this.auditorService.findByPrincipal();
		final Collection<Note> auNotes = au.getNotes();
		auNotes.add(result);
		au.setNotes(auNotes);
		this.auditorService.saveFromEdit(au);

		//this.referenceNoteToTrip(result, n.getTrip());
		// TODO comprobar si el trip se guarda en los test

		return result;
	}

	public Note saveFromReply(final Note n) {

		//ESTO TIENE LUGAR CUANDO UN MANAGER RESPONDE A LA NOTE. TEN EN CUENTA QUE LA NOTE NO PUEDE SER NULA Y QUE HAY QUE
		//COMPROBAR QUE LOS DATOS QUE NO SE PUEDAN EDITAR SON IGUALES. SI CAMBIAN, SE ESTA INTENTANDO HACKEAR LA NOTA.  
		//SOLO PUEDEN CAMBIAR LA REPLY Y EL TIEMPO DE RESPUESTA DE LA REPLY
		final Manager principal = this.managerService.findByPrincipal();
		Boolean isSuspicious;
		Assert.notNull(principal, "message.error.manager.login");
		Assert.notNull(n, "message.error.note.null");
		Assert.isTrue(principal.equals(n.getTrip().getManager()), "message.error.note.notTheCorrectManager");
		//Comprueba que no se intenta editar una nota nueva
		Assert.isTrue(n.getId() > 0);
		Assert.notNull(n.getResponse(), "message,error.note.response");
		Assert.isTrue(!n.getResponse().isEmpty(),"message.error.note.response");

		//Busca la nota que tenga el id de la que te pasan en la base de datos.
		final Note noteDB = this.noteRepository.findOne(n.getId());
		//Comprueba que los datos que no se deben cambiar son iguales (no se han alterado)
		//Assert.isTrue(n.getCreationMoment().equals(noteDB.getCreationMoment()));
		Assert.isTrue(n.getRemark().equals(noteDB.getRemark()));
		n.setResponseMoment(new Date(System.currentTimeMillis() - 1));

		isSuspicious = principal.getIsSuspicious();
		isSuspicious = isSuspicious || this.messageService.checkSpam(n.getResponse());
		principal.setIsSuspicious(isSuspicious);

		this.managerService.saveFromEdit(principal);

		final Note res = this.noteRepository.save(n);

		return res;
	}
	// Other business methods
	//B.16.4.1

	//B-16.4.1.1The minimum number of notes per trip
	public Integer findMinNotesPerTrip() {
		return this.noteRepository.findMinNotesPerTrip();
	}

	//B-16.4.1.2The maximum number of notes per trip
	public Integer findMaxNotesPerTrip() {
		return this.noteRepository.findMaxNotesPerTrip();
	}

	//B-16.4.1.3 The average number of notes per trip
	public Double findAverageNumberOfNotesPerTrip() {
		return this.noteRepository.findAverageNumberOfNotesPerTrip();
	}

	//B-16.4.1.4 The standard deviation of the number of notes per trip
	public Double findStandardDeviationOfNotesPerTrip() {
		return this.noteRepository.findStandardDeviationOfNotesPerTrip();
	}

	public void referenceNoteToTrip(final Note n, final Trip t) {

		//al hacer el test, comprobar: crear una nota, pillar un trip, contar el numero de notas que tiene, anadir la nota y contar otra vez para ver si el numero ha cambiado

		Assert.notNull(n);
		Assert.notNull(t);

		final Collection<Note> notesTrip = t.getNotes();
		Assert.notNull(notesTrip);
		notesTrip.add(n);
		t.setNotes(notesTrip);
	}

}
