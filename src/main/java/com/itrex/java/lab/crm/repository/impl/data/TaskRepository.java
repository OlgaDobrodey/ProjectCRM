package com.itrex.java.lab.crm.repository.impl.data;

import com.itrex.java.lab.crm.entity.Task;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Primary
public interface TaskRepository extends JpaRepository<Task, Integer> {

    List<Task> findTasksByUsers_Id(Integer Id);

}
