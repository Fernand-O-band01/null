package com.example.demo.server;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServerRepository extends JpaRepository<Server,Long> {
    List<Server> findAllByMembersId(Integer userId);
}
