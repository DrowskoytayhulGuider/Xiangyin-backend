package com.sleepywang.xiangyin.dialect.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CorpusVO {
    /**
     * 语料编号
     */
    private Integer id;
    /**
     * 所属方言点
     */
    private DialectPointVO dialectPoint;
    /**
     * 语料名称
     */
    private String name;
    /**
     * 所属派别（新派-NEW、中派-MEDIUM、老派-OLD）
     */
    private String faction;
    /**
     * 所受影响普化(POPULARIZATION)、其他方言影响(AFFECTED_BY_OTHER_DIALECT)、NONE）
     */
    private String affection;
    /**
     * 发音人年龄
     */
    private Integer age;
    /**
     * 发音人性别
     */
    private String sex;
    /**
     * 发音人文化程度
     */
    private String educationLevel;
    /**
     * 发音人是否长期居于外地
     */
    private Boolean liveOutside;
    /**
     * 发音人常住地
     */
    private String permanentPlace;
    /**
     * 发音人职业
     */
    private String vocation;
    /**
     * 发音人籍贯
     */
    private String originalPlace;
    /**
     * 提交人
     */
    private Long submitterId;
    /**
     * 语料说明
     */
    private String introduction;
    /**
     * 音系说明
     */
    private String phonologyExplanation;
    /**
     * 备注
     */
    private String remark;
    /**
     * 点赞数
     */
    private Integer likeNumber;
    /**
     * 正宗数
     */
    private Double authenticNumber;
    /**
     * 状态（草稿-DRAFT、待审核-PENDING、已发布-RELEASED、已打回-REJECTED、已删除-DELETED）。
     * 状态机说明：用户创建了语料，则设置为草稿，此时只有用户处可见，可编辑。
     * 用户点击发布，变为待审核，此时用户和管理员可见，用户不可编辑，管理员可编辑。
     * 管理员选择审核通过，变为已发布，此时所有用户和管理员可见，都不可编辑。
     * 管理员选择打回，变为已打回，此时用户和管理员都可见，用户可编辑，管理员不可编辑。
     * 用户再次发布，又转换为待审核。管理员对已发布的语料选择打回，又变为已打回。
     * 管理员对已打回的语料选择审核通过，则变为已发布。用户自己删除语料，变为已删除，所有人不可见不可编辑。
     * 已发布的语料用户可以修改为草稿用以修改，然后重新提交变为待审核。
     */
    private String status;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
