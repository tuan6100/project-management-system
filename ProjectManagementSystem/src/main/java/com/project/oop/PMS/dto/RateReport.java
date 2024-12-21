package com.project.oop.PMS.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RateReport {
    private Integer id;
    private String name;
    private Integer count;
    private double rate;
}
