/*
 * INF1416 - Segurança da Informação
 * Trabalho 2 - DigestCalculator
 *
 * Alunos:
 * João Pedro Zaidman dos Santos Gonçalves - Matrícula: 2320464
 * Breno de Andrade Soares - Matrícula: 2320363
 */

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class DigestCalculator 
{
    public static void main(String[] args) 
    {
        try 
        {
            validateArgumentCount(args);

            DigestAlgorithm digestAlgorithm = parseDigestAlgorithm(args[0]);

            validateDigestListFile(args[1]);

            File monitoredFilesDirectory = validateMonitoredFilesDirectory(args[2]);

            printDigests(monitoredFilesDirectory, digestAlgorithm);
        } 
        catch (IllegalArgumentException | IOException | NoSuchAlgorithmException exception) 
        {
            System.err.println(exception.getMessage());
        }
    }

    private static void validateArgumentCount(String[] args) 
    {
        if (args.length != 3) 
        {
            throw new IllegalArgumentException(
                "Quantidade de argumentos inválida."
                + System.lineSeparator()
                + "Uso: java DigestCalculator <MD5|SHA1|SHA256|SHA512> "
                + "<caminho_da_lista_de_digests> <caminho_da_pasta>"
            );
        }
    }

    private static DigestAlgorithm parseDigestAlgorithm(String value) 
    {
        try 
        {
            return DigestAlgorithm.valueOf(value.toUpperCase());
        } 
        catch (IllegalArgumentException exception) 
        {
            throw new IllegalArgumentException(
                "Algoritmo de digest inválido: " + value
                + System.lineSeparator()
                + "Algoritmos permitidos: MD5, SHA1, SHA256 ou SHA512.",
                exception
            );
        }
    }

    private static void validateDigestListFile(String path) 
    {
        File digestListFile = new File(path);

        if (!digestListFile.exists() || !digestListFile.isFile()) 
        {
            throw new IllegalArgumentException(
                "O arquivo com a lista de digests não foi encontrado: " + path
            );
        }
    }

    private static File validateMonitoredFilesDirectory(String path) 
    {
        File monitoredFilesDirectory = new File(path);

        if (!monitoredFilesDirectory.exists() || !monitoredFilesDirectory.isDirectory()) 
        {
            throw new IllegalArgumentException(
                "A pasta dos arquivos monitorados não foi encontrada: " + path
            );
        }

        return monitoredFilesDirectory;
    }

    private static void printDigests(
        File monitoredFilesDirectory,
        DigestAlgorithm digestAlgorithm
    ) throws IOException, NoSuchAlgorithmException 
    {
        FileDigestService digestService = new FileDigestService();

        File[] monitoredFiles = monitoredFilesDirectory.listFiles(File::isFile);

        if (monitoredFiles == null) 
        {
            throw new IOException(
                "Não foi possível acessar os arquivos da pasta: "
                + monitoredFilesDirectory.getPath()
            );
        }

        for (File monitoredFile : monitoredFiles) 
        {
            String digestHex;

            try 
            {
                digestHex = digestService.calculateDigest(monitoredFile, digestAlgorithm);
            } 
            catch (IOException exception) 
            {
                throw new IOException(
                    "Erro ao ler o arquivo: " + monitoredFile.getName(),
                    exception
                );
            } 
            catch (NoSuchAlgorithmException exception) 
            {
                throw new NoSuchAlgorithmException(
                    "Algoritmo não suportado pela JCA: " + digestAlgorithm
                );
            }

            System.out.println(
                monitoredFile.getName() + " "
                + digestAlgorithm + " "
                + digestHex
            );
        }
    }
}