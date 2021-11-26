package com.itrex.java.lab.crm.repository.impl.data;

import com.itrex.java.lab.crm.entity.Role;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Primary
@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

}
