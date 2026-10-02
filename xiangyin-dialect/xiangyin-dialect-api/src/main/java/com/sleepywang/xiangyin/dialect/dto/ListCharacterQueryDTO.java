package com.sleepywang.xiangyin.dialect.dto;

import com.sleepywang.xiangyin.dialect.entity.GuangyunInitialEntity;
import com.sleepywang.xiangyin.dialect.entity.GuangyunRhymeEntity;
import lombok.Data;

@Data
public class ListCharacterQueryDTO {
    private Integer id;
    private String hanzi;
    private GuangyunInitialEntity guangyunInitial;
    private GuangyunRhymeEntity guangyunRhyme;
    private String guangyunTone;
    private Integer characterListId;
}
