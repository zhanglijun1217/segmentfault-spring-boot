package com.segmentfault.springbootlesson7.mapper;

import com.segmentfault.springbootlesson7.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

/**
 * @author 夸克
 * @date 2018/11/18 15:34
 */
@Mapper
public interface AnnotationUserMapper {

    @Results(value = {
            @Result(property = "id", column = "id", id = true),
            @Result(property = "name", column = "name"),
            @Result(property = "age", column = "age")
    })
    @Select(value = "SELECT id,name,age FROM user WHERE id = #{id}")
    User selectUser(Long id);
}
