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
import java.util.ArrayList;
import java.util.List;

public class FileDigestService 
{

    public FileDigestService(DigestAlgorithm algorithm, File directory)
    {
        digestAlgorithm = algorithm;
        monitoredDirectory = directory;
    }


    public List<FileDigest> calculateDigests() throws IOException, NoSuchAlgorithmException 
    {
        File[] monitoredFiles = getMonitoredFiles();
        List<FileDigest> fileDigests = new ArrayList<>();

        for (File monitoredFile : monitoredFiles) 
        {
            String digestHex;

            try 
            {
                digestHex = calculateDigest(monitoredFile);
            } 
            catch (IOException exception) 
            {
                throw new IOException(
                    "Erro ao ler o arquivo: " + monitoredFile.getName(),
                    exception
                );
            }

            FileDigest fileDigest = new FileDigest(
                monitoredFile.getName(),
                digestAlgorithm,
                digestHex
            );

            fileDigests.add(fileDigest);
        }

        return fileDigests;
    }

    private File[] getMonitoredFiles() throws IOException 
    {
        File[] monitoredFiles = monitoredDirectory.listFiles(File::isFile);

        if (monitoredFiles == null) 
        {
            throw new IOException(
                "Não foi possível acessar os arquivos da pasta: "
                + monitoredDirectory.getPath()
            );
        }

        return monitoredFiles;
    }

    private String calculateDigest(File file)
            throws IOException, NoSuchAlgorithmException 
    {
        MessageDigest messageDigest = MessageDigest.getInstance(digestAlgorithm.getJcaName());
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
    private final DigestAlgorithm digestAlgorithm;
    private final File monitoredDirectory;

}