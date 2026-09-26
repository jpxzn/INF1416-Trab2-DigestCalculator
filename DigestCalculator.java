import java.io.File;

public class DigestCalculator 
{
    public static void main(String[] args) 
    {
        if (args.length != 3) 
        {
            System.err.println("Quantidade de argumentos inválida.");
            System.err.println("Uso: java DigestCalculator <MD5|SHA1|SHA256|SHA512> " 
                            + "<caminho_da_lista_de_digests> <caminho_da_pasta>");
            return;
        }

        DigestAlgorithm digestAlgorithm;

        try 
        {
            digestAlgorithm = DigestAlgorithm.valueOf(args[0].toUpperCase());
        } 
        catch (IllegalArgumentException exception) 
        {
            System.err.println("Algoritmo de digest inválido: " + args[0]);
            System.err.println("Algoritmos permitidos: MD5, SHA1, SHA256 ou SHA512.");
            return;
        }

        String digestListPath = args[1];
        String monitoredFilesPath = args[2];

        File digestListFile = new File(digestListPath);
        File monitoredFilesDirectory = new File(monitoredFilesPath);

        if (!digestListFile.exists() || !digestListFile.isFile()) 
        {
            System.err.println("O arquivo com a lista de digests não foi encontrado: " + digestListPath);
            return;
        }

        if (!monitoredFilesDirectory.exists() || !monitoredFilesDirectory.isDirectory()) 
        {
            System.err.println("A pasta dos arquivos monitorados não foi encontrada: " + monitoredFilesPath);
            return;
        }

        System.out.println("Argumentos válidos.");
        System.out.println("Algoritmo de digest: " + digestAlgorithm);
        System.out.println("Arquivo com a lista de digests: " + digestListFile.getAbsolutePath());
        System.out.println("Pasta dos arquivos monitorados: " + monitoredFilesDirectory.getAbsolutePath());
    }
}