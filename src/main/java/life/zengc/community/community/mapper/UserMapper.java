package life.zengc.community.community.mapper;
import life.zengc.community.community.model.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {

    /**
     * 用户状态插入数据库
     * @param user
     */
    @Insert("insert into user(id, name, account_id, token, gmt_create, gmt_modified, avatar_url) values (#{id}, #{name}, #{accountId}, #{token}, #{gmtCreate}, #{gmtModified}, #{avatarUrl})")
    public void insert(User user);

    @Select("select * from user where token = #{token}")
    User findByToken(@Param("token") String token);

    @Select("select * from user where id = #{id}")
    User findById(@Param("id") String id);

    @Select("select * from user where account_id = #{accountId}")
    User findByAccountId(@Param("accountId") String accountId);

    @Update("update user set name = #{name}, token = #{token}, gmt_modified = #{gmtModified}, avatar_url = #{avatarUrl} where account_id = #{accountId}")
    void update(User dBuser);

    @Select("select * from user where token = #{token}")
    User selectByToken(@Param("token") String token);
}
