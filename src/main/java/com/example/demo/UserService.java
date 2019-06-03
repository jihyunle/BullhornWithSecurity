package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.*;
import java.security.*;
import java.util.Arrays;

@Service // this class performs service tasks or business logics
public class UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public User findByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public Long countByEmail(String email){
        return userRepository.countByEmail(email);
    }

    public User findByUsername(String username){
        return userRepository.findByUsername(username);
    }

    public void saveUser(User user){
        user.setRoles(Arrays.asList(roleRepository.findByRole("USER")));
        user.setEnabled(true);
        userRepository.save(user);
    }

    public void saveAdmin(User user){
        user.setRoles(Arrays.asList(roleRepository.findByRole("ADMIN")));
        user.setEnabled(true);
        userRepository.save(user);
    }

    // returns currently logged in user
    public User getUser(){
        // retrieve the currently authenticated principal via a static call to SecurityContextHolder
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // check if there is an authenticated user
//        if (!(authentication instanceof AnonymousAuthenticationToken)){
        String currentPrincipalName = authentication.getName();
//        }
        User user = userRepository.findByUsername(currentPrincipalName);
        return user;
    }

    // below section added for Gravatar
    /*
     * The following class will provide you with a static method
     * that returns the hex format md5 of an input string
     * See: http://en.gravatar.com/site/implement/images/java/
     * Call this as shown below:
     * String email = "someone@somewhere.com";
     * String hash = MD5Util.md5Hex(email);
     */
    public static String hex(byte[] array) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < array.length; ++i) {
            sb.append(Integer.toHexString((array[i]
                    & 0xFF) | 0x100).substring(1,3));
        }
        return sb.toString();
    }
    public static String md5Hex (String message) {
        try {
            MessageDigest md =
                    MessageDigest.getInstance("MD5");
            return hex (md.digest(message.getBytes("CP1252")));
        } catch (NoSuchAlgorithmException e) {
        } catch (UnsupportedEncodingException e) {
        }
        return null;
    }
}
