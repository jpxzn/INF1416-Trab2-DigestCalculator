/*
 * INF1416 - Segurança da Informação
 * Trabalho 2 - DigestCalculator
 *
 * Alunos:
 * João Pedro Zaidman dos Santos Gonçalves - Matrícula: 2320464
 * Breno de Andrade Soares - Matrícula: 2320363
 */

import java.util.ArrayList;
import java.util.List;

public class DigestVerificationService
{

    public DigestVerificationService(List<FileDigest> calculated, List<FileDigest> registered)
    {
        calculatedDigests = calculated;
        registeredDigests = registered;
    }

    public List<DigestResult> verifyDigests()
    {
        List<DigestResult> results = new ArrayList<>();

        for (FileDigest digestToVerify : calculatedDigests)
        {
            DigestStatus status = determineStatus(digestToVerify);
            results.add(new DigestResult(digestToVerify, status));
        }

        return results;
    }

    private DigestStatus determineStatus(FileDigest digestToVerify)
    {
        if (hasCollision(digestToVerify))
        {
            return DigestStatus.COLISION;
        }

        FileDigest registeredDigest = findRegisteredDigest(digestToVerify);

        if (registeredDigest == null)
        {
            return DigestStatus.NOT_FOUND;
        }

        if (digestToVerify.getDigestHex().equalsIgnoreCase(registeredDigest.getDigestHex()))
        {
            return DigestStatus.OK;
        }

        return DigestStatus.NOT_OK;
    }

    private boolean hasCollision(FileDigest digestToVerify)
    {
        return collidesWith(digestToVerify, calculatedDigests)
            || collidesWith(digestToVerify, registeredDigests);
    }

    private boolean collidesWith(FileDigest digestToVerify, List<FileDigest> otherDigests)
    {
        for (FileDigest otherDigest : otherDigests)
        {
            boolean differentFile = !digestToVerify.getFileName().equals(otherDigest.getFileName());
            boolean sameAlgorithm = digestToVerify.getAlgorithm() == otherDigest.getAlgorithm();
            boolean sameDigest = digestToVerify.getDigestHex().equalsIgnoreCase(otherDigest.getDigestHex());

            if (differentFile && sameAlgorithm && sameDigest)
            {
                return true;
            }
        }

        return false;
    }

    private FileDigest findRegisteredDigest(FileDigest digestToVerify)
    {
        for (FileDigest registeredDigest : registeredDigests)
        {
            boolean sameFile = digestToVerify.getFileName().equals(registeredDigest.getFileName());
            boolean sameAlgorithm = digestToVerify.getAlgorithm() == registeredDigest.getAlgorithm();

            if (sameFile && sameAlgorithm)
            {
                return registeredDigest;
            }
        }

        return null;
    }

    private final List<FileDigest> registeredDigests;
    private final List<FileDigest> calculatedDigests;
}