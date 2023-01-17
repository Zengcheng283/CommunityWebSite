package life.zengc.community.community.mapper;

import life.zengc.community.community.model.Notification;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NotificationMapper {

    @Insert("insert into notification(id, notifier, receiver, outer_id, type, gmt_create, status) values (#{id}, #{notifier}, #{receiver}, #{outerId}, #{type}, #{gmtCreate}, #{status})")
    void insert(Notification notification);

    @Select("select * from notification where id = #{id}")
    Notification selectById(@Param(value = "id") String id);

    @Update("update notification set status = 1 where id = #{id}")
    void changeStatusById(@Param(value = "id") String id);

    @Select("select count(*) from notification where receiver = #{id}")
    Integer countById(@Param(value = "id") String id);

    @Select("select * from notification where receiver = #{id} limit #{offset}, #{size}")
    List<Notification> listById(@Param(value = "offset") Integer offset,
                            @Param(value = "size") Integer size,
                            @Param(value = "id") String id);

    @Select("select count(*) from notification where receiver = #{id} and status = 0")
    Integer countUndoById(String id);

    @Select("select * from notification where outer_id = #{id}")
    Notification getByOuterId(@Param(value = "id") String id);
}
