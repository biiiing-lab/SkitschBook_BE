package org.example.skitschbook.stickers;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StickerRepository extends JpaRepository<Stickers, Long> {
}
