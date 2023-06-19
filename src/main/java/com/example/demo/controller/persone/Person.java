package com.example.demo.controller.persone;

import javax.xml.bind.annotation.XmlElement;

public class Person {
    private String name;
    private int age;

    @XmlElement(namespace = "http://www.tei-c.org/ns/1.0")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlElement(namespace = "http://www.tei-c.org/ns/1.0")
    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}