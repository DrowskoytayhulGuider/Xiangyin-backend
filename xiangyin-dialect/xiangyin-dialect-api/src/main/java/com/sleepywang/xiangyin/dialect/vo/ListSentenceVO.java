package com.sleepywang.xiangyin.dialect.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.sleepywang.xiangyin.dialect.entity.SentenceListEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ListSentenceVO {
    /**
     * 短句编号
     */
    private Integer id;

    /**
     * 所属句表
     */
    private SentenceListEntity sentenceList;

    /**
     * 普通话句意
     */
    private String standardForm;

    /**
     * 备注
     */
    private String remark;

}
