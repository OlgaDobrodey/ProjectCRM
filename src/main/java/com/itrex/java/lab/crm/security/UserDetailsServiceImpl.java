package com.itrex.java.lab.crm.security;

import com.itrex.java.lab.crm.entity.User;
import com.itrex.java.lab.crm.repository.impl.data.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("userDetailsServiceImpl")
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {

        User user = userRepository.findUserByLogin(login).orElseThrow(()->
                new UsernameNotFoundException("User doen't exists"));
        return SecurityUser.fromUser(user);
    }

}
