package com.daniilSergachev.CloudFileStorage.repositories.user;

import com.daniilSergachev.CloudFileStorage.model.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends CrudRepository<Role, Integer> {

    Optional<Role> findByName(String name);
}