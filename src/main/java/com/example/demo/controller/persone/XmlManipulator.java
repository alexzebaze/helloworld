package com.example.demo.controller.persone;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
public class XmlManipulator {

    @Value("classpath:/static/person.xml")
    private Resource resourceFile;

    @GetMapping("/xmlpersone")
    public void xmlpersone(String[] args) {
        System.out.println("xmlpersone");
        try {

            ClassPathResource classPathResource = new ClassPathResource("static/person.xml");

            File file = new File(classPathResource.getFile().toString());
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
