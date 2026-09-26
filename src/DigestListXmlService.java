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

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.w3c.dom.Node;

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

    public void addNotFoundDigests(List<DigestResult> results)
        throws IOException
    {
        try
        {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(digestListFile);
            Element catalog = document.getDocumentElement();

            if (catalog == null || !catalog.getTagName().equals("CATALOG"))
            {
                throw new IOException("O elemento raiz do XML deve ser CATALOG.");
            }

            boolean documentChanged = false;

            for (DigestResult result : results)
            {
                if (result.getStatus() != DigestStatus.NOT_FOUND)
                {
                    continue;
                }

                FileDigest fileDigest = result.getFileDigest();
                Element fileEntry = findFileEntry(catalog, fileDigest.getFileName());

                if (fileEntry == null)
                {
                    fileEntry = createFileEntry(document, fileDigest.getFileName());
                    catalog.appendChild(fileEntry);
                }

                if (!hasDigestEntry(fileEntry, fileDigest.getAlgorithm()))
                {
                    addDigestEntry(document, fileEntry, fileDigest);
                    documentChanged = true;
                }
            }

            if (documentChanged)
            {
                saveDocument(document);
            }
        }
        catch (ParserConfigurationException | SAXException | TransformerException exception)
        {
            throw new IOException(
                "Não foi possível atualizar o arquivo XML: "
                + digestListFile.getPath(),
                exception
            );
        }
    }

    private Element findFileEntry(Element catalog, String fileName) throws IOException
    {
        NodeList fileEntries = catalog.getElementsByTagName("FILE_ENTRY");

        for (int i = 0; i < fileEntries.getLength(); i++)
        {
            Element fileEntry = (Element) fileEntries.item(i);
            String registeredFileName = getTagText(fileEntry, "FILE_NAME");

            if (fileName.equals(registeredFileName))
            {
                return fileEntry;
            }
        }

        return null;
    }

    private Element createFileEntry(Document document, String fileName)
    {
        Element fileEntry = document.createElement("FILE_ENTRY");
        Element fileNameElement = document.createElement("FILE_NAME");

        fileNameElement.setTextContent(fileName);
        fileEntry.appendChild(fileNameElement);

        return fileEntry;
    }

    private boolean hasDigestEntry(Element fileEntry, DigestAlgorithm algorithm) throws IOException
    {
        NodeList digestEntries = fileEntry.getElementsByTagName("DIGEST_ENTRY");
        for (int i = 0; i < digestEntries.getLength(); i++)
        {
            Element digestEntry = (Element) digestEntries.item(i);
            String digestType = getTagText(digestEntry,"DIGEST_TYPE");

            if (digestType.equalsIgnoreCase(algorithm.name()))
            {
                return true;
            }
        }

        return false;
    }

    private void addDigestEntry(Document document, Element fileEntry, FileDigest fileDigest)
    {
        Element digestEntry = document.createElement("DIGEST_ENTRY");
        Element digestType = document.createElement("DIGEST_TYPE");
        Element digestHex = document.createElement("DIGEST_HEX");

        digestType.setTextContent(fileDigest.getAlgorithm().name());
        digestHex.setTextContent(fileDigest.getDigestHex());

        digestEntry.appendChild(digestType);
        digestEntry.appendChild(digestHex);

        fileEntry.appendChild(digestEntry);
    }

    private void saveDocument(Document document) throws TransformerException
    {
        removeBlankTextNodes(document.getDocumentElement());

        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer();

        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.transform(new DOMSource(document), new StreamResult(digestListFile));
    }

    private void removeBlankTextNodes(Node node)
    {
        NodeList children = node.getChildNodes();

        for (int i = children.getLength() - 1; i >= 0; i--)
        {
            Node child = children.item(i);
            boolean isBlankText =
                child.getNodeType() == Node.TEXT_NODE
                && child.getTextContent().trim().isEmpty();

            if (isBlankText)
            {
                node.removeChild(child);
            }
            else if (child.getNodeType() == Node.ELEMENT_NODE)
            {
                removeBlankTextNodes(child);
            }
        }
    }

    private final File digestListFile;
}