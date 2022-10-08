package service;

import jakarta.jws.WebMethod;
import jakarta.jws.WebService;
import java.io.IOException;

@WebService
public interface ITransfer {

    @WebMethod
    String send(String fileName, String text) throws IOException;

    @WebMethod
    String get(String fileName) throws IOException;

}
