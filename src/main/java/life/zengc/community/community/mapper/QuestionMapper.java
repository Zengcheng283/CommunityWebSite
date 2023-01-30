package life.zengc.community.community.mapper;

import life.zengc.community.community.model.Question;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface QuestionMapper {

    /**
     * 向数据库question表插入一条数据
     * @param question
     */
    @Insert("insert into question(id, title, description, gmt_create, gmt_modified, creator, tag) values (#{id}, #{title}, #{description}, #{gmtCreate}, #{gmtModified}, #{creator}, #{tag})")
    void create(Question question);

    /**
     * 查询发起的问题，返回列表
     * @param offset
     * @param size
     * @return
     */
    @Select("select * from question limit #{offset}, #{size}")
    List<Question> list(@Param(value = "offset") Integer offset, @Param(value = "size") Integer size);

    /**
     * 查询问题数量，返回数字
     * @return
     */
    @Select("select count(1) from question")
    Integer count();

    /**
     * 通过用户id查询其提交的问题
     * @param offset
     * @param size
     * @param id
     * @return
     */
    @Select("select * from question where creator = #{id} limit #{offset}, #{size}")
    List<Question> listById(@Param(value = "offset") Integer offset,
                        @Param(value = "size") Integer size,
                        @Param(value = "id") String id);


    /**
     * 根据用户id查询问题数量
     * @param id
     * @return
     */
    @Select("select count(1) from question where creator = #{id}")
    Integer countById(@Param(value = "id") String id);

    /**
     * 根据id查询问题详情
     * @param id
     * @return
     */
    @Select("select * from question where id = #{id}")
    Question getById(@Param(value = "id") String id);

    @Update("update question set title = #{title}, description = #{description}, tag = #{tag}, gmt_modified = #{gmtModified} where id = #{id}")
    int update(Question question);

    @Update("update question set view_count = view_count + 1 where id = #{id}")
    void updateViewCount(@Param(value = "id") String id);

    @Select("select * from question where id = #{parentId}")
    Question selectByPId(@Param(value = "parentId") String parentId);

    @Update("update question set comment_count = comment_count + 1 where id = #{id}")
    void incCommentCount(Question resultQuestion);

    @Select("select * from question where id = #{id}")
    Question selectById(@Param(value = "id") String id);

    @Delete("delete from question where id = #{id}")
    int deleteById(@Param(value = "id") String id);

    /**
     * 查询相关问题
     * mybatis中，在''中添加变量需要使用$
     * @param tags
     * @return
     */
    @Select("select * from question where tag regexp #{tags} and id != #{id}")
    List<Question> selectRelated(@Param(value = "tags") String tags, @Param(value = "id") String id);

    @Select("select * from question where creator = #{creator}")
    List<Question> getByCreator(@Param(value = "creator") String creator);

    @Select("select creator from question where id = #{id}")
    String getCreatorById(@Param(value = "id") String id);
}
