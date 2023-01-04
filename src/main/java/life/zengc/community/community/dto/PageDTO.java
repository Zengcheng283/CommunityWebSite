package life.zengc.community.community.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PageDTO {

    private List<QuestionDTO> questionDTOList;

    private boolean showPrevious;

    private boolean showFirstPage;

    private boolean showNextPage;

    private boolean showEndPage;

    private Integer currentPage;

    private List<Integer> pages = new ArrayList<>();

    private Integer totalPage;

    public void setPage(Integer totalCount, Integer page, Integer size) {

        if (totalCount % size == 0) {
            totalPage = totalCount / size;
        } else {
            totalPage = totalCount / size + 1;
        }
        if (page < 1) {
            page = 1;
        }
        if (page > totalPage) {
            page = totalPage;
        }

        currentPage = page;
        pages.add(page);
        for (int i = 1; i <= 3; i++) {
            if (page - i > 0) {
                pages.add(0, page - i);
            }

            if (page + i <= totalPage) {
                pages.add(page + i);
            }
        }

        // 是否展示上一页
        showPrevious = page != 1;
        // 是否展示下一页
        showNextPage = !page.equals(totalPage);
        // 是否展示第一页，如果展示列表里没有第一页则展示
        showFirstPage = !pages.contains(1);
        // 是否展示最后一页，如果展示列表里没有最后一页则展示
        showEndPage = !pages.contains(totalPage);
    }
}
