package com.project.infra.scim.dao;

import com.project.infra.scim.model.ScimGroupEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ScimGroupEntityRepository extends JpaRepository<ScimGroupEntity, Long> {

    @EntityGraph(attributePaths = {"members", "members.user", "members.group"})
    Optional<ScimGroupEntity> findOneByIdentifier(UUID identifier);

    void deleteOneByIdentifier(UUID identifier);
}
