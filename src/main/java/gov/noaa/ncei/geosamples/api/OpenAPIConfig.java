package gov.noaa.ncei.geosamples.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import gov.noaa.ncei.geosamples.api.error.ApiError;
import gov.noaa.ncei.geosamples.api.service.BuildInfoService;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.servers.Server;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.springdoc.core.GroupedOpenApi;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

@Configuration
public class OpenAPIConfig {


  private final BuildInfoService buildInfoService;

  @Autowired
  public OpenAPIConfig(BuildInfoService buildInfoService) {
    this.buildInfoService = buildInfoService;
  }

  @Bean
  public GroupedOpenApi imlgsApiGroup(OpenApiCustomiser openApiCustomiser) {
    return GroupedOpenApi.builder().group("imlgs")
        .addOpenApiCustomiser(
            openApi -> openApi.info(
                new Info().title("IMLGS API").description("Read-only access to the Index to Marine and Lacustrine Geological Samples database")
                    .version(buildInfoService.getVersion())))
        .packagesToScan("gov.noaa.ncei.geosamples.api")
        .addOpenApiCustomiser(openApiCustomiser)
        .build();
  }


  @Bean
  public ModelResolver modelResolver(ObjectMapper objectMapper) {
    return new ModelResolver(objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE));
  }

  @Bean
  public OpenApiCustomiser openApiCustomiser(ServiceProperties serviceProperties) {
    return openApi -> {
      Server server = new Server();
      server.setUrl(serviceProperties.getPublicBaseUrl());
      openApi.setServers(Collections.singletonList(server));
      openApi.getComponents().getSchemas().putAll(ModelConverters.getInstance().read(ApiError.class));
      Schema errorResponseSchema = new Schema();
      errorResponseSchema.setName("ApiErrorView");
      errorResponseSchema.set$ref("#/components/schemas/ApiError");
      openApi.getPaths().values().forEach(pathItem -> pathItem.readOperations().forEach(operation -> {
        List<Parameter> parameters = operation.getParameters();
        if(parameters != null) {
          Iterator<Parameter> it = parameters.iterator();
          while (it.hasNext()) {
            Parameter parameter = it.next();
            if(parameter.getName().startsWith("aoi.")) {
              it.remove();
            }
          }
        }
        ApiResponses apiResponses = operation.getResponses();
        apiResponses.addApiResponse("300", createApiResponse(HttpStatus.FORBIDDEN.getReasonPhrase(), errorResponseSchema));
        apiResponses.addApiResponse("400", createApiResponse(HttpStatus.BAD_REQUEST.getReasonPhrase(), errorResponseSchema));
        apiResponses.addApiResponse("500", createApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), errorResponseSchema));
      }));
    };
  }

  private static ApiResponse createApiResponse(String message, Schema schema) {
    MediaType mediaType = new MediaType();
    mediaType.schema(schema);
    return new ApiResponse().description(message)
        .content(new Content().addMediaType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE, mediaType));
  }

}
