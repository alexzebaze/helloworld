package com.example.demo.controller.persone;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
public class XmlManipulator {

    private ResourceLoader resourceLoader;

    public XmlManipulator(){}
    public XmlManipulator(ResourceLoader resourceLoader){
        this.resourceFile = (Resource) resourceLoader;
    }

    @Value("classpath:/static/person.xml")
    private Resource resourceFile;

    @Value("classpath:static/xml/")
    private Resource resourceFolder;



    @GetMapping("xmlcreatePersonInsert")
    public void insertPerson(){
        try {
            // Créer une instance de DocumentBuilderFactory
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            // Créer un parseur de documents XML en utilisant DocumentBuilderFactory
            DocumentBuilder builder = factory.newDocumentBuilder();

            // Charger le fichier XML dans un objet Document
            //Document document = builder.parse("classpath:static/personList.xml");
            Document document = builder.parse("hal_tel_origine.xml");

            // Récupérer la racine du document
            Element root = document.getDocumentElement();

            // Récupérer tous les éléments "person"
            NodeList refNodes = document.getElementsByTagName("ref");

            // Parcourir les éléments "person"
            for (int i = 0; i<refNodes.getLength(); i++) {
                Element refElement = (Element) refNodes.item(i);

                // Créer l'élément "idno"
                Element idnoElement = document.createElement("idno");
                idnoElement.setAttribute("type", "RNSR");
                idnoElement.setTextContent("200017843Z");

                // Insérer l'élément "idno" avant l'élément "ref"
                refElement.getParentNode().insertBefore(idnoElement, refElement);
            }

            // Enregistrer les modifications dans un nouveau fichier XML
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();

            transformer.setOutputProperty(javax.xml.transform.OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new File("HAL_TEL_MODIFIE.xml"));
            transformer.transform(source, result);

            System.out.println("Modification effectuée avec succès.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GetMapping("xmlcreatePerson")
    public void createPerson() {
        try {

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();

            // Créer l'élément racine
            Element personListElement = doc.createElement("personList");
            doc.appendChild(personListElement);

            // Créer l'élément person
            Element personElement = doc.createElement("person");
            personListElement.appendChild(personElement);

            // Créer l'élément name et lui attribuer la valeur "John"
            Element nameElement = doc.createElement("name");
            nameElement.appendChild(doc.createTextNode("John"));
            personElement.appendChild(nameElement);

            // Créer l'élément age et lui attribuer la valeur "30"
            Element ageElement = doc.createElement("age");
            ageElement.appendChild(doc.createTextNode("30"));
            personElement.appendChild(ageElement);

            // Transformer le document en fichier XML
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();

            transformer.setOutputProperty(javax.xml.transform.OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("personList.xml"));

            // Écrire le contenu du document dans le fichier XML
            transformer.transform(source, result);

            System.out.println("Le fichier XML a été créé avec succès.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @GetMapping("/xmlpersone")
    public void xmlpersone(String[] args) {
        System.out.println("xmlpersone");
        try {


            ClassPathResource classPathResource = new ClassPathResource("static/person.xml");

            File file = new File(classPathResource.getFile().toString());
            System.out.println(classPathResource.getFile().toString());
            JAXBContext jaxbContext = JAXBContext.newInstance(PersonList.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            PersonList personList = (PersonList) jaxbUnmarshaller.unmarshal(file);

            // Accéder à la liste des personnes
            List<Person> persons = personList.getPersons();
            for (Person person : persons) {
                System.out.println("Nom : " + person.getName());
                System.out.println("Âge : " + person.getAge());
                System.out.println();
            }
        } catch (JAXBException | IOException e) {
            e.printStackTrace();
        }
    }
}
