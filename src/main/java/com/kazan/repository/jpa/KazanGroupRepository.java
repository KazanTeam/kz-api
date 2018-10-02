package com.kazan.repository.jpa;

import com.kazan.model.KazanGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KazanGroupRepository extends JpaRepository<KazanGroup, Integer> {

    Page<KazanGroup> findByCreator(Integer creator, Pageable pageRequest);

}
