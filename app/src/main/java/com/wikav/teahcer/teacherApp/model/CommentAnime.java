package com.wikav.teahcer.teacherApp.model;

/**
 * Created by wikav-pc on 7/4/2018.
 */

public class CommentAnime {


        private String name ;
        private String comment;
        private String id;
        private String image_url;

        public CommentAnime() {
        }

        public CommentAnime(String name, String comment, String id, String image_url) {
            this.name = name;
           this.comment=comment;
            this.id = id;

            this.image_url = image_url;
        }


        public String getNameCom() {
            return name;
        }

        public String getComment() {
            return comment;
        }

        public String getIdCom() {
            return id;
        }

        public String getImage_url() {
            return image_url;
        }




    public void setNameCom(String name) {
            this.name = name;
        }

    public void setComment(String comment) {
            this.comment =comment;
        }

        public void setIdCom(String nb_episode) {
            this.id = nb_episode;
        }

    public void setImage_url(String image_url) {
            this.image_url = image_url;
        }
    }


