package service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Locale;

@Setter
@Getter
@NoArgsConstructor
public class DiffieHellmanAlgorithm {

    private String P;
    private String G;
    private String K_sec;
    private String K_pubA;
    private String K_pubB;
    private String K;
    private String K_ses = "null";

    private static FileWriter fileWriter;
    private static final int KEY_LENGHT = 1024;
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    public static String getPublicKey(String P, String G, String K_sec){

        return calculatePower(new BigInteger(G, 16), new BigInteger(K_sec, 16), new BigInteger(P, 16));
    }
    public static String getPrivateKey(String K_pubB, String K_sec, String P){

        return calculatePower(new BigInteger(K_pubB, 16), new BigInteger(K_sec, 16), new BigInteger(P, 16));
    }
    public static String getRandomBigIntegerNumber(){

        return new BigInteger(String.valueOf(BigInteger.probablePrime(KEY_LENGHT, SECURE_RANDOM)), 10).toString(16).toUpperCase(Locale.ROOT);
    }
    public static void writeFile(String text, String fileName) throws IOException {
        fileWriter = new FileWriter(fileName);
        fileWriter.write(text);
        fileWriter.close();
    }
    public static String [] readFileKey(String file) throws IOException {
        Path fileName = Path.of(file);
        String fileKey = Files.readString(fileName);
        return fileKey.split("\n");
    }
    public static String readFile(String file) throws IOException {
        Path fileName = Path.of(file);
        return Files.readString(fileName);
    }
    private static String calculatePower(BigInteger x, BigInteger y, BigInteger P)
    {
        BigInteger result = BigInteger.ZERO;
        if (y.equals(BigInteger.ONE)){
            return x.toString();
        }
        else{
            result = x.modPow(y, P);
            return new BigInteger(result.toString(), 10).toString(16).toUpperCase(Locale.ROOT);
        }
    }
    public static String SHA256(String input)
    {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
