package com.example.kutudp.tinhangngay.models;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by kutudp on 1/28/2018.
 */

public class News implements Serializable {
    private int id;
    private String name;
    private String preview;
    private String link;
    private String picture;
    private String dateCreated;
    private int idCategory;
    private int idResource;

    public News(int id, String name, String preview, String link, String picture, String dateCreated, int idCategory, int idResource) {
        this.id = id;
        this.name = name;
        this.preview = preview;
        this.link = link;
        this.picture = picture;
        this.dateCreated = dateCreated;
        this.idCategory = idCategory;
        this.idResource = idResource;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPreview() {
        return preview;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public int getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(int idCategory) {
        this.idCategory = idCategory;
    }

    public int getIdResource() {
        return idResource;
    }

    public void setIdResource(int idResource) {
        this.idResource = idResource;
    }

    public News() {
    }
}
