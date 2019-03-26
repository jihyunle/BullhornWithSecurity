package com.example.demo.repositories;

import com.example.demo.models.Message;
import org.springframework.data.repository.CrudRepository;

public interface MessageRepository extends CrudRepository<Message, Long> {

    Message findByTitle(String title);
}
