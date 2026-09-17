package com.sleepywang.xiangyin.dialect.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 广韵声母
 *
 * @author sleepywang
 * @date 2026-09-18 01:22:09
 */
@Data
@TableName("guangyun_initial")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "广韵声母")
public class GuangyunInitialEntity extends Model<GuangyunInitialEntity> {


    /**
     * 声母编号
     */
    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description="声母编号")
    private Integer id;

    /**
     * 声母名称
     */
    @Schema(description="声母名称")
    private String initial;

    /**
     * 清浊（清-0，浊-1）
     */
    @Schema(description="清浊（清-0，浊-1）")
    private Integer voicing;

    /**
     * 发音部位（重唇，轻唇，舌头，舌上，牙，齿头，正齿，喉，半舌，半齿）
     */
    @Schema(description="发音部位（重唇，轻唇，舌头，舌上，牙，齿头，正齿，喉，半舌，半齿）")
    private String articulationPlace;

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
