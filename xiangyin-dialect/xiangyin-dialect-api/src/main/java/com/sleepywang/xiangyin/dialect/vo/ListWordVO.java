package com.sleepywang.xiangyin.dialect.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.sleepywang.xiangyin.dialect.entity.WordListEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ListWordVO {
    /**
     * 词汇编号
     */

    private Integer id;

    /**
     * 所属词表
     */

    private WordListEntity wordList;

    /**
     * 普通话标准词形
     */

    private String standardForm;

    /**
     * 词性
     */

    private String speechPart;

    /**
     * 含义
     */

    private String meaning;

    /**
     * 备注
     */

    private String remark;

}
