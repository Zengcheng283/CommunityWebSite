package life.zengc.community.community.mapper;

import life.zengc.community.community.model.DetailTagTable;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DetailTagTableMapper {

    @Select("select * from detailtagtable where parent_id = #{parentId} order by name")
    List<DetailTagTable> selectByPId(@Param(value = "parentId") String parentId);

    @Select("select * from detailtagtable where id = #{detailIdKey}")
    DetailTagTable selectById(@Param(value = "detailIdKey") String detailIdKey);

    @Insert("insert into detailtagtable(id, name, parent_id) VALUES (#{detailIdKey}, #{detailTag}, #{idKey})")
    void insert(@Param(value = "detailIdKey") String detailIdKey, @Param(value = "detailTag") String detailTag, @Param(value = "idKey") String idKey);

    @Select("select * from detailtagtable where name = #{detailTag}")
    DetailTagTable selectByName(@Param(value = "detailTag") String detailTag);
}
