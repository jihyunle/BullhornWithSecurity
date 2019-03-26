/* Let's add some accounts so that we can log in! */

/* It was possiblt to have both DataLoader class and PostConstruct */

package com.example.demo.services;

import com.example.demo.repositories.MessageRepository;
import com.example.demo.repositories.RoleRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.models.Message;
import com.example.demo.models.Role;
import com.example.demo.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

@Component
public class DataLoader implements CommandLineRunner {
    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String...strings) throws Exception{

        // adding roles
        roleRepository.save(new Role("USER"));
        roleRepository.save(new Role("ADMIN"));
        
        Role adminRole = roleRepository.findByRole("ADMIN");
        Role userRole = roleRepository.findByRole("USER");

        // adding users
        User user = new User("jim@jim.com", "password", "Jim",
                "Jimmerson", true, "jim");
        user.setUserpicture("https://www.gravatar.com/avatar/205e460b479e2e5b48aec07710c08d50?s=80");
        user.setRoles(Arrays.asList(userRole));
        userRepository.save(user);

        user = new User("admin@admin.com", "password", "Admin",
                "User", true, "admin");
        user.setRoles(Arrays.asList(adminRole));
        userRepository.save(user);

        // adding messages
        Message message = new Message("Who wants an Americano",
                "Hey, I'm about to make a run to Java Junction. Who's in?",
                "jim");
        String strDate = "03-01-19";
        Date date = new SimpleDateFormat("MM-dd-YY").parse(strDate);
        message.setPostedDate(date);
        message.setUser(userRepository.findByUsername("jim"));
        messageRepository.save(message);


        message = new Message("Election day",
                "About to get some donuts for the election view party, who's interested?",
                "jim");
        strDate = "11-04-18";
        date = new SimpleDateFormat("MM-dd-YY").parse(strDate);
        message.setPostedDate(date);
        message.setUser(userRepository.findByUsername("jim"));
        messageRepository.save(message);

        message = new Message("Japanese stationary",
                "I am looking for a new planner for the new year. Are there any japanese brands that you recommend?",
                "admin");
        strDate = "01-15-19";
        date = new SimpleDateFormat("MM-dd-YY").parse(strDate);
        message.setPostedDate(date);
        message.setUser(userRepository.findByUsername("admin"));
        messageRepository.save(message);

        message = new Message("Matcha coffee?",
                "@admin I have no japanese planner but I can offer you a matcha green tea latte..",
                "jim");
        strDate = "01-15-19";
        date = new SimpleDateFormat("MM-dd-YY").parse(strDate);
        message.setPostedDate(date);
        message.setUser(userRepository.findByUsername("jim"));
        messageRepository.save(message);

        message = new Message(" :x ",
                "@jim Oh you fancy shmancy.",
                "admin");
        strDate = "01-15-19";
        date = new SimpleDateFormat("MM-dd-YY").parse(strDate);
        message.setPostedDate(date);
        message.setUser(userRepository.findByUsername("admin"));
        messageRepository.save(message);

    }
}
