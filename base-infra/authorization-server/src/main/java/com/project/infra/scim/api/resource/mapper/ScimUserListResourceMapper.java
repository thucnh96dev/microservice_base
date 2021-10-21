package com.project.infra.scim.api.resource.mapper;

import com.project.infra.scim.api.resource.ScimMetaResource;
import com.project.infra.scim.api.resource.ScimUserListResource;
import com.project.infra.scim.model.ScimUserEntity;
import org.springframework.stereotype.Component;

@Component
public class ScimUserListResourceMapper {

    public ScimUserListResource mapEntityToResource(ScimUserEntity scimUserEntity, String location) {
        return new ScimUserListResource(
                new ScimMetaResource("User",
                        scimUserEntity.getCreatedDate(),
                        scimUserEntity.getLastModifiedDate(),
                        scimUserEntity.getVersion().toString(), location),
                scimUserEntity.getIdentifier(), scimUserEntity.getExternalId(), scimUserEntity.getUserName(),
                scimUserEntity.getFamilyName(), scimUserEntity.getGivenName(), scimUserEntity.getMiddleName(),
                scimUserEntity.getHonorificPrefix(), scimUserEntity.getHonorificSuffix(), scimUserEntity.getNickName(),
                scimUserEntity.getProfileUrl(), scimUserEntity.getTitle(), scimUserEntity.getUserType(),
                scimUserEntity.getPreferredLanguage(), scimUserEntity.getLocale(), scimUserEntity.getTimezone(),
                scimUserEntity.isActive());
    }
}
