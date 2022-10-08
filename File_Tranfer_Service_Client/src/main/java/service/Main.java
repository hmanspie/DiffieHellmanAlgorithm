package service;

import jakarta.xml.ws.Service;

import java.io.FileWriter;
import java.io.IOException ;
import javax.xml.namespace.QName;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.SecureRandom;
import java.util.Locale;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        URL url = new URL("http://localhost:8080/File_Tranfer_Service-1.0-SNAPSHOT/ITransferImplService?wsdl");
        QName qName = new QName("http://service/", "ITransferImplService");
        Service service = Service.create(url, qName);
        QName port = new QName("http://service/", "ITransferImplPort");
        ITransfer tv = service.getPort(port, ITransfer.class);

        DiffieHellmanAlgorithm DHA = new DiffieHellmanAlgorithm();

        int key = 0;
        do {
            System.out.println("1 --------> Generate public key A.");
            System.out.println("2 --------> Get public key B.");
            System.out.println("3 --------> Continue.");
            key = new Scanner(System.in).nextInt();

            switch (key) {
                case 1:
                    DHA.setP(DiffieHellmanAlgorithm.getRandomBigIntegerNumber());
                    DHA.setG(DiffieHellmanAlgorithm.getRandomBigIntegerNumber());
                    DHA.setK_sec(DiffieHellmanAlgorithm.getRandomBigIntegerNumber());
                    DHA.setK_pubA(DiffieHellmanAlgorithm.getPublicKey(DHA.getP(), DHA.getG(), DHA.getK_sec()));
                    DiffieHellmanAlgorithm.writeFile(DHA.getP() + "\n" + DHA.getG() + "\n" + DHA.getK_pubA(), "key.key");
                    System.out.println("Key generated successfully!");
                    break;
                case 2:
                    String [] arrKey = DiffieHellmanAlgorithm.readFileKey("key.key");
                    DHA.setP(arrKey[0]);
                    DHA.setG(arrKey[1]);
                    DHA.setK_pubB(arrKey[2]);
                    if(DHA.getK_sec().equals(""))
                        DHA.setK_sec(DiffieHellmanAlgorithm.getRandomBigIntegerNumber());
                    if( DHA.getK_pubA().equals(""))
                        DHA.setK_pubA(DiffieHellmanAlgorithm.getPublicKey(DHA.getP(), DHA.getG(), DHA.getK_sec()));
                    DiffieHellmanAlgorithm.writeFile(DHA.getP() + "\n" + DHA.getG() + "\n" + DHA.getK_pubA(), "key.key");
                    System.out.println();
                    System.out.println("Get key successfully!");
                    break;
                case 3:
                    try {
                        DHA.setK(DiffieHellmanAlgorithm.getPrivateKey(DHA.getK_pubB(), DHA.getK_sec(), DHA.getP()));
                        DHA.setK_ses(DiffieHellmanAlgorithm.SHA256(DHA.getK()));
                        break;
                    }
                    catch (Exception e){
                        System.out.println("No keys found!!!");
                    }
                default:
                    System.out.println("Incorrect input!!!");
                    break;
            }
        }while(key != 3);



        System.out.println("P: " + DHA.getP());
        System.out.println("G: " + DHA.getG());
        System.out.println("K_secret: " + DHA.getK_sec());
        System.out.println("K_public user A: " + DHA.getK_pubA());
        System.out.println("K_public user B: " + DHA.getK_pubB());
        System.out.println("K: " + DHA.getK());
        System.out.println("K_session = SHA256(K): " + DHA.getK_ses().toUpperCase(Locale.ROOT));
        AES256.SECRET_KEY = DHA.getK_ses();

        do{
            System.out.println();
            System.out.println("Select: ");
            System.out.println("1 --------> Send file.");
            System.out.println("2 --------> Get file.");
            try{
                String nameFiles;
                int choose = new Scanner(System.in).nextInt();
                switch (choose){
                    case 1:
                        System.out.print("Enter to file name: ");
                        nameFiles = new Scanner(System.in).nextLine();
                        System.out.println(tv.send(nameFiles, DiffieHellmanAlgorithm.readFile(nameFiles)));
                        break;
                    case 2:
                        System.out.print("Enter to file name: ");
                        nameFiles = new Scanner(System.in).nextLine();
                        System.out.println();
                        DiffieHellmanAlgorithm.writeFile(tv.get(nameFiles),"enc_" + nameFiles);
                        DiffieHellmanAlgorithm.writeFile(AES256.decrypt(tv.get(nameFiles)),nameFiles);
                        System.out.println("File " + nameFiles + " received successfully!");
                        break;
                    default:
                        System.out.println("\n" + "Incorrect input!!!");
                        break;
                }
            }catch (Exception e){
                System.out.println("\n" + "Incorrect input!!!");
            }
        }while(true);
    }
}
