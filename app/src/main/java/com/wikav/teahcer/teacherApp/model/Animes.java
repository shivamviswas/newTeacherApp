package com.wikav.teahcer.teacherApp.model;

/**
 * Created by Aws on 11/03/2018.
 */

public class Animes {

    private String name ;
    private String Description;
    private String rating;
    private String id;
    private String categorie;
    private String studio ;
    private String image_url;

    public Animes() {
    }

    public Animes(String name, String description, String rating, String nb_episode, String categorie, String studio, String image_url) {
        this.name = name;
        Description = description;
        this.rating = rating;
        this.id = nb_episode;
        this.categorie = categorie;
        this.studio = studio;
        this.image_url = image_url;
    }


    public String getName() {
        return name;
    }

    public String getDescription() {
        return Description;
    }

    public String getRating() {
        return rating;
    }

    public String getIdd() {
        return id;
    }

    public String getCategorie() {
        return categorie;
    }

    public String getStudio() {
        return studio;
    }

    public String getImage_url() {
        return image_url;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public void setId(String nb_episode) {
        this.id = nb_episode;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public void setStudio(String studio) {
        this.studio = studio;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}
