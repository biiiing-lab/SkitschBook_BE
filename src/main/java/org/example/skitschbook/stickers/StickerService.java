package org.example.skitschbook.stickers;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StickerService {
    private final AmazonS3 amazonS3;
    private final StickerRepository stickerRepository;
    private final String BUCKET = ""; // todo s3 bucket name

    public void saveSticker(MultipartFile file, String name) throws IOException {
        String filename = UUID.randomUUID() + "-" + file.getOriginalFilename();
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());

        // 파일을 S3에 업로드
        amazonS3.putObject(new PutObjectRequest(BUCKET, filename, file.getInputStream(), metadata)
                .withCannedAcl(CannedAccessControlList.PublicRead));

        // 파일 URL 생성
        String fileUrl = amazonS3.getUrl(BUCKET, filename).toString();

        Stickers stickers = Stickers.builder()
                .name(name)
                .imageUrl(fileUrl)
                .build();

        stickerRepository.save(stickers);
    }
}
