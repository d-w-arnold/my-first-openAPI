package io.swagger.api;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJerseyServerCodegen", date = "2020-06-23T15:45:21.078Z")
public class ApiException extends Exception
{
    private int code;

    public ApiException(int code, String msg)
    {
        super(msg);
        this.code = code;
    }
}
