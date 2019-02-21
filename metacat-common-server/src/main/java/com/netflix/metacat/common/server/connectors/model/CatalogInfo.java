package com.netflix.metacat.common.server.connectors.model;


import com.netflix.metacat.common.QualifiedName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public final class CatalogInfo extends BaseInfo {
    private String account;
    private String region;
    private String env;
    private String clusterName;

    /**
     *
     */
    @Builder
    private CatalogInfo(
        final QualifiedName name,
        final AuditInfo auditInfo,
        final Map<String, String> metadata,
        final String account,
        final String region,
        final String env,
        final String clusterName
    ) {
        super(name, auditInfo, metadata);
        this.account = account;
        this.region = region;
        this.env = env;
        this.clusterName = clusterName;
    }
}
