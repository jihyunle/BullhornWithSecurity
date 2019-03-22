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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Controller
public class HomeController {
    @Autowired
    MessageRepository messageRepository;

    @Autowired
    CloudinaryConfig cloudc;

    /* lists all message entries*/
    @RequestMapping("/")
    public String listMessage(Model model){
        model.addAttribute("messages", messageRepository.findAll());
        return "list";
    }

    /* allows user to post a new message*/
    @GetMapping("/add")
    public String messageForm(Model model){
        model.addAttribute("message", new Message());
        return "form";
    }

    /* method=POST from form.html brings entries back here for processing */
    @PostMapping("/process")
    public String processForm(@Valid @ModelAttribute Message message,
                              BindingResult result,
                              @RequestParam("postedDate") String postedDate,
                              @RequestParam("file")MultipartFile file,
                              Model model){
        if (result.hasErrors()){
            return "form";  /* posts a new message if entry is valid*/
        }
        // add and save picture
        if (! file.isEmpty()){
            try {
                Map uploadResult = cloudc.upload(file.getBytes(),
                        ObjectUtils.asMap("resourcestype", "auto"));
                message.setMessagePic(uploadResult.get("url").toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // add and save date
        Date date = new Date();
        try {
            date = new SimpleDateFormat("MM-dd-YY").parse(postedDate);

        } catch (Exception e) {
            e.printStackTrace();

        }
        message.setPostedDate(date);

        messageRepository.save(message);
        return "redirect:/";    /* redirects the user to main page if invalid*/
    }


    /* takes user to the message details page*/
    @RequestMapping("/detail/{id}")
    public String showMessage(@PathVariable("id") long id, Model model){
        model.addAttribute("message", messageRepository.findById(id).get());
        return "show";
    }

    /* takes user to the message form */
    @RequestMapping("/update/{id}")
    public String updateMessage(@PathVariable("id") long id, Model model){
        model.addAttribute("message", messageRepository.findById(id).get());
        return "form";
    }

    /* deletes by ID then redirects the user to main page*/
    @RequestMapping("/delete/{id}")
    public String delMessage(@PathVariable("id") long id){
        messageRepository.deleteById(id);
        return "redirect:/";
    }
}
