package gov.noaa.ncei.geosamples.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import gov.noaa.ncei.geosamples.api.error.ApiError;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.jackson.ModelResolver;
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
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

@Configuration
public class OpenAPIConfig {

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
      errorResponseSchema.set$ref("#/components/schemas/ApiErrorView");
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
        apiResponses.addApiResponse("3xx", createApiResponse(HttpStatus.FORBIDDEN.getReasonPhrase(), errorResponseSchema));
        apiResponses.addApiResponse("4xx", createApiResponse(HttpStatus.BAD_REQUEST.getReasonPhrase(), errorResponseSchema));
        apiResponses.addApiResponse("5xx", createApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), errorResponseSchema));
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
