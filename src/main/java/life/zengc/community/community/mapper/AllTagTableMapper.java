package life.zengc.community.community.mapper;

import life.zengc.community.community.model.AllTagTable;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AllTagTableMapper {

    @Select("select * from alltagtable")
    List<AllTagTable> selectAll();

    @Select("select * from alltagtable where id = #{idKey}")
    AllTagTable selectById(@Param(value = "idKey") String idKey);

    @Insert("insert into alltagtable(id, name) values (#{idKey}, #{alTagName})")
    void insert(String idKey, String alTagName);

    @Select("select * from alltagtable where name = #{alTagName}")
    AllTagTable selectByName(@Param(value = "alTagName") String alTagName);
}
