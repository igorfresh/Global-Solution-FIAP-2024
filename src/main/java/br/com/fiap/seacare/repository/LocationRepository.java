package br.com.fiap.seacare.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.seacare.model.Location;

public interface LocationRepository extends JpaRepository<Location, Long>{
    
}
