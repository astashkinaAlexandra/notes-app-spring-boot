package ru.mirea.study.springbootbackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.mirea.study.springbootbackend.repository.FolderRepository;
import ru.mirea.study.springbootbackend.repository.NoteRepository;
import ru.mirea.study.springbootbackend.repository.UserRepository;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    FolderRepository folderRepository;
    @Autowired
    NoteRepository noteRepository;

    @GetMapping("/numberOfUsers")
    public Integer getNumberOfUsers() {
        return userRepository.findAll().size();
    }

    @GetMapping("/numberOfFolders")
    public Integer getNumberOfFolders() {
        return folderRepository.findAll().size();
    }

    @GetMapping("/numberOfNotes")
    public Integer getNumberOfNotes() {
        return noteRepository.findAll().size();
    }
}
