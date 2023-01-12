package life.zengc.community.community.mapper;

import life.zengc.community.community.model.Comment;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CommentMapper {
    @Insert("insert into comment(id, parent_id, type, commentator, gmt_create, gmt_modified, like_count, content) values (#{id}, #{parentId}, #{type}, #{commentator}, #{gmtCreate}, #{gmtModified}, #{likeCount}, #{content})")
    void insert(Comment comment);

    @Select("select * from comment where parent_id = #{parentId}")
    Comment selectByPId(@Param(value = "parentId") String parentId);

    @Select("select * from comment where id = #{id}")
    Comment selectById(@Param(value = "id") String id);

    @Select("select * from comment where parent_id = #{id} and type = #{type} ORDER BY gmt_create DESC")
    List<Comment> selectByPIdAndType(@Param(value = "id") String id, @Param(value = "type") Integer type);
}
