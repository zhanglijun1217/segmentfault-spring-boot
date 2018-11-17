package com.segmentfault.springbootlesson7.demo;

import com.segmentfault.springbootlesson7.entity.User;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.EncodedResource;

import java.io.Reader;
import java.util.Properties;

/**
 * @author 夸克
 * @date 2018/11/18 01:44
 */
public class MyBatisXmlConfigurationDemo {

    public static void main(String[] args) throws Exception {

        /**
         * 利用spring的接口拿到mybatis-config.xml文件中的配置
         */

        // spring的resourceLoader的接口
        ResourceLoader resourceLoader = new DefaultResourceLoader();
        // 注意这个getResource中的路径 classpath:后面是一个反斜杠
        Resource resource = resourceLoader.getResource("classpath:/mybatis/mybatis-config.xml");
        // 对编码设置一下
        EncodedResource encodedResource = new EncodedResource(resource, "UTF-8");

        // 拿到reader流
        Reader reader = encodedResource.getReader();

        /**
         * mybatis api 获取sqlSession
         */
        SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
        // 针对properties参数，这里传入一个new Properties()即可，因为在Configuration类中会赋上在xml中配置的值
        SqlSessionFactory build = sqlSessionFactoryBuilder.build(reader, "dev", new Properties());
        SqlSession sqlSession = build.openSession();

        User user = sqlSession.selectOne("com.segmentfault.springbootlesson7.mapper.UserMapper.selectOneUser", 6L);

        System.out.println(user);
        sqlSession.close();

    }
}
