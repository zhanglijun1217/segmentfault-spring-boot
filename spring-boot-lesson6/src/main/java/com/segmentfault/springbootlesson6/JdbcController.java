package com.segmentfault.springbootlesson6;

import com.segmentfault.springbootlesson6.domain.User;
import com.segmentfault.springbootlesson6.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 夸克
 * @date 2018/11/11 11:38
 */
@RestController
public class JdbcController {

    // spring boot 比较好的好处是自动装配
    // 比较原生的java api的连接方式
    @Autowired
    private DataSource dataSource;

    @Resource
    private UserService userService;

    @RequestMapping("/user/get")
    public Map<String, Object> getUser(@RequestParam(value = "id", defaultValue = "1") String id) {

        Map<String, Object> data = new HashMap<>();
        Connection connection = null;
        try {
            // 创建数据库连接
            connection = dataSource.getConnection();
            // 创建statement （但是这样直接拼参数有可能进行sql注入） 应该用预编译的方式
//            Statement statement = connection.createStatement();
//            ResultSet resultSet = statement.executeQuery("select id, name, age from user");

            // 预编译的statement 可以防止sql注入
            PreparedStatement preparedStatement = connection.prepareStatement("select id, name, age from user where id = ?");
            preparedStatement.setString(1, id);
            // 执行获取resultSet
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {

                int id_ = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");

                data.put("id", id_);
                data.put("name", name);
                data.put("age", age);
            }
        } catch (SQLException s) {
            s.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

        return data;
    }


    @RequestMapping("/user/add")
    public boolean addUser(@RequestBody User user) {
        return userService.save(user);
    }

}
