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
 * 物品信息对象 goods
 *
 * @author caseycehng
 * @date 2022-11-27
 */
@TableName("goods")
@Setter
@Getter
public class Goods extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /** 物品名称 */
    @Excel(name = "物品名称")
    @TableField("goods_name")
    private String goodsName;

    /** 封面 */
    @Excel(name = "封面")
    @TableField("goods_cover")
    private String goodsCover;

    /** 分类 */
    @Excel(name = "分类")
    @TableField("category_id")
    private String categoryId;
    /** 分类对象*/
    @TableField(exist = false)
    private GoodsCategory category;

    /** 品牌 */
    @Excel(name = "品牌")
    @TableField("brand")
    private String brand;

    /** 规格 */
    @Excel(name = "规格")
    @TableField("spec")
    private String spec;

    /** 描述 */
    @Excel(name = "描述")
    @TableField("goods_desc")
    private String goodsDesc;
}
