package br.com.fiap.seacare.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Report {
    
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private String description;

    private LocalDate date;

    @ManyToOne
    private UserSC userSc;

    @ManyToOne
    private Location location;
    
    public Report(Long id) {
        this.id = id;
    }
}
