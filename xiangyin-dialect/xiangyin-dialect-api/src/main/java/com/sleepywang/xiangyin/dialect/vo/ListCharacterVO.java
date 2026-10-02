package com.sleepywang.xiangyin.dialect.vo;

import com.sleepywang.xiangyin.dialect.entity.CharacterListEntity;
import com.sleepywang.xiangyin.dialect.entity.GuangyunInitialEntity;
import com.sleepywang.xiangyin.dialect.entity.GuangyunRhymeEntity;
import lombok.Data;

@Data
public class ListCharacterVO {
    private Integer id;
    /**
     * 字形
     */
    private String hanzi;
    /**
     * 中古声母
     */
    private GuangyunInitialEntity guangyunInitial;

    /**
     * 中古声调
     */
    private String guangyunTone;

    /**
     * 字义
     */
    private String meaning;

    /**
     * 中古韵母
     */
    private GuangyunRhymeEntity guangyunRhyme;

    /**
     * 所属字表
     */
    private CharacterListEntity characterList;

    /**
     * 备注
     */
    private String remark;
}
