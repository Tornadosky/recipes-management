package dev.gigadev.recipes.repository;

import dev.gigadev.recipes.model.ERole;
import dev.gigadev.recipes.model.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RoleRepository extends MongoRepository<Role, String> {
    Optional<Role> findByName(ERole name);
}