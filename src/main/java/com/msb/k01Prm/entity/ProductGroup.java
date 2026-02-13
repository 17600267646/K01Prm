package com.msb.k01Prm.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("product_group")
public class ProductGroup {

    @TableId
    private Long id;

    // TODO: 根据实际表结构增加其他字段
}

