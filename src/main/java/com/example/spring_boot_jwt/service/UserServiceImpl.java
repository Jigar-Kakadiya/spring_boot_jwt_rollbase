package com.example.spring_boot_jwt.service;

import com.example.spring_boot_jwt.model.CName;
import com.example.spring_boot_jwt.model.CST;
import com.example.spring_boot_jwt.model.ERole;
import com.example.spring_boot_jwt.model.Role;
import com.example.spring_boot_jwt.model.SignUpRequest;
import com.example.spring_boot_jwt.model.User;
import com.example.spring_boot_jwt.repository.CSTRepository;
import com.example.spring_boot_jwt.repository.RoleRepository;
import com.example.spring_boot_jwt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {


    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CSTRepository cstRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public ResponseEntity<Object> save(SignUpRequest signUpRequest) {
        try {
            if (userRepository.existsUserByEmail(signUpRequest.getEmail())) {
                return ResponseEntity
                        .badRequest()
                        .body("Error: Username is already taken!");
            }
            User user  = new User();

            user.setFirstName(signUpRequest.getFirstName());
            user.setLastName(signUpRequest.getLastName());
            String p = bCryptPasswordEncoder.encode(signUpRequest.getPassword());
            user.setPassword(p);
            user.setEmail(signUpRequest.getEmail());

            Set<String> strRoles = signUpRequest.getRole();
            Set<Role> roles = new HashSet<>();
            if (strRoles == null) {
                Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                        .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                roles.add(userRole);
            } else {
                strRoles.forEach(role -> {
                    if ("admin".equals(role)) {
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);
                    } else {
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                    }
                });
            }
            user.setRoles(roles);
            userRepository.save(user);
            return new ResponseEntity<>("User Register Successfully", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<Object> updateUser(Long id, CName cName) {
        User old_user = userRepository.findUsersByIdIs(id);
        User user1 = new User();
        try {
            if (old_user == null) {
                return new ResponseEntity<>("User Not Found", HttpStatus.NOT_FOUND);
            } else {
                user1.setEmail(old_user.getEmail());
                user1.setId(id);
                user1.setFirstName(cName.getFirstName());
                user1.setLastName(cName.getLastName());
                user1.setPassword(old_user.getPassword());
                user1.setRoles(old_user.getRoles());
                userRepository.save(user1);
                return new ResponseEntity<>(user1, HttpStatus.OK);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>("Error", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<Object> createCst(CST cst) {
        User user = userRepository.findUsersByIdIs(cst.getUserId());
        try {
            if (user == null){
                return new ResponseEntity<>("User Not Found", HttpStatus.NOT_FOUND);
            }
            else {
                cstRepository.save(cst);
                return new ResponseEntity<>(cst, HttpStatus.OK);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>("Error", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<Object> cstList() {
        try {
            List<CST> cstList = cstRepository.findAll();
            if (cstList.isEmpty()) {
                return new ResponseEntity<>(cstList, HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(cstList, HttpStatus.OK);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

