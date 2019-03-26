package com.example.demo.controllers;

import com.example.demo.services.CustomUserDetails;
import com.example.demo.models.User;
import com.example.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@Controller
public class SecurityController {

    @Autowired
    private UserService userService;

    // GET
    @GetMapping("/register")
    public String showRegistrationPage(Model model){
        model.addAttribute("user", new User());
        return "registration";
    }

    // POST
    @PostMapping("/register")
    public String processRegistrationPage(@Valid
                                          @ModelAttribute("user") User user,
                                          BindingResult result,
                                          Model model){
        model.addAttribute("user", user);
        if (result.hasErrors()){
            return "registration";
        }
        else {
            userService.saveUser(user);
            model.addAttribute("message", "User Account Created");
        }
        // where should it go after a successful registration?
        return "redirect:/login";
    }



    /* ATTN Dave or Melisssa -- 
    *
    * Right now when I log in, I am logged in but it first immeidately takes me to
    * path "/style.css"
    *
    * You have to refirectly manually to
    * path "/"
    * and it will work fine.
    * */
    /* Do I need get and post separately for log in? */
    @RequestMapping("/login")
    public String login(Model model){
        return "login";
    }
//    @PostMapping("/login")
//    public String login(@ModelAttribute("user") User user, Model model){
//        model.addAttribute("user", user);
//        return "redirect:/";
//    }


    /* taken from:
     * https://www.baeldung.com/get-user-in-spring-security */
    @GetMapping("/username")
    @ResponseBody
    public String currentUsername(Principal principal){
        return principal.getName();
    }
//    @GetMapping("/username")
//    @ResponseBody
//    public String currentUsernameSimple(HttpServletRequest request){
//        Principal principal = request.getUserPrincipal();
//        return principal.getName();

//    }




    @RequestMapping("/myprofile")
    public String secure(Principal principal, Model model){
        User myuser = ((CustomUserDetails)
                        ((UsernamePasswordAuthenticationToken) principal)
                                .getPrincipal()).getUser();
        model.addAttribute("myuser", myuser);
        return "profile";
    }

    /* Addition for separate log out page */
//    @RequestMapping("/logoutconfirm")
//    public String logoutconfirm(){
//        return "login";
//    }


}
