package ru.mirea.study.springbootbackend.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.mirea.study.springbootbackend.model.Folder;

import java.util.List;

@Repository
public interface FolderRepository extends JpaRepository<Folder, Long> {
    List<Folder> findByTitle(String title);

    List<Folder> findByUserId(Long userId);
}
