package com.itrex.java.lab.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * The Task class is responsible for the task's for users.
 * task and user are linked in a cross table crm.user_task
 */

@Entity
@Table(name = "task", schema = "CRM")
@Getter
@Setter
@EqualsAndHashCode(exclude = {"users"})
public class Task implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "deadline")
    private LocalDate deadline;

    private String info;

    @ManyToMany(mappedBy = "tasks")
    private List<User> users = new ArrayList<>();

    @Override
    public String toString() {
        return "\nTask{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", status=" + status +
                ", dedline=" + deadline +
                ", info='" + info + '\'' +
                '}';
    }

}
