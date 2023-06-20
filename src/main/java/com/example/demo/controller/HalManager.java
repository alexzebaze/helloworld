package com.example.demo.controller;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.*;

import com.sun.istack.Nullable;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/*import fr.inrae.dipso.gestionattributiondoihal.dto.checkhaldoiresponse.CheckHalDoiResponse;
import fr.inrae.dipso.gestionattributiondoihal.dto.checkhaldoiresponse.HalResponse;
import fr.inrae.dipso.gestionattributiondoihal.dto.doi.enregistrerdoiresponse.EnregistrerDoiResponse;
import fr.inrae.dipso.gestionattributiondoihal.dto.doi.genererdoiresponse.GenererDoiResponse;
import fr.inrae.dipso.gestionattributiondoihal.dto.doi.majdoirequest.MajDOIRequest;
import fr.inrae.dipso.gestionattributiondoihal.service.DoiService;
import fr.inrae.dipso.gestionattributiondoihal.service.HalService;*/

/**
 * Classe des ressources de la partie backend de l'application.<br/>
 * <br/>
 * La liste des ressources est la suivante :<br/>
 * <br/>
 * checkhaldoi/{halId} (GET)<br/>
 * generatehaldoi (POST)<br/>
 * <br/>
 * {halId} est notre identifiant HAL
 *
 * @author cedric.barbet@softeam.fr
 *
 */
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class HalManager {



    /**
     * Méthode permettant d'injecter le DOI dans la structure d'une notice HAL extraite au format XML TEI.
     * 	 *
     * @param halXMLTEI
     * @param doi
     * @return String
     */
    private String addingDoi(String halXMLTEI, String doi) {

        InputSource is = new InputSource(new StringReader(halXMLTEI));
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        //String halXmlTeiUpdated = "<?xml version=\"1.0\"?>";
        String halXmlTeiUpdated = "";
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(is);
            Element element = doc.createElement("idno");
            element.setAttribute("type", "doi");
            Text newDOI = doc.createTextNode(doi);
            element.appendChild(newDOI);
            XPathExpression expression = XPathFactory.newInstance().newXPath()
                    .compile("/TEI/text/body/listBibl/biblFull/sourceDesc/biblStruct");
            NodeList nodeResult = (NodeList) expression.evaluate(doc.getDocumentElement(), XPathConstants.NODESET);
            nodeResult.item(0).appendChild(element);
            doc.getDocumentElement().normalize();
            DOMSource source = new DOMSource(doc);
            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            transformer.transform(source, result);
            halXmlTeiUpdated = halXmlTeiUpdated + writer.toString();

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }

        return halXmlTeiUpdated;
    }


    /**
     * Méthode permettant d'injecter le DOI dans la structure d'une notice HAL extraite au format XML TEI.
     * 	 *
     * @param halXMLTEI
     * @param doi
     * @return String
     */
    @GetMapping("addingNewDoi")
    private String addingNewDoi(@Nullable String halXMLTEI, @Nullable String doi) {
        halXMLTEI = halXMLTEI == null ? "" : halXMLTEI;
        doi = doi == null ? "" : doi;

        InputSource is = new InputSource(new StringReader(halXMLTEI));
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        //String halXmlTeiUpdated = "<?xml version=\"1.0\"?>";
        String halXmlTeiUpdated = "";
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            /* remplacé ic par hal_tel_origine.xml mon fichier se trouvant à la racine */
            Document doc = dBuilder.parse("hal_tel_origine.xml");

            //final Element monogr = (Element) doc.getElementsByTagName("monogr").item(0);

            XPathExpression expression = XPathFactory.newInstance().newXPath()
                    .compile("/TEI/text/body/listBibl/biblFull/sourceDesc/biblStruct");

            /*
            Element element = doc.createElement("idno");
            element.setAttribute("type", "doi");
            Text newDOI = doc.createTextNode(doi);
            element.appendChild(newDOI);


            NodeList nodeResult = (NodeList) expression.evaluate(doc.getDocumentElement(), XPathConstants.NODESET);

            for (int idx = 0; idx < nodeResult.getLength(); idx++) {
                NodeList nodeList = nodeResult.item(idx).getChildNodes();
                for (int i = 0; i < nodeList.getLength(); i++) {
                    Node currentNode = nodeList.item(i);
                    if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
                        //calls this method for all the children which is Element
                        if (currentNode.getNodeName().equals("ref")) {
                            System.out.println(currentNode);
                            currentNode.appendChild(element);
                        }
                    }
                }
            }*/

            /**
             * @Author Alex
             */
            NodeList refNodes = doc.getElementsByTagName("ref");
            for (int i = 0; i<refNodes.getLength(); i++) {
                Element refElement = (Element) refNodes.item(i);

                Element idnoElement = doc.createElement("idno");
                idnoElement.setAttribute("type", "idno_type");
                idnoElement.setTextContent("idno_value");

                // Insérer l'élément "idno" avant l'élément "ref"
                refElement.getParentNode().insertBefore(idnoElement, refElement);
            }

            //doc.getElementsByTagName("biblStruct").item(0).appendChild(element);
            //monogr.insertBefore(element,monogr.getLastChild().getNextSibling());
            //NodeList list = doc.getElementsByTagName("monogr");
            //System.out.println(list);
            //XPathExpression expression = XPathFactory.newInstance().newXPath()
            //		.compile("/TEI/text/body/listBibl/biblFull/sourceDesc/biblStruct/monogr");
            //NodeList nodeResult = (NodeList) expression.evaluate(doc.getDocumentElement(), XPathConstants.NODESET);
            //expression.af(element, element.getFirstChild().getNextSibling());
            //list.insertBefore(element, element1.getFirstChild().getNextSibling());
            //nodeResult.item(0).appendChild(element);
            doc.getDocumentElement().normalize();
            DOMSource source = new DOMSource(doc);
            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(new File("HAL_TEL_MODIFIE.xml"));
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            transformer.transform(source, result);
            halXmlTeiUpdated = halXmlTeiUpdated + writer.toString();

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        } catch (XPathExpressionException e) {
            throw new RuntimeException(e);
        }

        return halXmlTeiUpdated;
    }

    /**
     * Méthode permettant de parser une notice HAL au format XML-TEI et d'injecter "inra_publicVise_local_SC" si pas présent.
     * 	 *
     * @param halXMLTEI
     * @return String
     */
    private String replaceElementXml(String halXMLTEI) {

        InputSource is = new InputSource(new StringReader(halXMLTEI));
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        String halXmlTeiUpdated = "";
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(is);
            XPath xpath = XPathFactory.newInstance().newXPath();
            NodeList nodes = (NodeList)xpath.evaluate("//*[contains(@n, 'DOUV')]",
                    doc, XPathConstants.NODESET);
            for (int idx = 0; idx < nodes.getLength(); idx++) {
                Node value = nodes.item(idx).getAttributes().getNamedItem("n");
                String val = value.getNodeValue();
                value.setNodeValue(val.replaceAll("DOUV", "OUV"));
            }
            doc.getDocumentElement().normalize();
            DOMSource source = new DOMSource(doc);
            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            transformer.transform(source, result);
            halXmlTeiUpdated = halXmlTeiUpdated + writer.toString();

        } catch (TransformerConfigurationException e) {
            throw new RuntimeException(e);
        } catch (XPathExpressionException e) {
            throw new RuntimeException(e);
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }

        return halXmlTeiUpdated;
    }

    /**
     * Méthode permettant de parser une notice HAL au format XML-TEI et d'injecter "inra_publicVise_local_SC" si pas présent.
     * 	 *
     * @param halXMLTEI
     * @return String
     */
    private String addElementXml(String halXMLTEI) {
        InputSource is = new InputSource(new StringReader(halXMLTEI));
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;

        String halXmlTeiUpdated = "";
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(is);
            XPath xpath = XPathFactory.newInstance().newXPath();
            NodeList nodes = (NodeList)xpath.evaluate("//*[contains(@name, 'inra_publicVise_local')]",
                    doc, XPathConstants.NODESET);
            //System.out.println(nodes.getLength());
            if (nodes.getLength() == 0) {
                Element fs = doc.createElement("fs");
                Element f = doc.createElement("f");
                Element str = doc.createElement("string");

                f.setAttribute("name", "inra_publicVise_local");
                f.setAttribute("notation", "string");
                str.appendChild(doc.createTextNode("inra_publicVise_local_SC"));

                f.appendChild(str);
                fs.appendChild(f);
                //doc.appendChild(fs);
                XPathExpression expression = XPathFactory.newInstance().newXPath()
                        .compile("/TEI/text/body/listBibl/biblFull/editionStmt/edition");
                NodeList nodeResult = (NodeList) expression.evaluate(doc.getDocumentElement(), XPathConstants.NODESET);
                nodeResult.item(0).appendChild(fs);
                doc.getDocumentElement().normalize();
                DOMSource source = new DOMSource(doc);
                StringWriter writer = new StringWriter();
                StreamResult result = new StreamResult(writer);
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
                transformer.transform(source, result);
                halXmlTeiUpdated = halXmlTeiUpdated + writer.toString();
            } else {
                halXmlTeiUpdated = halXMLTEI;
            }

        } catch (TransformerConfigurationException e) {
            throw new RuntimeException(e);
        } catch (XPathExpressionException e) {
            throw new RuntimeException(e);
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }

        return halXmlTeiUpdated;
    }

    /**
     * Méthode permettant de parser une notice HAL au format XML-TEI et d'injecter une licence si pas présent.
     * 	 *
     * @param halXMLTEI
     * @return String
     */
    private String addLicenceXml(String halXMLTEI) {
        InputSource is = new InputSource(new StringReader(halXMLTEI));
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;

        String halXmlTeiUpdated = "";
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(is);
            NodeList list = doc.getElementsByTagName("availability");
            //System.out.println("Nombre de licence : ");
            //System.out.println(list.getLength());
            if (list.getLength() == 1) {
                XPathExpression expression = XPathFactory.newInstance().newXPath()
                        .compile("/TEI/text/body/listBibl/biblFull/publicationStmt");
                NodeList nodeResult = (NodeList) expression.evaluate(doc.getDocumentElement(), XPathConstants.NODESET);

                Element availability = doc.createElement("availability");
                Element licence = doc.createElement("licence");

                availability.setAttribute("status", "restricted");
                licence.setAttribute("target", "http://creativecommons.org/licenses/by-nc-nd/");
                licence.appendChild(doc.createTextNode("Attribution - NonCommercial - NoDerivatives"));

                availability.appendChild(licence);

                nodeResult.item(0).appendChild(availability);
                doc.getDocumentElement().normalize();
                DOMSource source = new DOMSource(doc);
                StringWriter writer = new StringWriter();
                StreamResult result = new StreamResult(writer);
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
                transformer.transform(source, result);
                halXmlTeiUpdated = halXmlTeiUpdated + writer.toString();
            } else {
                halXmlTeiUpdated = halXMLTEI;
            }

        } catch (TransformerConfigurationException e) {
            throw new RuntimeException(e);
        } catch (XPathExpressionException e) {
            throw new RuntimeException(e);
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }

        return halXmlTeiUpdated;
    }
}