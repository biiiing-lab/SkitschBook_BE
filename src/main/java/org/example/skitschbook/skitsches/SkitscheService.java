package org.example.skitschbook.skitsches;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.example.skitschbook.global.Utils.ImageUtils;
import org.example.skitschbook.global.dto.StatusResponse;
import org.example.skitschbook.skitsches.dto.SkitscheResponse;
import org.example.skitschbook.users.UserRepository;
import org.example.skitschbook.users.Users;
import org.example.skitschbook.users.dto.IsLogined;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SkitscheService {

    //  todo 재설정
    private final String FOLDER_PATH = "D:\\skitsche_file\\";

    private final SkitscheRepository skitscheRepository;
    private final UserRepository userRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    //  원본 파일 저장
    public ResponseEntity<SkitscheResponse> saveOriginalImage(MultipartFile file, IsLogined isLogined) throws IOException {
        String filepath = FOLDER_PATH + file.getOriginalFilename();
        String filename = file.getOriginalFilename();

        File dest = new File(filepath);
        file.transferTo(dest);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        Users user = userRepository.findById(Long.valueOf(userDetails.getUsername())).orElseThrow();

        Skitsche skitsche = Skitsche.builder()
                .filepath(filepath)
                .filename(filename)
                .users(user)
                .isLogined(isLogined.isLogin())
                .build();

        if (skitsche == null) {
            return ResponseEntity.ok(new SkitscheResponse("실패", null, null));
        }

        skitscheRepository.save(skitsche);

        return  ResponseEntity.ok(new SkitscheResponse("성공", skitsche.getSkitscheId().toString(), null));
    }

    public ResponseEntity<StatusResponse> saveFinalImage(String canvasData, Long skitscheId) throws IOException {
        Skitsche skitsche = skitscheRepository.findById(skitscheId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 스키치입니다."));

        // 원본 이미지 파일 경로
        String filepath = skitsche.getFilepath();

        // 캔버스 데이터를 이미지 파일로 변환하여 기존 이미지 위에 덮어씌우기
        BufferedImage originalImage = ImageIO.read(new File(filepath));
        BufferedImage finalImage = overlayCanvasDataOntoImage(originalImage, canvasData);

        // 덮어쓰기
        File outputFile = new File(filepath);
        ImageIO.write(finalImage, "png", outputFile);

        return ResponseEntity.ok(new StatusResponse(HttpStatus.OK.value(), "최종 스키치 저장 성공"));
    }

    // 캔버스 데이터를 원본 이미지에 덮어씌우는 메서드
    private BufferedImage overlayCanvasDataOntoImage(BufferedImage originalImage, String canvasData) throws IOException {
        // 여기서는 canvasData(JSON 문자열 형태)를 BufferedImage로 변환하고
        // originalImage 위에 그리는 로직을 구현해야 합니다.
        // 예를 들어 fabric.js의 데이터를 기반으로 이미지를 그릴 수 있는 로직을 추가하세요.

        // canvasData를 처리하고, 이미지로 변환
        BufferedImage canvasImage = convertCanvasDataToImage(canvasData);

        // 원본 이미지에 캔버스 데이터를 덮어씌우기
        Graphics2D g2d = originalImage.createGraphics();
        g2d.drawImage(canvasImage, 0, 0, null);
        g2d.dispose();

        return originalImage;
    }

    // 캔버스 데이터를 BufferedImage로 변환하는 메서드
    private BufferedImage convertCanvasDataToImage(String canvasData) {
        // 캔버스 데이터를 이미지로 변환하는 로직 구현
        // 예: JSON 데이터를 바탕으로 이미지를 생성
        // 이 부분은 프론트엔드에서 fabric.js에서 넘겨준 데이터를 처리하는 로직에 맞게 구현해야 함
        return null; // 예시로 null 반환, 실제 구현 필요
    }


    public byte[] download(String fileId) throws IOException {
        log.info("스키치 다운로드 서비스 작동");
        Skitsche skitsche = skitscheRepository.findById(Long.valueOf(fileId)).orElseThrow();
        String filepath = skitsche.getFilepath();
        log.info("스키치 다운로드 서비스 작동 완료");
        return Files.readAllBytes(new File(filepath).toPath());
    }

    public List<SkitscheResponse> getAllFilesFromRedis(RedisTemplate<String, Object> redisTemplate) throws IOException {
        log.info("Redis를 사용한 스키치 파일 모두 가져오기 작동");

        // Redis에서 Object로 데이터를 읽어오기
        List<Object> fileIds = redisTemplate.opsForList().range("file:all", 0, -1);
        List<SkitscheResponse> responses = new ArrayList<>();

        // Redis에 캐시된 파일이 없다면 DB에서 조회 후 Redis에 캐싱
        if (fileIds == null || fileIds.isEmpty()) {
            log.info("Redis에 파일 정보가 없어 DB에서 조회 중...");
            List<Skitsche> skitscheList = skitscheRepository.findAll();

            for (Skitsche skitsche : skitscheList) {
                byte[] imageData = Files.readAllBytes(new File(skitsche.getFilepath()).toPath());
                String fileId = String.valueOf(skitsche.getSkitscheId());

                // Redis에 파일 정보 저장 (메타데이터만 저장)
                redisTemplate.opsForList().rightPush("file:all", fileId);
                redisTemplate.opsForValue().set("file:" + fileId, skitsche);

                // Convert to StatusResponse
                SkitscheResponse response = new SkitscheResponse(fileId, skitsche.getFilename(), imageData);
                responses.add(response);
            }
        } else {
            // Redis에서 파일 정보를 가져와 파일 데이터를 읽어옴
            for (Object fileId : fileIds) {
                Skitsche skitsche = (Skitsche) redisTemplate.opsForValue().get("file:" + fileId);
                if (skitsche != null) {
                    byte[] imageData = Files.readAllBytes(new File(skitsche.getFilepath()).toPath());
                    // Convert to StatusResponse
                    SkitscheResponse response = new SkitscheResponse(String.valueOf(fileId), skitsche.getFilename(), imageData);
                    responses.add(response);
                }
            }
        }

        return responses;
    }


}
