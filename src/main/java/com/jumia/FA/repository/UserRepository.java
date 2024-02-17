package com.jumia.FA.repository;

import com.jumia.FA.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

   User findByEmail(String email);
}
