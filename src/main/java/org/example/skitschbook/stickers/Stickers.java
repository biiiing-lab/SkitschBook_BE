package org.example.skitschbook.stickers;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@Builder
@RequiredArgsConstructor
public class Stickers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stikerId;

    private String name;
    private String imageUrl;
}
