package edu.gdpu.mapper;

import edu.gdpu.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

/**
 * @author ilanky
 * @date 2020年 05月16日 06:06:03
 */
@Mapper
public interface UserMapper {

    @Options(useGeneratedKeys = true,keyProperty = "id",keyColumn = "id")
    @Insert("insert into User(username,password,email) values (#{username},#{password},#{email})")
    void save(User user);

    @Select("select * from User where username=#{username}")
    User findByUsername(String username);

    @Select("select * from User where id=#{id}")
    User findById(Integer id);
}
