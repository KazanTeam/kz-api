package com.kazan.repository.jpa;

import com.kazan.model.KazanUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface KazanUserRepository extends JpaRepository<KazanUser, Integer> {

    @Query("Select kazanUser.password from KazanUser kazanUser where kazanUser.userId = ?1 ")
    String findPasswordByMemberId(Integer userId);

}
