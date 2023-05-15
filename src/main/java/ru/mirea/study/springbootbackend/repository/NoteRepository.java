package ru.mirea.study.springbootbackend.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.mirea.study.springbootbackend.model.Note;

import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findByFolderId(Long folderId);

    @Transactional
    void deleteByFolderId(long folderId);
}
