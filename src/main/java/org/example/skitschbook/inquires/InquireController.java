package org.example.skitschbook.inquires;

import lombok.RequiredArgsConstructor;
import org.example.skitschbook.inquires.dto.InquireContent;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/inquire")
@RequiredArgsConstructor
public class InquireController {

    private final InquireService inquireService;

    @PostMapping("/save")
    public ResponseEntity<?> saveFile(@RequestParam("content") String content,
                                      @RequestParam("email") String email,
                                      @RequestParam("file") MultipartFile file) throws IOException {
        return inquireService.saveFile(content, email, file);
    }
}
