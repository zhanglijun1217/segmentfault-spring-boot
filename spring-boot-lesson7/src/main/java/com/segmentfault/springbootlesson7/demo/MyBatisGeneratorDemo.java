package com.segmentfault.springbootlesson7.demo;

import com.segmentfault.springbootlesson7.entity_auto_generator.User;
import com.segmentfault.springbootlesson7.entity_auto_generator.UserExample;
import com.segmentfault.springbootlesson7.mapper.UserMapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Properties;

/**
 * @author 夸克
 * @date 2018/11/18 15:07
 */
public class MyBatisGeneratorDemo {

    public static void main(String[] args) throws Exception{

        /**
         * 利用classLoader读取mybatis配置
         */
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        // 获取resource 相对路径
        InputStream inputStream = classLoader.getResourceAsStream("mybatis/mybatis-config.xml");
        InputStreamReader reader = new InputStreamReader(inputStream);

        /**
         * 通过builder制造SqlSession
         */
        SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
        SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBuilder.build(reader, "dev", new Properties());
        SqlSession sqlSession = sqlSessionFactory.openSession();

        /**
         * 操作自动生成的sql操作
         */
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        criteria.andIdEqualTo(6L);

        List<User> users = mapper.selectByExample(userExample);
        System.out.println(users.get(0));
        sqlSession.close();


    }
}
