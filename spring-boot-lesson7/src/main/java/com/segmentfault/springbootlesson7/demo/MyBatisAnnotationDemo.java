package com.segmentfault.springbootlesson7.demo;

import com.segmentfault.springbootlesson7.entity.User;
import com.segmentfault.springbootlesson7.mapper.AnnotationUserMapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * @author 夸克
 * @date 2018/11/18 15:51
 */
public class MyBatisAnnotationDemo {

    public static void main(String[] args) {
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
        AnnotationUserMapper mapper = sqlSession.getMapper(AnnotationUserMapper.class);

        User user = mapper.selectUser(6L);
        System.out.println(user);
        sqlSession.close();
    }
}
