package org.example.skitschbook.global.dto;

import lombok.*;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Getter
public class StatusResponse {
    private int status;
    private String message;
}
