package io.swagger.api;

import io.swagger.api.factories.GetUsersApiServiceFactory;

import javax.servlet.ServletConfig;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.io.IOException;

@Path("/getUsers")

@Produces({"application/json"})
@io.swagger.annotations.Api(description = "the getUsers API")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2020-06-23T15:45:21.078Z")
public class GetUsersApi
{
    private final GetUsersApiService delegate;

    public GetUsersApi(@Context ServletConfig servletContext)
    {
        GetUsersApiService delegate = null;

        if (servletContext != null) {
            String implClass = servletContext.getInitParameter("GetUsersApi.implementation");
            if (implClass != null && !"".equals(implClass.trim())) {
                try {
                    delegate = (GetUsersApiService) Class.forName(implClass).newInstance();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }

        if (delegate == null) {
            delegate = GetUsersApiServiceFactory.getGetUsersApi();
        }

        this.delegate = delegate;
    }

    @GET


    @Produces({"application/json"})
    @io.swagger.annotations.ApiOperation(value = "Get users living in London and within 50 miles of London.", notes = "", response = Void.class, tags = {"default",})
    @io.swagger.annotations.ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 200, message = "Success", response = Void.class),

            @io.swagger.annotations.ApiResponse(code = 404, message = "Not Found", response = Void.class)})
    public Response getUsers(@Context SecurityContext securityContext)
            throws NotFoundException, IOException
    {
        return delegate.getUsers(securityContext);
    }
}
