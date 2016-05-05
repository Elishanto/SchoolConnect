package com.elishanto.schoololoconnect.adapter;

/**
 * Created by elishanto on 04/02/16.
 */
public class Homework {
    private String created;
    private String delivery;
    private String subject;
    private String desc;
    private String full;

    public Homework(String created, String delivery, String subject, String desc, String full) {
        this.created = created;
        this.delivery = delivery;
        this.subject = subject;
        this.desc = desc;
        if(full.equals("Нет полного описания задания"))
            this.full = "";
        else
            this.full = full;
    }

    public String getCreated() {
        return created;
    }

    public String getDelivery() {
        return delivery;
    }

    public String getSubject() {
        return subject;
    }

    public String getDesc() {
        return desc;
    }

    public String getFull() {
        return full;
    }
}
