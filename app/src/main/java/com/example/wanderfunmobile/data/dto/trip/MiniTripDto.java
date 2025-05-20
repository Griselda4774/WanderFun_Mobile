package com.example.wanderfunmobile.data.dto.trip;

import java.time.LocalDate;

public class MiniTripDto {
    private Long id;
    private String name;
    private LocalDate startTime;
    private LocalDate endTime;
    private MiniUserDto user;

    public MiniTripDto() {
    }
}
