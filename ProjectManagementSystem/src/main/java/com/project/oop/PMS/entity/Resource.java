package com.project.oop.PMS.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity
@Table(name = "resources")
public class Resource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer resourceId;

    @ManyToOne
    @JoinColumn(name = "project_id", referencedColumnName = "id", nullable = false)
    private Project project;

    private String licenceKey;
    private String name;
    private String type;
    private Integer capacity;
    private BigDecimal totalMoney;
    private String newAttribute;

    @Temporal(TemporalType.DATE)
    private Date expirationDate;
}
