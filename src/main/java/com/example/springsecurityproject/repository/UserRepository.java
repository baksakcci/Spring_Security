package com.example.springsecurityproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.springsecurityproject.model.User;

// CRUD 함수를 JPARepository가 기본적으로 가지고 있다.
// @Repository라는 어노테이션이 없어도 IoC가 된다. 상속받았기 때문.
public interface UserRepository extends JpaRepository<User, Integer> {
    // select * from user where username = ?
    public User findByUsername(String username);
}