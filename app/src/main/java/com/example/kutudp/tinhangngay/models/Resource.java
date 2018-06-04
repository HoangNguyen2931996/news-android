package com.example.kutudp.tinhangngay.models;

/**
 * Created by kutudp on 1/28/2018.
 */

public class Resource {
    private int id;
    private String name;
    private String link;


    public Resource() {
    }

    public Resource(int id, String name, String link) {
        this.id = id;
        this.name = name;
        this.link = link;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLink() {
        return link;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
