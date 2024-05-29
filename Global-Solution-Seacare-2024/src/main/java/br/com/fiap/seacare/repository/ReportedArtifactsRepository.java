package br.com.fiap.seacare.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.seacare.model.ReportedArtifacts;

public interface ReportedArtifactsRepository extends JpaRepository<ReportedArtifacts, Long>{
    
}
