package org.example.skitschbook.inquires;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.skitschbook.users.Users;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Inquires {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long inquireId;

    private String content;
    private String filename;
    private String filepath;
    private String email;

    @ManyToOne
    @JoinColumn(name = "userId")
    private Users userId;


}
