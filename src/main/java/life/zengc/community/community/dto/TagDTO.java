package life.zengc.community.community.dto;

import lombok.Data;

import java.util.List;

@Data
public class TagDTO {
    private String alTagName;

    private List<String> detailTagName;
}
