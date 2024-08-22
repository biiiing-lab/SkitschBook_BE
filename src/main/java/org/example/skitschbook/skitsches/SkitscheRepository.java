package org.example.skitschbook.skitsches;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SkitscheRepository extends JpaRepository<Skitsche, Long> {
    Optional<Skitsche> findByFilename(String filename);
    List<Skitsche> findAll();
}
