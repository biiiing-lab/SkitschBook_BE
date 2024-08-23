package org.example.skitschbook.stickers;

import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/sticker")
@RequiredArgsConstructor
public class StickerController {
    private final StickerService stickerService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadSticker(@RequestParam("file") MultipartFile file, @RequestParam("name") String name) {
        try {
            stickerService.saveSticker(file, name);
            return ResponseEntity.ok().body("파일 업로드 성공");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("파일 업로드 실패");
        }
    }
}
