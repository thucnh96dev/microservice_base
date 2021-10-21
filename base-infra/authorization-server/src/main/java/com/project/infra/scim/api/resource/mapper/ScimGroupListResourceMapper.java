package com.project.infra.scim.api.resource.mapper;

import com.project.infra.scim.api.resource.ScimGroupListResource;
import com.project.infra.scim.api.resource.ScimMetaResource;
import com.project.infra.scim.model.ScimGroupEntity;
import org.springframework.stereotype.Component;

@Component
public class ScimGroupListResourceMapper {

    public ScimGroupListResource mapEntityToResource(ScimGroupEntity scimGroupEntity, String location) {
        return new ScimGroupListResource(new
                ScimMetaResource("Group", scimGroupEntity.getCreatedDate(),
                scimGroupEntity.getLastModifiedDate(),
                scimGroupEntity.getVersion().toString(), location), scimGroupEntity.getIdentifier(),
                scimGroupEntity.getExternalId(), scimGroupEntity.getDisplayName());
    }

}
