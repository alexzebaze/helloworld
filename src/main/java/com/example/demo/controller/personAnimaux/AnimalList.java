package com.example.demo.controller.personAnimaux;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "animalList")
public class AnimalList {
    private List<Animal> animals;

    @XmlElement(name = "animal")
    public List<Animal> getAnimals() {
        return animals;
    }

    public void setAnimals(List<Animal> animals) {
        this.animals = animals;
    }
}
