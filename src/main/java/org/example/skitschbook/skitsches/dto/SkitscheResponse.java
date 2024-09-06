package org.example.skitschbook.skitsches.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
public class SkitscheResponse {
    private String fileId;
    private String fileName;
    private byte[] fileData;
}
