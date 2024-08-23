package org.example.skitschbook.skitsches;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.example.skitschbook.global.Utils.ImageUtils;
import org.example.skitschbook.global.dto.StatusResponse;
import org.example.skitschbook.users.UserRepository;
import org.example.skitschbook.users.Users;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SkitscheService {

    //  todo 재설정
    private final String FOLDER_PATH = "D:\\skitsche_file\\";

    private final SkitscheRepository skitscheRepository;
    private final UserRepository userRepository;

    public ResponseEntity<StatusResponse> save(MultipartFile file) throws IOException {
        String filepath = FOLDER_PATH + file.getOriginalFilename();
        String filename = file.getOriginalFilename();

        File dest = new File(filepath);
        file.transferTo(dest);

        log.info("스키치 회원 정보 가져오기");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        Users user = userRepository.findById(Long.valueOf(userDetails.getUsername())).orElseThrow();

        log.info("스키치 빌드");
        Skitsche skitsche = Skitsche.builder()
                .filepath(filepath)
                .filename(filename)
                .users(user)
                .build();

        log.info("스키치 빌드 성공");

        log.info("스키치 비어있는지 확인");
        if (skitsche == null) {
            log.info("imageData: {}", skitsche);
            return ResponseEntity.ok(new StatusResponse(HttpStatus.NO_CONTENT.value(), "스키치 저장 실패"));
        }
        log.info("스키치 비어있는지 확인 완료");

        log.info("스키치 저장");
        skitscheRepository.save(skitsche);

        return  ResponseEntity.ok(new StatusResponse(HttpStatus.OK.value(), "스키치 저장 성공"));
    }

    public byte[] download(String filename) throws IOException {
        log.info("스키치 다운로드 서비스 작동");
        Skitsche skitsche = skitscheRepository.findByFilename(filename).orElseThrow();
        String filepath = skitsche.getFilepath();
        log.info("스키치 다운로드 서비스 작동 완료");
        return Files.readAllBytes(new File(filepath).toPath());
    }

    public List<byte[]> getAll() throws IOException {
        log.info("스키치 모두 가져오기 작동");
        List<Skitsche> skitscheList = skitscheRepository.findAll();
        List<byte[]> files = new ArrayList<>();

        for(Skitsche skitsche : skitscheList) {
            byte[] imageData = Files.readAllBytes(new File(skitsche.getFilepath()).toPath());
            files.add(imageData);
        }
        return files;
    }

}
