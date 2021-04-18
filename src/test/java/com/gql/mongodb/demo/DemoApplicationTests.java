package com.gql.mongodb.demo;

import com.gql.mongodb.demo.entity.User;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;
import java.util.regex.Pattern;

/**
 * 基于mongoTemplate实现CRUD
 */
@SpringBootTest
class DemoApplicationTests {

    // 注入mongoTemplate
    @Autowired
    private MongoTemplate mongoTemplate;

    // 添加记录
    @Test
    public void create() {
        User user = new User();
        user.setAge(21);
        user.setName("Hudie");
        user.setEmail("Hudie@qq.com");
        User user1 = mongoTemplate.insert(user);
        System.out.println(user1);
    }

    // 查询所有记录
    @Test
    public void findAll() {
        List<User> list = mongoTemplate.findAll(User.class);
        System.out.println(list);
    }

    // 根据id查询
    @Test
    public void findId() {
        User user = mongoTemplate.findById("607b7469ef30607aa5bb9a1b", User.class);
        System.out.println(user);
    }

    // 条件查询
    @Test
    public void findUserList() {
        Query query = new Query(Criteria.where("name").is("Hudie")
                .and("age").is(21));

        List<User> list = mongoTemplate.find(query, User.class);
        System.out.println(list);
    }

    // 模糊查询
    @Test
    public void findLikeUserList() {
        String name = "di";
        String regex = String.format("%s%s%s", "^.*", name, ".*$");
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Query query = new Query(
                Criteria.where("name").regex(pattern));
        List<User> userList = mongoTemplate.find(query, User.class);
        System.out.println(userList);
    }

    // 分页查询
    @Test
    public void findUsersPage() {
        String name = "di";
        // 当前页
        int pageNo = 1;
        // 每页显示记录数
        int pageSize = 10;
        // 模糊条件构造
        String regex = String.format("%s%s%s", "^.*", name, ".*$");
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Query query = new Query(Criteria.where("name").regex(pattern));
        // 查询记录数
        long count = mongoTemplate.count(query, User.class);
        // 查询开始位置+每页显示记录数
        List<User> users = mongoTemplate.find(
                query.skip((pageNo - 1) * pageSize).limit(pageSize), User.class);
        System.out.println(count);
        System.out.println(users);
    }

    //修改
    @Test
    public void updateUser() {
        // 1.根据id查询到记录
        User user = mongoTemplate.findById("607b7469ef30607aa5bb9a1b", User.class);
        // 2.设置修改值
        user.setName("test01");
        user.setAge(23);
        user.setEmail("test@qq.com");
        // 3.调用方法实现修改
        Query query = new Query(Criteria.where("_id").is(user.getId()));
        Update update = new Update();
        update.set("name", user.getName());
        update.set("age", user.getAge());
        update.set("email", user.getEmail());
        UpdateResult result = mongoTemplate.upsert(query, update, User.class);
        // 返回受影响的行数
        long count = result.getModifiedCount();
        System.out.println(count);
    }

    //删除操作
    @Test
    public void deleteUser() {
        Query query =
                new Query(Criteria.where("_id").is("607b7469ef30607aa5bb9a1b"));
        DeleteResult result = mongoTemplate.remove(query, User.class);
        // 返回受影响的行数
        long count = result.getDeletedCount();
        System.out.println(count);
    }
}
