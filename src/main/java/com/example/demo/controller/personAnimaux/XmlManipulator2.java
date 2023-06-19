package com.example.demo.controller.personAnimaux;

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
public class XmlManipulator2 {

    @Value("classpath:/static/person.xml")
    private Resource resourceFile;

    @GetMapping("/xmlpersoneAnimaux")
    public void xmlpersone(String[] args) {
        System.out.println("xmlpersone");
        try {

            ClassPathResource classPathResource = new ClassPathResource("static/xmlpersoneAnimaux.xml");

            File file = new File(classPathResource.getFile().toString());
            JAXBContext jaxbContext = JAXBContext.newInstance(PersonList.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            PersonList personList = (PersonList) jaxbUnmarshaller.unmarshal(file);

            List<Person> persons = personList.getPersons();
            for (Person person : persons) {
                System.out.println("Nom : " + person.getName());
                System.out.println("Âge : " + person.getAge());

                AnimalList animaux = person.getAnimaux();
                List<Animal> animals = animaux.getAnimals();
                for (Animal animal : animals) {
                    System.out.println("Comportement de l'animal : " + animal.getComportement());
                    System.out.println("Crie de l'animal : " + animal.getCrie());
                }
            }
        } catch (JAXBException | IOException e) {
            e.printStackTrace();
        }
    }
}
