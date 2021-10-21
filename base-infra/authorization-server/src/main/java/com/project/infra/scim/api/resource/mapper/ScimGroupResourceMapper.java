package com.project.infra.scim.api.resource.mapper;

import com.project.infra.scim.api.resource.ScimGroupResource;
import com.project.infra.scim.api.resource.ScimMetaResource;
import com.project.infra.scim.api.resource.ScimRefResource;
import com.project.infra.scim.model.ScimGroupEntity;
import com.project.infra.scim.model.ScimUserEntity;
import com.project.infra.scim.model.ScimUserGroupEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.stream.Collectors;

import static com.project.infra.scim.api.ScimUserRestController.USER_ENDPOINT;
import static java.util.Collections.emptySet;

@Component
public class ScimGroupResourceMapper {

    public ScimGroupResource mapEntityToResource(ScimGroupEntity scimGroupEntity, String location) {
        return new ScimGroupResource(new
                ScimMetaResource("Group", scimGroupEntity.getCreatedDate(),
                scimGroupEntity.getLastModifiedDate(),
                scimGroupEntity.getVersion().toString(), location), scimGroupEntity.getIdentifier(),
                scimGroupEntity.getExternalId(), scimGroupEntity.getDisplayName(),
                scimGroupEntity.getMembers() != null ?
                        scimGroupEntity.getMembers()
                                .stream().map(uge -> {
                                    URI userLocation =
                                            ServletUriComponentsBuilder.fromCurrentContextPath()
                                                    .path(USER_ENDPOINT + "/{userId}")
                                                    .buildAndExpand(uge.getUser().getIdentifier())
                                                    .toUri();
                                    return new ScimRefResource(uge.getUser().getIdentifier(), userLocation, uge.getUser().getDisplayName());
                                }).collect(Collectors.toSet()) : emptySet());
    }

    public ScimGroupEntity mapResourceToEntity(ScimGroupResource scimGroupResource) {
        return new ScimGroupEntity(scimGroupResource.getIdentifier(), scimGroupResource.getExternalId(),
                scimGroupResource.getDisplayName(),
                scimGroupResource.getMembers() != null ?
                        scimGroupResource.getMembers().stream()
                                .map(gref -> new ScimUserGroupEntity(new ScimUserEntity(gref.getValue()),
                                        new ScimGroupEntity(scimGroupResource.getIdentifier(), scimGroupResource.getExternalId(),
                                                scimGroupResource.getDisplayName(), null)))
                                .collect(Collectors.toSet()) : emptySet());
    }

}
