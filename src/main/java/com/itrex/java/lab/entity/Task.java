package com.itrex.java.lab.entity;

import java.time.LocalDate;

public class Task {

    private Integer id;
    private String title;
    private Status status;
    private LocalDate dedline;
    private String info;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDate getDedline() {
        return dedline;
    }

    public void setDedline(LocalDate dedline) {
        this.dedline = dedline;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return "\nTask{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", status=" + status +
                ", dedline=" + dedline +
                ", info='" + info + '\'' +
                '}';
    }
}
