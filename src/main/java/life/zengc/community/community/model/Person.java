package life.zengc.community.community.model;

import lombok.Data;

@Data
public class Person {

    private Integer id;

    private String name;

    private String accountId;

    private String token;

    private Long gmtCreate;

    private Long gmtModified;
}
