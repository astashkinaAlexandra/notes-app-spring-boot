package ru.mirea.study.springbootbackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mirea.study.springbootbackend.exception.ResourceNotFoundException;
import ru.mirea.study.springbootbackend.model.Note;
import ru.mirea.study.springbootbackend.repository.FolderRepository;
import ru.mirea.study.springbootbackend.repository.NoteRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class NoteController {

    @Autowired
    private NoteRepository noteRepository;
    @Autowired
    private FolderRepository folderRepository;

    // get all notes
    @GetMapping("/notes")
    public List<Note> getAllNotes() {
        return noteRepository.findAll();
    }

    // get notes by folder id
    @GetMapping("/folders/{id}/notes")
    public ResponseEntity<List<Note>> getAllNotesByFolderId(@PathVariable(value = "id") Long folderId) throws ResourceNotFoundException {
        if (!folderRepository.existsById(folderId)) {
            throw new ResourceNotFoundException("Notes not found for this folder id: " + folderId);
        }

        List<Note> notes = noteRepository.findByFolderId(folderId);
        return ResponseEntity.ok().body(notes);
    }

    // create note rest api
    @PostMapping("/folders/{id}/notes")
    public ResponseEntity<Note> createNote(@PathVariable(value = "id") Long folderId,
                                                 @RequestBody Note noteRequest) throws ResourceNotFoundException {
        Note note = folderRepository.findById(folderId).map(folder -> {
            noteRequest.setFolder(folder);
            return noteRepository.save(noteRequest);
        }).orElseThrow(() -> new ResourceNotFoundException("Folder not found for this id: " + folderId));
        return ResponseEntity.ok().body(note);
    }

    // update note rest api
    @PutMapping("/notes/{id}")
    public ResponseEntity<Note> updateNote(@PathVariable Long id, @RequestBody Note noteDetails) {
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Note not exist with id: " + id));

        note.setText(noteDetails.getText());
        note.setCreatedDate(noteDetails.getCreatedDate());

        Note updatedNote = noteRepository.save(note);
        return ResponseEntity.ok(updatedNote);
    }

    // delete note rest api
    @DeleteMapping("/notes/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteNote(@PathVariable Long id) {
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Note not exist with id: " + id));

        noteRepository.delete(note);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/folders/{id}/notes")
    public Map<String, Boolean> deleteAllNotesOfFolder(@PathVariable(value = "id") Long folderId) throws ResourceNotFoundException {
        if (!folderRepository.existsById(folderId)){
            throw new ResourceNotFoundException("Folder not found for this id: " + folderId);
        }

        noteRepository.deleteByFolderId(folderId);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
}
