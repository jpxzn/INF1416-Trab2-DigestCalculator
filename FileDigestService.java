/*
 * INF1416 - Segurança da Informação
 * Trabalho 2 - DigestCalculator
 *
 * Alunos:
 * João Pedro Zaidman dos Santos Gonçalves - Matrícula: 2320464
 * Breno de Andrade Soares - Matrícula: 2320363
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FileDigestService 
{
    public String calculateDigest(File file, DigestAlgorithm algorithm)
            throws IOException, NoSuchAlgorithmException 
    {
        MessageDigest messageDigest = MessageDigest.getInstance(algorithm.getJcaName());
        byte[] buffer = new byte[BUFFER_SIZE];

        try (FileInputStream inputStream = new FileInputStream(file)) 
        {
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) 
            {
                messageDigest.update(buffer, 0, bytesRead);
            }
        }

        byte[] digestBytes = messageDigest.digest();

        return convertToHex(digestBytes);
    }

    private String convertToHex(byte[] digestBytes) 
    {
        StringBuilder digestHex = new StringBuilder();

        for (byte digestByte : digestBytes) 
        {
            digestHex.append(String.format("%02x", digestByte & 0xff));
        }

        return digestHex.toString();
    }

    private static final int BUFFER_SIZE = 8192;
}