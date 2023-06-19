package com.example.demo.controller.personAnimaux;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "person")
public class Person {
    private String name;
    private int age;
    private AnimalList animaux;

    @XmlElement
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlElement
    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @XmlElement(name = "animaux")
    public AnimalList getAnimaux() {
        return animaux;
    }

    public void setAnimaux(AnimalList animaux) {
        this.animaux = animaux;
    }
}
