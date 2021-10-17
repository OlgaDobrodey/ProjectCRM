package com.itrex.java.lab.entity;

import java.time.LocalDate;
import java.util.Objects;

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
                ", status=" + status.getStatusName() +
                ", dedline=" + dedline +
                ", info='" + info + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id) && Objects.equals(title, task.title) && Objects.equals(status, task.status) && Objects.equals(dedline, task.dedline) && Objects.equals(info, task.info);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, status, dedline, info);
    }
}
