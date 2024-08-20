package org.example.skitschbook.skitsches;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.skitschbook.users.Users;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Skitsche {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long skitscheId;

    private String filename; // file 이름
    private String filepath; // file 경로

    @CreationTimestamp
    private LocalDateTime createdAt; // 생성 날짜

    @Lob
    private byte[] fileData;

    @ManyToOne
    @JoinColumn(name = "userId")
    Users users; // 작성 유저

}
