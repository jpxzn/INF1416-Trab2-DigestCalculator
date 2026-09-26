/*
 * INF1416 - Segurança da Informação
 * Trabalho 2 - DigestCalculator
 *
 * Alunos:
 * João Pedro Zaidman dos Santos Gonçalves - Matrícula: 2320464
 * Breno de Andrade Soares - Matrícula: 2320363
 */

public class DigestResult
{
    public DigestResult(FileDigest fileDigest, DigestStatus status)
    {
        this.fileDigest = fileDigest;
        this.status = status;
    }

    public FileDigest getFileDigest() { return fileDigest; }

    public DigestStatus getStatus() { return status; }

    @Override
    public String toString()
    {
        return fileDigest + " (" + status + ")";
    }

    private final FileDigest fileDigest;
    private final DigestStatus status;
}