package com.sparta.trelloproject.domain.card.repository;

import com.sparta.trelloproject.domain.card.entity.Manager;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ManagerRepository extends JpaRepository<Manager, Long> {

    boolean existsByUserIdAndCardId(Long id, Long id1);
    List<Manager> findManagersByCard_Id(Long id);

}
