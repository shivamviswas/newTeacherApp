package com.wikav.teahcer.teacherApp.model;

/**
 * Created by Aws on 11/03/2018.
 */

public class Attendence {

    private String stundentName;
    private String stId;
    private boolean isPresent;


    public Attendence() {
    }

    public String getStundentName() {
        return stundentName;
    }


    public String getStId() {
        return stId;
    }


    public boolean isPresent() {
        return isPresent;
    }

    public void setPresent(boolean present) {
        isPresent = present;
    }

    public void setStundentName(String name) {
        this.stundentName = name;
    }


    public void setStId(String nb_episode) {
        this.stId = nb_episode;
    }

}
