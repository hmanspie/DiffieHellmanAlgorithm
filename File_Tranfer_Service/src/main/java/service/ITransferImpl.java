package service;

import com.hmanspie.ServerServlet;
import jakarta.jws.WebService;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebService(endpointInterface = "service.ITransfer")
public class ITransferImpl implements ITransfer {

    private static Logger log = Logger.getLogger(ITransferImpl.class.getName());

    @Override
    public String send(String fileName, String text) throws IOException {
        if(!ServerServlet.serverStart)
            return "File transfer service not running";

        FileWriter fw = new FileWriter(fileName);
        fw.write(text);
        fw.close();

        log.log(Level.INFO, "File " + fileName + " received successfully!");

        return "File " + fileName + " has been successfully delivered to the server!";
    }

    @Override
    public String get(String fileName) throws IOException {
        if(!ServerServlet.serverStart)
            return "File transfer service not running";

        int i = 0;
        String text = "";
        FileReader fr = new FileReader(fileName);
        while ((i = fr.read()) != -1) {
            text += (char)i;
        }
        fr.close();

        return text;
    }

}
