package com.example.spring_boot_jwt.service;

import com.example.spring_boot_jwt.model.User;
import com.example.spring_boot_jwt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MyUserDetailsService implements UserDetailsService {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public MyUserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = new User();
       // List<SimpleGrantedAuthority> role = null;
        List<GrantedAuthority> authorities = null;
        try {
            User customer = userRepository.findUserByEmail(email);
            if (customer != null) {
              //  role = Collections.singletonList(new SimpleGrantedAuthority("ROLE_"+customer.getRole()));
                 authorities = customer.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                        .collect(Collectors.toList());

                user.setEmail(customer.getEmail());
                user.setPassword(customer.getPassword());
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
      //  assert authorities != null;
        return new MyUserDetails(user.getEmail(), user.getPassword(), authorities);
    }

}
