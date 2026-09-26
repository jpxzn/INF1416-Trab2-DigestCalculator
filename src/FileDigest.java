/*
 * INF1416 - Segurança da Informação
 * Trabalho 2 - DigestCalculator
 *
 * Alunos:
 * João Pedro Zaidman dos Santos Gonçalves - Matrícula: 2320464
 * Breno de Andrade Soares - Matrícula: 2320363
 */

public class FileDigest 
{
    public FileDigest(
        String fileName,
        DigestAlgorithm algorithm,
        String digestHex
    ) 
    {
        this.fileName = fileName;
        this.algorithm = algorithm;
        this.digestHex = digestHex;
    }

    public String getFileName() { return fileName; }

    public DigestAlgorithm getAlgorithm() { return algorithm; }

    public String getDigestHex() { return digestHex; }

    @Override
    public String toString() 
    {
        return fileName + " " + algorithm + " " + digestHex;
    }

    private final String fileName;
    private final DigestAlgorithm algorithm;
    private final String digestHex;
}