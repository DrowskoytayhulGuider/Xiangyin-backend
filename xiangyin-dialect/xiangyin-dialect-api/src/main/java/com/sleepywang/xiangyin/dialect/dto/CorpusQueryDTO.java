package com.sleepywang.xiangyin.dialect.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

// 语料可按照：语料编号、所属方言点、语料名称、所属派别、发音人年龄（区间）、发音人性别、
// 发音人文化程度（多选）、发音人职业（like）、只显示受影响不明显的（开关）、提交人、
// 创建时间（区间）、状态，分页及列表筛选，并按正宗数排序，可以选择按点赞排序。（后期加入置顶功能，置顶内部单独排序，始终在最上面）
// 筛选同样先剔除已删除的
@Data
public class CorpusQueryDTO {
    /**
     * 语料编号
     */
    private Integer id;
    /**
     * 模式：模式一：先从发布表中筛选再从工作表中筛选（适用于浏览）
     * 模式二：先从工作表中筛选再从发布表中筛选（适用于审核）
     * 模式三：只从工作表中筛选（适用于编辑）
     */
    private Integer mode;
    /**
     * 所属方言点
     */
    private Integer dialectPointId;
    /**
     * 语料名称
     */
    private String name;
    /**
     * 所属派别（新派-NEW、中派-MEDIUM、老派-OLD）
     */
    private String faction;
    /**
     * 发音人年龄
     */
    private Integer ageStart;
    private Integer ageEnd;
    /**
     * 发音人性别
     */
    private String sex;
    /**
     * 发音人文化程度
     */
    private List<String> educationLevels;
    /**
     * 发音人职业
     */
    private String vocation;
    /**
     * 提交人
     */
    private Long submitterId;
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
    private LocalDateTime createTimeStart;
    private LocalDateTime createTimeEnd;
    /**
     * 是否按点赞数排序，默认false，并按照正宗数排序
     */
    private Boolean isSortedByLike=false;
}
