package org.example.skitschbook.skitsches;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.skitschbook.global.dto.StatusResponse;
import org.example.skitschbook.skitsches.dto.SkitscheReqeust;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/skitsche")
@RequiredArgsConstructor
public class SkitscheController {

    private final SkitscheService skitscheService;

    // 파일 실시간 저장
    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody SkitscheReqeust skitscheReqeust) throws Exception {
        try {
            skitscheService.save(skitscheReqeust);
            return ResponseEntity.ok().body("실시간 저장 성공");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("저장 실패: " + e.getMessage());
        }
    }

    // 파일 다운로드
    // todo 파일명이 아닌 파일 고유 번호로 작동할 수 있도록 하기
    @GetMapping("/download/{file}")
    public ResponseEntity<?> download(@PathVariable("file") String filename) throws IOException {
        byte[] downloadFile = skitscheService.download(filename);

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(downloadFile);
    }

    // 스키치 세부 조회
    @GetMapping("/view/{file}")
    public ResponseEntity<?> view(@PathVariable("file") String file) throws IOException {
        String path = "D:\\skitsche_file\\";
        Resource resource = new FileSystemResource(path + file);
        return new ResponseEntity<>(resource, HttpStatus.OK);
    }

    // 파일 전체 조회 (마이페이지)
    @GetMapping("/all")
    public ResponseEntity<List<byte[]>> getAll() throws IOException {
        List<byte[]> skitsches = skitscheService.getAll();
        return ResponseEntity.ok(skitsches);
    }

}
