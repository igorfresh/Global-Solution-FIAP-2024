package br.com.fiap.seacare.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Location {
    
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String UF;

    private String city;

    private String referencePoint;
}