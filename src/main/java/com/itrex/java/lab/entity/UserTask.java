package com.itrex.java.lab.entity;

import java.util.Objects;

public class UserTask {

    private User user;
    private Task task;
    private String info;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return "\nUserTask{" +
                "user=" + user.getId() +
                ", task=" + task.getId() +
                ", info='" + info + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserTask userTask = (UserTask) o;
        return Objects.equals(user, userTask.user) && Objects.equals(task, userTask.task) && Objects.equals(info, userTask.info);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, task, info);
    }
}
