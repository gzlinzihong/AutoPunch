package edu.gdpu.mapper;

import edu.gdpu.entity.Message;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author ilanky
 * @date 2020年 05月20日 22:39:11
 */
@Mapper
public interface MessageMapper {

    @Select("select * from message order by time DESC limit 1")
    Message findNewest();

    @Insert("insert into message(content) values(#{content})")
    void save(Message message);
}
