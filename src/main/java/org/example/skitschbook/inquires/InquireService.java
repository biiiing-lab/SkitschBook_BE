package org.example.skitschbook.inquires;

import lombok.RequiredArgsConstructor;
import org.example.skitschbook.global.dto.StatusResponse;
import org.example.skitschbook.inquires.dto.InquireContent;
import org.example.skitschbook.users.UserRepository;
import org.example.skitschbook.users.Users;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InquireService {

    //  todo 재설정
    private final String FOLDER_PATH = "D:\\skitsche_inquire\\";
    private final UserRepository userRepository;
    private final InquireRepository inquireRepository;

    public ResponseEntity<StatusResponse> saveFile(String content, String email, MultipartFile file) throws IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        Users user = userRepository.findById(Long.valueOf(userDetails.getUsername())).orElseThrow();

        Inquires inquires;

        if(!file.isEmpty()) {
            String filepath = FOLDER_PATH + file.getOriginalFilename();
            String filename =  file.getOriginalFilename();

            // 파일 저장
            String uniqueFilename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            File dest = new File(FOLDER_PATH + uniqueFilename);
            file.transferTo(dest);

            inquires = Inquires.builder()
                    .content(content)
                    .email(email)
                    .filename(filename)
                    .filepath(filepath)
                    .userId(user)
                    .build();
        } else {
            inquires = Inquires.builder()
                    .content(content)
                    .email(email)
                    .filename(null)
                    .filepath(null)
                    .userId(user)
                    .build();
        }

        inquireRepository.save(inquires);

        return  ResponseEntity.ok(new StatusResponse(HttpStatus.OK.value(), "문의사항 저장 성공"));
    }
}
