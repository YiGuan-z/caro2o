package com.ruoyi.store.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.util.Date;


/**
 * 出入库单据对象 stock_bill
 *
 * @author ruoyi
 * @date 2022-11-27
 */
@Getter
@Setter
@ToString
@TableName("stock_bill_item")
public class StockBillItem extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 物品id */
    @Excel(name = "id")
    @TableId(type = IdType.ASSIGN_ID)
    @TableField("id")
    private String id;

    /** 数量 */
    @Excel(name = "物品id")
    @TableField("goods_id")
    private String goodsId;

    /**
     * 单价
     */
    @Excel(name = "数量")
    private Integer amounts;

    /**
     * 单据id
     */
    @Excel(name = "单价")
    private Long price;

    /** 状态 */
    @Excel(name = "单据id")
    @TableField("bill_id")
    private String billId;

    @Excel(name = "状态")
    @TableField("state")
    private String state;
}
