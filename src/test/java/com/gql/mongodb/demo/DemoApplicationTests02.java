package com.gql.mongodb.demo;

import com.gql.mongodb.demo.entity.User;
import com.gql.mongodb.demo.repository.UserRepository;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;
import java.util.regex.Pattern;
/**
 * 基于userRepository实现CRUD
 */
@SpringBootTest
class DemoApplicationTests02 {

    // 注入userRepository
    @Autowired
    private UserRepository userRepository;

    // 添加记录
    @Test
    public void create() {
        User user = new User();
        user.setAge(21);
        user.setName("胡蝶");
        user.setEmail("hudie@163.com");
        User user1 = userRepository.save(user);
        System.out.println(user1);
    }

    // 查询所有记录
    @Test
    public void findAll() {
        List<User> userList = userRepository.findAll();
        System.out.println(userList);
    }

    // 根据id查询
    @Test
    public void findId() {
        User user = userRepository.findById("607b87662f45b658feccd2e7").get();
        System.out.println(user);
    }

    // 条件查询
    @Test
    public void findUserList() {
        User user = new User();
        user.setName("胡蝶");
        user.setAge(21);
        Example<User> userExample = Example.of(user);
        List<User> userList = userRepository.findAll(userExample);
        System.out.println(userList);
    }

    // 模糊查询
    @Test
    public void findLikeUserList() {
        //设置模糊查询匹配规则
        ExampleMatcher matcher = ExampleMatcher.matching()
                // 设置模糊查询,若不加参数是完全匹配
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                // 忽略大小写
                .withIgnoreCase(true);
        User user = new User();
        user.setName("蝶");
        Example<User> userExample = Example.of(user, matcher);
        List<User> userList = userRepository.findAll(userExample);
        System.out.println(userList);
    }

    // 分页查询
    @Test
    public void findUsersPage() {
        // 根据年龄降序排列
        Sort sort = Sort.by(Sort.Direction.DESC, "age");
        // 0为第一页,每页10条记录
        Pageable pageable = PageRequest.of(0, 10, sort);

        // 设置模糊查询匹配规则
        ExampleMatcher matcher = ExampleMatcher.matching() //构建对象
                //改变默认字符串匹配方式：模糊查询
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase(true); //改变默认大小写忽略方式：忽略大小写
        User user = new User();
        user.setName("蝶");
        Example<User> userExample = Example.of(user, matcher);
        //创建实例
        Page<User> pages = userRepository.findAll(userExample, pageable);
        System.out.println(pages);
    }

    //修改
    @Test
    public void updateUser() {
        User user = userRepository.findById("607b87662f45b658feccd2e7").get();
        user.setName("胡小蝶");
        user.setAge(21);
        user.setEmail("diedie@qq.com");
        User save = userRepository.save(user);
        System.out.println(save);
    }

    //删除操作
    @Test
    public void deleteUser() {
        userRepository.deleteById("607b87662f45b658feccd2e7");
    }
}
