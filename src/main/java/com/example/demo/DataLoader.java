/* Let's add some accounts so that we can log in! */

package com.example.demo;

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
        user.setRoles(Arrays.asList(userRole));
        userRepository.save(user);

        user = new User("admin@admin.com", "password", "Admin",
                "User", true, "admin");
        user.setRoles(Arrays.asList(adminRole));
        userRepository.save(user);

        // adding messages
        Message message = new Message("Who wants an Americano",
                "Hey, I'm about to make a run to Java Junction. Who's in?",
                "J.Lee");
        String strDate = "03-01-19";
        Date date = new SimpleDateFormat("MM-dd-YY").parse(strDate);
        message.setPostedDate(date);
        messageRepository.save(message);

        message = new Message("I am!",
                "@J.Lee Can you get me a shot of espresso? I have a deadline today and missed my morning coffee. Thanks!",
                "M.Choi");
        strDate = "03-01-19";
        date = new SimpleDateFormat("MM-dd-YY").parse(strDate);
        message.setPostedDate(date);
        messageRepository.save(message);

        message = new Message("Election day",
                "About to get some donuts for the election view party, who's interested?",
                "H.Hermoine");
        strDate = "11-04-18";
        date = new SimpleDateFormat("MM-dd-YY").parse(strDate);
        message.setPostedDate(date);
        messageRepository.save(message);

        message = new Message("Japanese stationary",
                "I am looking for a new planner for the new year. Are there any japanese brands that you recommend?",
                "R.Weasley");
        strDate = "01-15-19";
        date = new SimpleDateFormat("MM-dd-YY").parse(strDate);
        message.setPostedDate(date);
        messageRepository.save(message);

        message = new Message("Matcha coffee?",
                "@R.Weasley I have no japanese planner but I can offer you a matcha green tea latte..",
                "C.Weasley");
        strDate = "01-15-19";
        date = new SimpleDateFormat("MM-dd-YY").parse(strDate);
        message.setPostedDate(date);
        messageRepository.save(message);

        message = new Message(" :x ",
                "@C.Weasley Oh you fancy shmancy.",
                "R.Weasley");
        strDate = "01-15-19";
        date = new SimpleDateFormat("MM-dd-YY").parse(strDate);
        message.setPostedDate(date);
        messageRepository.save(message);

    }
}
