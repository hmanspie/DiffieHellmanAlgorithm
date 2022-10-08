package service;

import jakarta.jws.WebMethod;
import jakarta.jws.WebService;

@WebService
public interface ITransfer {

    @WebMethod
    String send(String fileName, String text);

    @WebMethod
    String get(String fileName);

}
