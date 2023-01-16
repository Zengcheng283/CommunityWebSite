package life.zengc.community.community.service;

import life.zengc.community.community.common.CommonMethods;
import life.zengc.community.community.dto.TagDTO;
import life.zengc.community.community.exception.CustomizeErrorCode;
import life.zengc.community.community.exception.CustomizeException;
import life.zengc.community.community.mapper.AllTagTableMapper;
import life.zengc.community.community.mapper.DetailTagTableMapper;
import life.zengc.community.community.model.AllTagTable;
import life.zengc.community.community.model.DetailTagTable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TagDTOService {

    @Autowired
    private AllTagTableMapper allTagTableMapper;

    @Autowired
    private DetailTagTableMapper detailTagTableMapper;

    @Autowired
    private CommonMethods commonMethods;

    public List<TagDTO> getTag() {
        List<AllTagTable> selectAllList = allTagTableMapper.selectAll();
        return selectAllList.stream().map(selectAll -> {
            List<DetailTagTable> detailTagTableList = detailTagTableMapper.selectByPId(selectAll.getId());
            List<String> detailTagList = detailTagTableList.stream().map(DetailTagTable::getName).collect(Collectors.toList());
            TagDTO tagDTO = new TagDTO();
            tagDTO.setAlTagName(selectAll.getName());
            tagDTO.setDetailTagName(detailTagList);
            return tagDTO;
        }).collect(Collectors.toList());
    }

    public void createOrUpdate(String alTagName, List<String> detailTagName) {
        if (allTagTableMapper.selectByName(alTagName) == null) {
            try {
                String idKey = commonMethods.randomId();
                while (allTagTableMapper.selectById(idKey) != null) {
                    idKey = commonMethods.randomId();
                }
                allTagTableMapper.insert(idKey, alTagName);

                for (String detailTag : detailTagName) {
                    if (detailTagTableMapper.selectByName(detailTag) == null) {
                        String detailIdKey = commonMethods.randomId();
                        while (detailTagTableMapper.selectById(detailIdKey) != null) {
                            detailIdKey = commonMethods.randomId();
                        }
                        detailTagTableMapper.insert(detailIdKey, detailTag, idKey);
                    }
                }
            } catch (Exception ex) {
                log.info(ex.getMessage());
                throw new CustomizeException(CustomizeErrorCode.DATA_BASE_INSERT_ERROR);
            }
        } else {
            AllTagTable allTagTable = allTagTableMapper.selectByName(alTagName);
            try {
                for (String detailTag : detailTagName) {
                    if (detailTagTableMapper.selectByName(detailTag) == null) {
                        String detailIdKey = commonMethods.randomId();
                        while (detailTagTableMapper.selectById(detailIdKey) != null) {
                            detailIdKey = commonMethods.randomId();
                        }
                        detailTagTableMapper.insert(detailIdKey, detailTag, allTagTable.getId());
                    }
                }
            } catch (Exception ex) {
                log.info(ex.getMessage());
                throw new CustomizeException(CustomizeErrorCode.DATA_BASE_INSERT_ERROR);
            }
        }
    }

    public String invaild(String tags) {
        String[] split = tags.split(",");
        List<TagDTO> tagDTOList = getTag();

        // floatMap对外部列表进行循环，在内部使用stream进行二次循环获取内容，收集为列表
        List<String> tagList = tagDTOList.stream().flatMap(tag -> tag.getDetailTagName().stream()).collect(Collectors.toList());

        // filter对列表进行判断，若在tagList中出现tag库不存在的tag，收集起来返回
        String result = Arrays.stream(split).filter(t -> !tagList.contains(t)).collect(Collectors.joining(","));

        return result;
    }
}
