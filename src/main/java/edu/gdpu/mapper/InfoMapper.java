package edu.gdpu.mapper;

import edu.gdpu.domain.Punch;
import edu.gdpu.entity.Info;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface InfoMapper {

    @Insert("insert into Info(username,password,school,location,ob,health,de,userId,temp) " +
            "values(#{info.username},#{info.password},#{info.school},#{info.location},#{info.ob}," +
            "#{info.health},#{info.de},#{userId},#{info.temp})")
    void save(@Param("info") Info info, @Param("userId")Integer userId);

    @Select("select * from Info where userId=#{userId}")
    Info findByUserId(Integer userId);

    @Select("select * from Info where username=#{username}")
    Info findByUsername(Info info);

    @Select("select * from Info ")
    List<Punch> findAll();

    @Update("update Info set username=#{info.username},password=#{info.password},school=#{info.school}," +
            "location=#{info.location},ob=#{info.ob},health=#{info.health},de=#{info.de},temp=#{info.temp} " +
            "where userId=#{userId}")
    void update(@Param("info") Info info, @Param("userId")Integer userId);

    @Delete("delete from Info where username=#{username}")
    void delete(Punch punch);

    @Delete("delete from Info where userId=#{userId}")
    void deleteByUserId(Integer userId);
}
