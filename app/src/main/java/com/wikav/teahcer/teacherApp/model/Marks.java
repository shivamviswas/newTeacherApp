package com.wikav.teahcer.teacherApp.model;

/**
 * Created by Aws on 11/03/2018.
 */

public class Marks {

    private String stName ;
    private String stId;
    private String zero="";
    private String obtainMarrks;

    public String getObtainMarrks() {
        return obtainMarrks;
    }

    public void setObtainMarrks(String obtainMarrks) {
        this.obtainMarrks = obtainMarrks;
    }

    public String getTotalMarks() {
        return totalMarks;
    }

    public void setTotalMarks(String totalMarks) {
        this.totalMarks = totalMarks;
    }

    private String totalMarks;



    public Marks() {
    }

    public String getStName() {
        return stName;
    }
    public String getStId() {
        return stId;
    }

    public String getZero() {
        return zero;
    }

    public void setStName(String name) {
        this.stName = name;
    }
    public void setStId(String nb_episode) {
        this.stId = nb_episode;
    }


}
