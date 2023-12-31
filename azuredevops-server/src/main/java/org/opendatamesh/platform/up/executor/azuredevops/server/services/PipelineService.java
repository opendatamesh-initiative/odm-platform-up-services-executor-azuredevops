package org.opendatamesh.platform.up.executor.azuredevops.server.services;

import org.opendatamesh.platform.core.commons.servers.exceptions.InternalServerException;
import org.opendatamesh.platform.core.commons.servers.exceptions.UnprocessableEntityException;
import org.opendatamesh.platform.up.executor.api.resources.ExecutorApiStandardErrors;
import org.opendatamesh.platform.up.executor.azuredevops.api.clients.AzureDevOpsClient;
import org.opendatamesh.platform.up.executor.azuredevops.api.resources.PipelineResource;
import org.opendatamesh.platform.up.executor.azuredevops.server.mappers.PipelineMapper;
import org.opendatamesh.platform.up.executor.azuredevops.server.resources.odm.ConfigurationsResource;
import org.opendatamesh.platform.up.executor.azuredevops.server.resources.odm.TemplateResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PipelineService {

    @Autowired
    protected PipelineMapper pipelineMapper;

    @Autowired
    protected AzureDevOpsClient azureDevOpsClient;

    @Autowired
    protected ParameterService parameterService;

    private static final Logger logger = LoggerFactory.getLogger(PipelineService.class);

    public String runPipeline(
            ConfigurationsResource configurationsResource, TemplateResource templateResource, String callbackRef
    ) {

        configurationsResource = addParamsFromContext(configurationsResource);

        PipelineResource pipelineResource = pipelineMapper.toAzurePipelineResource(
                configurationsResource, templateResource, callbackRef
        );

        if(
                templateResource.getOrganization() == null ||
                templateResource.getProject() == null ||
                templateResource.getPipelineId() == null
        )
            throw new UnprocessableEntityException(
                    ExecutorApiStandardErrors.SC422_05_TASK_IS_INVALID,
                    "Impossible to send request to Azure DevOps: "
                            + "you must specify all parameters in template (organization, project and pipelineId)"
            );

        logger.info("Calling AzureDevOps to run the pipeline ...");

        ResponseEntity<String> azureRespone = azureDevOpsClient.runPipeline(
                pipelineResource,
                templateResource.getOrganization(),
                templateResource.getProject(),
                templateResource.getPipelineId()
        );
        String azureResponseBody = azureRespone.getBody();

        if(!azureRespone.getStatusCode().is2xxSuccessful()){
            throw new InternalServerException(
                    ExecutorApiStandardErrors.SC502_50_REGISTRY_SERVICE_ERROR,
                    "Azure DevOps responded with an internal server error: " + azureResponseBody
            );
        } else {
            logger.info("Pipeline run ended successfully");
            return azureResponseBody;
        }

    }

    private ConfigurationsResource addParamsFromContext(ConfigurationsResource configurationsResource) {

        if(configurationsResource.getContext() != null && configurationsResource.getParams() != null) {
            Map<String, String> params = parameterService.extractParamsFromContext(
                    configurationsResource.getParams(),
                    configurationsResource.getContext()
            );
            configurationsResource.setParams(params);
        }

        configurationsResource.setContext(null);

        return configurationsResource;
    }

}
