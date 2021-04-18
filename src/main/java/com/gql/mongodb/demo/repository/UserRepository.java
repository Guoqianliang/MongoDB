package com.gql.mongodb.demo.repository;

import com.gql.mongodb.demo.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @Description:
 * @author Guoqianliang
 * @date 9:02 - 2021/4/18
 */
public interface UserRepository extends MongoRepository<User, String> {
    
}
