/*
 * INF1416 - Segurança da Informação
 * Trabalho 2 - DigestCalculator
 *
 * Alunos:
 * João Pedro Zaidman dos Santos Gonçalves - Matrícula: 2320464
 * Breno de Andrade Soares - Matrícula: 2320363
 */

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class DigestCalculator 
{
    public static void main(String[] args) 
    {
        try 
        {
            CommandLineArguments arguments = new CommandLineArguments(args);

            DigestListXmlService digestListService = new DigestListXmlService(arguments.getDigestListFile());
            FileDigestService digestService = new FileDigestService(arguments.getDigestAlgorithm(), 
                                                                    arguments.getMonitoredFilesDirectory());
            
            List<FileDigest> registeredDigests = digestListService.readDigests();
            List<FileDigest> calculatedDigests = digestService.calculateDigests();

            DigestVerificationService verificationService = new DigestVerificationService(
                calculatedDigests,
                registeredDigests
            );

            List<DigestResult> results = verificationService.verifyDigests();
            for (DigestResult result : results)
            {
                System.out.println(result);
            }

        } 
        catch (IllegalArgumentException | IOException | NoSuchAlgorithmException exception) 
        {
            System.err.println(exception.getMessage());
        }
    }
}