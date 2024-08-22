package org.example.skitschbook.skitsches;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.skitschbook.global.dto.StatusResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/skitsche")
@RequiredArgsConstructor
@Slf4j
public class SkitscheController {

    private final SkitscheService skitscheService;

    // 파일 저장
    @PostMapping("/save")
    public ResponseEntity<StatusResponse> save(@RequestParam("file") MultipartFile file) throws Exception {
        return skitscheService.save(file);
    }

    // 파일 다운로드
    @GetMapping("/{file}")
    public ResponseEntity<?> download(@PathVariable("file") String filename) throws IOException {
        log.info("스키치 다운로드 컨트롤러 작동");
        byte[] downloadFile = skitscheService.download(filename);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(downloadFile);
    }

    // 파일 세부 조회

    // 파일 전체 조회 (마이페이지)
}
