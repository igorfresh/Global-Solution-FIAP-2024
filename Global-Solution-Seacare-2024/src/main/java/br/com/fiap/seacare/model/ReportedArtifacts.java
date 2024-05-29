package br.com.fiap.seacare.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class ReportedArtifacts {
    
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Report report;

    private Artifact artifact;
}
