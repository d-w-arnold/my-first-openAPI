package io.swagger.api;

import io.swagger.jaxrs.config.SwaggerContextService;
import io.swagger.models.Contact;
import io.swagger.models.Info;
import io.swagger.models.License;
import io.swagger.models.Swagger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

public class Bootstrap extends HttpServlet
{
    @Override
    public void init(ServletConfig config) throws ServletException
    {
        Info info = new Info()
                .title("DWP Online Test - Software Engineer Interview")
                .version("1.0.0")
                .description("This is my online test for the role of Software Engineer.")
                .termsOfService("")
                .contact(new Contact()
                        .email(""))
                .license(new License()
                        .name("")
                        .url("http://unlicense.org"));

        ServletContext context = config.getServletContext();
        Swagger swagger = new Swagger().info(info);

        new SwaggerContextService().withServletConfig(config).updateSwagger(swagger);
    }
}
