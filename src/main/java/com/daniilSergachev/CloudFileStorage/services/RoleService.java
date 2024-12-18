package com.daniilSergachev.CloudFileStorage.services;

import com.daniilSergachev.CloudFileStorage.model.Role;
import com.daniilSergachev.CloudFileStorage.repositories.user.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    public Role getUserRole() {
        return roleRepository.findByName("ROLE_USER").get();
    }
}