package com.puppet.frontendpracticeservice.repository;

import com.puppet.frontendpracticeservice.domain.entity.Requisites;
import com.puppet.frontendpracticeservice.domain.request.RequisitesDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RequisitesRepository extends JpaRepository<Requisites, UUID> {

    @Query(value = """
            SELECT new com.puppet.frontendpracticeservice.domain.request.RequisitesDto(
            u.name, u.surname, r.currentAccount, r.kbk)
            FROM Requisites r
            JOIN r.user u
            WHERE u.id =:id
            """)
    Optional<RequisitesDto> findRequisitesDtoById(UUID id);
}