package br.com.fiap.seacare.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.seacare.model.Artifact;

public interface ArtifactRepository extends JpaRepository<Artifact, Long>{
    
}
