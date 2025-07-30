package com.example.Login.repository;

import com.example.Login.model.Maintain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface S_MaintainRepository extends JpaRepository<Maintain, String> {
}
