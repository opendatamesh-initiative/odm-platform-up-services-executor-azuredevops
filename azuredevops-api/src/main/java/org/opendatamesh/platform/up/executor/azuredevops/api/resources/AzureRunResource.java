package org.opendatamesh.platform.up.executor.azuredevops.api.resources;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AzureRunResource {
    @JsonProperty("id")
    Long runId;

    @JsonProperty("state")
    AzureRunState state;
}
