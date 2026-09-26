/*
 * INF1416 - Segurança da Informação
 * Trabalho 2 - DigestCalculator
 *
 * Alunos:
 * João Pedro Zaidman dos Santos Gonçalves - Matrícula: 2320464
 * Breno de Andrade Soares - Matrícula: 2320363
 */

import java.io.File;

public class CommandLineArguments 
{
    public CommandLineArguments(String[] args) 
    {
        validateArgumentCount(args);

        digestAlgorithm = parseDigestAlgorithm(args[0]);
        digestListFile = validateDigestListFile(args[1]);
        monitoredFilesDirectory = validateMonitoredFilesDirectory(args[2]);
    }

    private void validateArgumentCount(String[] args) 
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

    private DigestAlgorithm parseDigestAlgorithm(String value) 
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

    private File validateDigestListFile(String path) 
    {
        File digestListFile = new File(path);

        if (!digestListFile.exists() || !digestListFile.isFile()) 
        {
            throw new IllegalArgumentException(
                "O arquivo com a lista de digests não foi encontrado: " + path
            );
        }

        return digestListFile;
    }

    private File validateMonitoredFilesDirectory(String path) 
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

    public DigestAlgorithm getDigestAlgorithm() { return digestAlgorithm; }
    public File getDigestListFile() { return digestListFile; }
    public File getMonitoredFilesDirectory() { return monitoredFilesDirectory; }

    private final DigestAlgorithm digestAlgorithm;
    private final File digestListFile;
    private final File monitoredFilesDirectory;
}