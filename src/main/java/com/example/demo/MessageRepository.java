package com.example.demo;

import com.example.demo.Message;
import org.springframework.data.repository.CrudRepository;

public interface MessageRepository extends CrudRepository<Message, Long> {

    Message findByTitle(String title);
}
