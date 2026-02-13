package com.msb.k01Prm.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 种植动作明细记录表
 * </p>
 *
 * @author GJJ
 * @since 2026-02-13 05:11:18
 */
@Getter
@Setter
@TableName("myjk_plant_action")
public class MyjkPlantAction implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 种植表id
     */
    private Integer plantId;

    /**
     * 动作 0：播种 1：浇水 2：施肥
     */
    private Integer action;

    /**
     * 数据状态，0--无效，1--有效
     */
    private Integer status;

    /**
     * 备注信息
     */
    private String remark;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
