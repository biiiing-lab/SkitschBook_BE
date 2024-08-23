package org.example.skitschbook.inquires.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@AllArgsConstructor
public class InquireContent {
    private String content;
    private String email;
    private MultipartFile file;
}
