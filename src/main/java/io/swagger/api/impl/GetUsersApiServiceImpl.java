package io.swagger.api.impl;

import io.swagger.api.ApiResponseMessage;
import io.swagger.api.GetUsersApiService;
import io.swagger.api.NotFoundException;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2020-06-23T15:45:21.078Z")
public class GetUsersApiServiceImpl extends GetUsersApiService
{
    @Override
    public Response getUsers(SecurityContext securityContext) throws NotFoundException
    {
        // do some magic!
        return Response.ok().entity(new ApiResponseMessage(ApiResponseMessage.OK, "magic!")).build();
    }
}
