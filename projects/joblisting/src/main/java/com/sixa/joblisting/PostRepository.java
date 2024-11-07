package com.sixa.joblisting;

import com.sixa.joblisting.model.Post;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;

public interface PostRepository extends MongoRepository<Post, String> {

}
