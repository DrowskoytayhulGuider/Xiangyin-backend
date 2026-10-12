package com.sleepywang.xiangyin.dialect.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

/**
 * 关注
 *
 * @author sleepywang
 * @date 2026-09-18 01:08:00
 */
@Data
@TableName("following")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "关注")
public class FollowingEntity extends Model<FollowingEntity> {



    @TableId(type = IdType.AUTO)
    @Schema(description="ID")
    private Integer id;
    /**
     * 关注者ID
     */
    @Schema(description="关注者ID")
    private Long followerId;

    /**
     * 被关注者ID
     */
    @Schema(description="被关注者ID")
    private Long followeeId;

    /**
     * 关注时间
     */
    @Schema(description="关注时间")
    private LocalDateTime flowTime;

    /**
     * 是否解除
     */
    @Schema(description="是否解除")
    private Boolean isRelieved;
}
