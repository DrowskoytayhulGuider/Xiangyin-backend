package com.sleepywang.xiangyin.dialect.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 广韵韵母
 *
 * @author sleepywang
 * @date 2026-09-18 01:21:20
 */
@Data
@TableName("guangyun_rhyme")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "广韵韵母")
public class GuangyunRhymeEntity extends Model<GuangyunRhymeEntity> {


    /**
     * 韵母编号
     */
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description="韵母编号")
    private Integer id;

    /**
     * 韵母名称
     */
    @Schema(description="韵母名称")
    private String rhyme;

    /**
     * 韵摄
     */
    @Schema(description="韵摄")
    private String she;

    /**
     * 等
     */
    @Schema(description="等")
    private String deng;

    /**
     * 开合
     */
    @Schema(description="开合")
    private String hu;

    /**
     * 韵尾性质（塞-PLOSIVES、鼻-NASAL、无-NONE）
     */
    @Schema(description="韵尾性质（塞-PLOSIVES、鼻-NASAL、无-NONE）")
    private String rhymeEnd;

    /**
     * 拟音方案一
     */
    @Schema(description="拟音方案一")
    private String onomatopoeia1;

    /**
     * 拟音方案二
     */
    @Schema(description="拟音方案二")
    private String onomatopoeia2;

    /**
     * 拟音方案三
     */
    @Schema(description="拟音方案三")
    private String onomatopoeia3;
}
