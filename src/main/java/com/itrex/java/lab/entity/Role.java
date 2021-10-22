package com.itrex.java.lab.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "role", schema = "CRM")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "role_name")
    private String roleName;

    @OneToMany(mappedBy = "role")
    private List<User> users;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName.toUpperCase();
    }

    @Override
    public String toString() {
        return "\nRole{" +
                "id=" + id +
                ", roleName='" + roleName + '\'' +
                '}';
    }
}

