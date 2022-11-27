package com.ruoyi.store.domain;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 物品库存对象 goods_store
 *
 * @author caseycheng
 * @date 2022-11-27
 */
@TableName("goods_store")
@Setter
@Getter
public class GoodsStore extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    /** 物品id */
    @Excel(name = "物品id")
    @TableField("goods_id")
    private String goodsId;

    /** 仓库id */
    @Excel(name = "仓库id")
    @TableField("store_id")
    private String storeId;

    /** 库存数量 */
    @Excel(name = "库存数量")
    @TableField("amounts")
    private Long amounts;
    
    // 物品字段
    @TableField(exist = false)
    private String productImg;
    // 名称
    @TableField(exist = false)
    private String name;
    //品牌
    @TableField(exist = false)
    private String brand;
    //分类
    @TableField(exist = false)
    private String classify;
}
