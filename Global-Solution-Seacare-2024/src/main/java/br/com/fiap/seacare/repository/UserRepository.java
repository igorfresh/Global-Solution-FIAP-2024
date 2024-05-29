package br.com.fiap.seacare.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.seacare.model.User;

public interface UserRepository extends JpaRepository<User, Long>{
    
}
