package com.github.riafka.graduation.repository;

import com.github.riafka.graduation.model.User;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
public interface UserRepository extends BaseRepository<User> {

    @Query("SELECT u FROM User u WHERE u.email = LOWER(:email)")
    @Cacheable("users")
    Optional<User> findByEmailIgnoreCase(String email);

    @Override
    @Transactional
    @CachePut(value = "users", key = "#user.email")
    User save(User user);

    @Override
    @Transactional
    @CacheEvict(value = "users", key = "#user.email")
    void delete(User user);

    @Override
    @Transactional
    @CacheEvict(value = "users", allEntries = true)
    void deleteById(Integer integer);

    @Override
    @Transactional
    @CacheEvict(value = "users", allEntries = true)
    default void deleteExisted(int id) {
        BaseRepository.super.deleteExisted(id);
    }
}
