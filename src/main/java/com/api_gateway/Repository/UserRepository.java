package com.api_gateway.Repository;


import com.api_gateway.Entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

@Service
public interface UserRepository extends MongoRepository<User, ObjectId> {
    User findByUsername(String username);
}
