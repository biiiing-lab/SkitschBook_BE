package org.example.skitschbook.skitsches;

import jakarta.transaction.Status;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.skitschbook.global.dto.StatusResponse;
import org.example.skitschbook.skitsches.dto.SkitscheResponse;
import org.example.skitschbook.users.dto.IsLogined;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/skitsche")
@RequiredArgsConstructor
@Slf4j
public class SkitscheController {

    private final SkitscheService skitscheService;
    private final RedisTemplate<String, Object> redisTemplate;
    private final SimpMessagingTemplate messagingTemplate;

    // 원본 파일 저장 + 상태를 Redis에 저장, WebSocket을 통해 실시간 알림 전송
    @PostMapping("/save")
    public ResponseEntity<StatusResponse> save(@RequestParam("file") MultipartFile file, IsLogined isLogined) throws Exception {
        ResponseEntity<SkitscheResponse> response = skitscheService.saveOriginalImage(file, isLogined);

        // Redis에 저장
        String key = "file:" + response.getBody().getFileId();
        redisTemplate.opsForValue().set(key, response);

        // WebSocket을 통해 상태 전송
        messagingTemplate.convertAndSend("/topic/file-status", response);

        return ResponseEntity.ok(new StatusResponse(HttpStatus.OK.value(), "원본 사진 저장 완료"));
    }

    // 파일 다운로드
    // todo 파일명이 아닌 파일 고유 번호로 작동할 수 있도록 하기
    @GetMapping("/download/{skitscheId}")
    public ResponseEntity<?> download(@PathVariable("skitscheId") String fileId) throws IOException {
        log.info("스키치 다운로드 컨트롤러 작동");

        // Redis에서 파일 상태 조회
        String key = "file:" + fileId;
        SkitscheResponse fileStatus = (SkitscheResponse) redisTemplate.opsForValue().get(key);

        if (fileStatus == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File not found");
        }

        byte[] downloadFile = skitscheService.download(fileStatus.getFileId());

        // 다운로드 상태를 WebSocket으로 전송
        messagingTemplate.convertAndSend("/topic/file-status", fileStatus);

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(downloadFile);
    }


    // 스키치 세부 조회
    @GetMapping("/view/{file}")
    public ResponseEntity<?> view(@PathVariable("file") String file) throws IOException {
        log.info("스키치 조회 작동");
        String path = "D:\\skitsche_file\\";
        Resource resource = new FileSystemResource(path + file);
        return new ResponseEntity<>(resource, HttpStatus.OK);
    }

    // 파일 상태를 실시간으로 조회 (Redis 캐시)
    @GetMapping("/status/{fileId}")
    public ResponseEntity<StatusResponse> getFileStatus(@PathVariable("fileId") String fileId) {
        String key = "file:" + fileId;
        StatusResponse fileStatus = (StatusResponse) redisTemplate.opsForValue().get(key);

        if (fileStatus == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity.ok(fileStatus);
    }

    // 모든 파일 조회 (Redis 캐시 활용)
    @GetMapping("/all")
    public ResponseEntity<List<SkitscheResponse>> getAllFiles() throws IOException {
        List<SkitscheResponse> allFiles = skitscheService.getAllFilesFromRedis(redisTemplate);
        return ResponseEntity.ok(allFiles);
    }

    // WebSocket 연결 테스트
    @MessageMapping("/file-status")
    @SendTo("/topic/file-status")
    public StatusResponse sendStatus(StatusResponse status) {
        return status;
    }

}
