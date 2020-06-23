package io.swagger.api;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2020-06-23T15:45:21.078Z")
public abstract class GetUsersApiService
{
    public abstract Response getUsers(SecurityContext securityContext) throws NotFoundException;
}
