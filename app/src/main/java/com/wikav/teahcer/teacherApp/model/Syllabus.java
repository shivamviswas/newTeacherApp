package com.wikav.teahcer.teacherApp.model;

/**
 * Created by Aws on 11/03/2018.
 */

public class Syllabus {

   private String subjectId;
   private String subject;
   private String syllabusComplete;
   private String totalSyllabus;

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSyllabusComplete() {
        return syllabusComplete;
    }

    public void setSyllabusComplete(String syllabusComplete) {
        this.syllabusComplete = syllabusComplete;
    }

    public String getTotalSyllabus() {
        return totalSyllabus;
    }

    public void setTotalSyllabus(String totalSyllabus) {
        this.totalSyllabus = totalSyllabus;
    }
}
