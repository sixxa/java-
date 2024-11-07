package com.sixa.joblisting.repository;

import com.sixa.joblisting.model.Post;
import org.springframework.stereotype.Component;

import java.util.List;

public interface SearchRepository {

    List<Post> findByText(String text);

}
