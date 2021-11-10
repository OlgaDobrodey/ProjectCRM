package com.itrex.java.lab.dto;

import com.itrex.java.lab.entity.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * The Task class is responsible for the task's for users.
 * task and user are linked in a cross table crm.user_task
 */

@NoArgsConstructor
@Getter
@Setter
public class TaskDTO {

    private Integer id;
    private String title;
    private Status status;
    private LocalDate deadline;
    private String info;

    @Override
    public String toString() {
        return "\nTaskDTO{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", status=" + status +
                ", deadline=" + deadline +
                ", info='" + info + '\'' +
                '}';
    }
}
