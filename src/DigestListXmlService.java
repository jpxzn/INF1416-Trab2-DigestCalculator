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
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class DigestListXmlService 
{
    public DigestListXmlService(File digestListFile) 
    {
        this.digestListFile = digestListFile;
    }

    public List<FileDigest> readDigests() throws IOException 
    {
        try 
        {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(digestListFile);
            Element catalog = document.getDocumentElement();

            if (catalog == null || !catalog.getTagName().equals("CATALOG")) 
            {
                throw new IOException(
                    "O elemento raiz do XML deve ser CATALOG."
                );
            }

            List<FileDigest> registeredDigests = new ArrayList<>();
            NodeList fileEntries = catalog.getElementsByTagName("FILE_ENTRY");

            for (int i = 0; i < fileEntries.getLength(); i++) 
            {
                Element fileEntry = (Element) fileEntries.item(i);
                String fileName = getTagText(fileEntry,"FILE_NAME");
                NodeList digestEntries = fileEntry.getElementsByTagName("DIGEST_ENTRY");

                for (int j = 0; j < digestEntries.getLength(); j++) 
                {
                    Element digestEntry = (Element) digestEntries.item(j);

                    String digestType = getTagText(digestEntry, "DIGEST_TYPE");
                    String digestHex = getTagText(digestEntry,"DIGEST_HEX");
                    DigestAlgorithm algorithm = parseDigestAlgorithm(digestType);

                    FileDigest fileDigest = new FileDigest(
                        fileName,
                        algorithm,
                        digestHex.toLowerCase()
                    );

                    registeredDigests.add(fileDigest);
                }
            }

            return registeredDigests;
        } 
        catch (ParserConfigurationException | SAXException exception) 
        {
            throw new IOException(
                "Não foi possível interpretar o arquivo XML: "
                + digestListFile.getPath(),
                exception
            );
        }
    }

    private String getTagText(Element parent, String tagName)
            throws IOException 
    {
        NodeList elements = parent.getElementsByTagName(tagName);

        if (elements.getLength() == 0) 
        {
            throw new IOException(
                "Tag não encontrada no XML: " + tagName
            );
        }

        return elements.item(0).getTextContent().trim();
    }

    private DigestAlgorithm parseDigestAlgorithm(String value)
            throws IOException 
    {
        try 
        {
            return DigestAlgorithm.valueOf(value.toUpperCase());
        } 
        catch (IllegalArgumentException exception) 
        {
            throw new IOException(
                "Algoritmo inválido encontrado no XML: " + value,
                exception
            );
        }
    }

    private final File digestListFile;
}