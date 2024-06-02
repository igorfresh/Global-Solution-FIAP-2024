package br.com.fiap.seacare.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Location {
    
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String uf;

    private String city;

    private String referencePoint;

    public Location(Long id) {
        this.id = id;
    }
}
