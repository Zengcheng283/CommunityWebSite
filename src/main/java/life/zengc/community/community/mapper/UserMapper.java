package life.zengc.community.community.mapper;

import life.zengc.community.community.model.Person;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    /**
     * 用户状态插入数据库
     * @param person
     */
    @Insert("insert into user(name, account_id, token, gmt_create, gmt_modified) values (#{name}, #{accountId}, #{token}, #{gmtCreate}, #{gmtModified})")
    public void insert(Person person);

    @Select("select * from user where token = #{token}")
    Person findByToken(@Param("token") String token);
}
