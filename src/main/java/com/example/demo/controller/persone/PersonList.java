package com.example.demo.controller.persone;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "personList", namespace = "http://www.tei-c.org/ns/1.0")
public class PersonList {
    private List<Person> persons;

    @XmlElement(name = "person", namespace = "http://www.tei-c.org/ns/1.0")
    public List<Person> getPersons() {
        return persons;
    }

    public void setPersons(List<Person> persons) {
        this.persons = persons;
    }
}


