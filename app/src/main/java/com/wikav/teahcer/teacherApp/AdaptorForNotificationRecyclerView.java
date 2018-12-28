package com.wikav.teahcer.teacherApp;

public class AdaptorForNotificationRecyclerView {
    private String Head;
    private String Decp;

    public AdaptorForNotificationRecyclerView(String head, String decp) {
        this.Head = head;
        this.Decp = decp;
    }

    public String getHead() {
        return Head;
    }

    public String getDecp() {
        return Decp;
    }
}
