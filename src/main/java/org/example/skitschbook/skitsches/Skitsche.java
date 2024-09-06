package org.example.skitschbook.skitsches;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.skitschbook.users.Users;
import org.hibernate.annotations.ColumnDefault;
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

    // 사용자 범위
    // 기본값 true : 누구나 할 수 있게 하기
    // false : 로그인한 사용자만 할 수 있게 하기
    @ColumnDefault("true")
    private boolean isLogined;

    @ManyToOne
    @JoinColumn(name = "userId")
    Users users; // 작성 유저

    public void updateIsLogined(boolean isLogined) {
        this.isLogined = isLogined;
    }

}
