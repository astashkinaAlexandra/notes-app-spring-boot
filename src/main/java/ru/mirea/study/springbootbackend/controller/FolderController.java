package ru.mirea.study.springbootbackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mirea.study.springbootbackend.exception.ResourceNotFoundException;
import ru.mirea.study.springbootbackend.model.Folder;
import ru.mirea.study.springbootbackend.repository.FolderRepository;
import ru.mirea.study.springbootbackend.repository.UserRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class FolderController {

    @Autowired
    private FolderRepository folderRepository;

    @Autowired
    private UserRepository userRepository;

    // get all folders
    @GetMapping("/folders")
    public List<Folder> getAllFolders() {
        return folderRepository.findAll();
    }

    // get folders by user id
    @GetMapping("/users/{id}/folders")
    public ResponseEntity<List<Folder>> getFoldersByUserId(@PathVariable(value = "id") Long userId) throws ResourceNotFoundException {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("Folders not found for this user id: " + userId);
        }

        List<Folder> folders = folderRepository.findByUserId(userId);
        return ResponseEntity.ok().body(folders);
    }

    // get folder by id
    @GetMapping("/folders/{id}")
    public ResponseEntity<Folder> getFolderById(@PathVariable(value = "id") Long id)
            throws ResourceNotFoundException {
        Folder folder = folderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Folder not exist with id: " + id));
        return ResponseEntity.ok().body(folder);
    }

    @PostMapping("/users/{id}/folders")
    public ResponseEntity<Folder> createFolder(@PathVariable(value = "id") Long userId,
                                   @RequestBody Folder folderRequest) throws ResourceNotFoundException {
        Folder folder = userRepository.findById(userId).map(user -> {
            folderRequest.setUser(user);
            return folderRepository.save(folderRequest);
        }).orElseThrow(() -> new ResourceNotFoundException("User not found for this id: " + userId));
        return ResponseEntity.ok().body(folder);
    }

    @PutMapping("/folders/{id}")
    public ResponseEntity<Folder> updateFolder(@PathVariable Long id, @RequestBody Folder folderDetails) throws ResourceNotFoundException {
        Folder folder = folderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Folder not exist with id: " + id));

        folder.setTitle(folderDetails.getTitle());

        Folder updatedFolder = folderRepository.save(folder);
        return ResponseEntity.ok(updatedFolder);
    }

    // delete folder rest api
    @DeleteMapping("/folders/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteFolder(@PathVariable Long id) throws ResourceNotFoundException {
        Folder folder = folderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Folder not exist with id: " + id));

        folderRepository.delete(folder);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }
}
