package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CancelDTO {
    private String trainId;
    private String passId;
    private String from;
    private String to;
    private String berth;
    private String emailId;
}
