package com.example.demo;

import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Controller
public class HomeController {
    @Autowired
    MessageRepository messageRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CloudinaryConfig cloudc;

    @Autowired
    private UserService userService;

    /* lists all message entries*/
    @RequestMapping("/")
    public String listMessage(Model model) {
        model.addAttribute("messages", messageRepository.findAll());

        // Remember to add this line to assign user!
        if (userService.getUser() != null) {
            model.addAttribute("user_id", userService.getUser().getId());
        }
        return "list";
    }

    /* allows user to load form page*/
    @GetMapping("/add")
    public String messageForm(Model model) {
        model.addAttribute("message", new Message());
        return "form";
    }

    /* method=POST from form.html brings entries back here for processing */
    @PostMapping("/process")
    public String processForm(@Valid @ModelAttribute Message message,
                              BindingResult result,
                              @RequestParam("postedDate") String postedDate,
                              @RequestParam("file") MultipartFile file,
                              Model model,
                              Principal principal) {
        String username = principal.getName();
        if (result.hasErrors()) {
            return "form";  /* posts a new message if entry is valid*/
        }

        // add user
        model.addAttribute("user", userService.getUser());

        // add and save picture
        if (file.isEmpty()) { // if file empty
            messageRepository.save(message);
//            message.setUser(userService.getUser());
            message.setUser(userRepository.findByUsername(username));
            messageRepository.save(message);
        } else {
            try {
                Map uploadResult = cloudc.upload(file.getBytes(), ObjectUtils.asMap("resourcetype", "auto"));
                message.setMessagePic(uploadResult.get("url").toString());
                messageRepository.save(message);
                message.setUser(userRepository.findByUsername(username));
                messageRepository.save(message);
            } catch (IOException e) {
                e.printStackTrace();
                return "redirect:/add";
            }
            messageRepository.save(message);
        }

        // add and save date
        Date date = new Date();
        try {
            date = new SimpleDateFormat("MM-dd-YY").parse(postedDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        message.setPostedDate(date);

        message.setUser(userService.getUser());
        messageRepository.save(message);
        return "redirect:/";    /* redirects the user to main page if valid*/
    }


    /* takes user to the message details page*/
    @RequestMapping("/detail/{id}")
    public String showMessage(@PathVariable("id") long id, Model model) {
        model.addAttribute("message", messageRepository.findById(id).get());
        return "show";
    }

    /* takes user to the message form */
    @RequestMapping("/update/{id}")
    public String updateMessage(@PathVariable("id") long id, Model model) {
        model.addAttribute("message", messageRepository.findById(id).get());
        model.addAttribute("user", userService.getUser());
        return "form";
    }

    /* deletes by ID then redirects the user to main page*/
    @RequestMapping("/delete/{id}")
    public String delMessage(@PathVariable("id") long id) {
        messageRepository.deleteById(id);
        return "redirect:/";
    }

}
