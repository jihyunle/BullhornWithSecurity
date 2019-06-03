package com.example.demo;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@Entity
@Access(AccessType.PROPERTY)
@Table(schema = "TEST", name="USER_DATA")
//@Table(name="USER")
public class User
        implements Serializable
{

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ID", unique = true, nullable = false) // w/o this gives error: No identifier specified for entity: com.example.demo.User
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;


    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "enabled")
    private boolean enabled;

    @Column(name = "username")
    private String username;

    @Column(name = "userpicture")
    private String userpicture;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Collection<Role> roles;


    @OneToMany(mappedBy = "user",
            cascade = CascadeType.REMOVE,
            fetch = FetchType.EAGER)
    public Set<Message> messages;


    // BONUS addition for adding followers
    /*2
    option 1
    * https://gist.github.com/ffbit/3343910
    * */
    private Set<User> followers;

    private Set<User> following;


    // constructor
    public User(){ }


    /* Anytime we create an overloaded constructor we need to have a default no-arg constructor.
     *
      * Also, a java bean must have private variables, no-arg constructor, and getters and setters. */
    public User(String email, String password, String firstName, String lastName, boolean enabled, String username) {
        this.setEmail(email);
        this.setPassword(password);
        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.setEnabled(enabled);
        this.setUsername(username);

        this.followers = new HashSet<User>();
        this.followers = new HashSet<User>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        BCryptPasswordEncoder passwordEncoder =
                new BCryptPasswordEncoder();
        this.password = passwordEncoder.encode(password);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Collection<Role> getRoles() {
        return roles;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }

    public String getUserpicture() {
        return userpicture;
    }

    public void setUserpicture(String userpicture) {
        this.userpicture = userpicture;
    }

    public Set<Message> getMessages() {
        return messages;
    }

    public void setMessages(Set<Message> messages) {
        this.messages = messages;
    }

    // BONUS addition for adding followers
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(schema = "TEST", name = "USER_RELATIONS",
    joinColumns = @JoinColumn(name = "FOLLOWED_ID"),
    inverseJoinColumns = @JoinColumn(name = "FOLLOWER_ID"))
    public Set<User> getFollowers() {
        return followers;
    }

    public void setFollowers(Set<User> followers) {
        this.followers = followers;
    }

    public void addFollower(User follower) {
        followers.add(follower);
        follower.following.add(this); // don't understand this part
    }

    @ManyToMany(mappedBy = "followers")
    public Set<User> getFollowing() {
        return following;
    }

    public void setFollowing(Set<User> following) {
        this.following = following;
    }

    public void addFollowing(User followed) {
        followed.addFollower(this);
    }

    // option 2 for adding followers.
    /*
    * https://www.programcreek.com/2014/08/leetcode-design-twitter-java/
    * */
    // the following section is stand-alone
    HashMap<Integer, HashSet<Integer>> userMap; // user & followees

    // follower follows a followee. if the op is invalid it should be a no-op
    public void follow(int followerId, int followeeId){
        HashSet<Integer> set = userMap.get(followerId);
        if (set==null){
            set = new HashSet<Integer>();
            userMap.put(followerId, set);
        }
        set.add(followeeId);
    }

    // follower unfollows a followee. if the op is invalid it should be a no-op
    public void unfollow(int followerId, int followeeId){
        if (followerId==followeeId){
            return ;
        }
        HashSet<Integer> set = userMap.get(followerId);
        if (set==null){
            return ;
        }
        set.remove(followeeId);
    }
}




















