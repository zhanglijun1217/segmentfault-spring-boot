package com.segmentfault.springbootlesson6.service;

import com.segmentfault.springbootlesson6.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author 夸克
 * @date 2018/11/11 18:33
 */
@Service
@EnableTransactionManagement
public class UserServiceImpl implements UserService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean save(User user) {
        // 预编译Statement 回调中进行设置参数和查询
        jdbcTemplate.execute("Insert into user(name, age) values (?,?);", new PreparedStatementCallback<Object>() {

            @Override
            public Object doInPreparedStatement(PreparedStatement preparedStatement) throws SQLException, DataAccessException {
                preparedStatement.setString(1, user.getName());
                preparedStatement.setInt(2, user.getAge());

                return preparedStatement.executeUpdate() > 0;
            }

        });
        return true;
    }
}
