package br.com.fiap.seacare.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.seacare.model.UserSC;

public interface UserSCRepository extends JpaRepository<UserSC, Long>{
    
}
