package io.swagger.api;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2020-06-23T15:45:21.078Z")
public class NotFoundException extends ApiException
{
    private int code;

    public NotFoundException(int code, String msg)
    {
        super(code, msg);
        this.code = code;
    }
}
