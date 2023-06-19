package com.example.demo.controller.personAnimaux;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "animal")
public class Animal {
    private String comportement;
    private String crie;

    @XmlElement
    public String getComportement() {
        return comportement;
    }

    public void setComportement(String comportement) {
        this.comportement = comportement;
    }

    @XmlElement
    public String getCrie() {
        return crie;
    }

    public void setCrie(String crie) {
        this.crie = crie;
    }
}
