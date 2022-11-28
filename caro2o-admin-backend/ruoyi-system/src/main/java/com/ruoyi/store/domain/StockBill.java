package com.ruoyi.store.domain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
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
 * 出入库单据对象 stock_bill
 *
 * @author ruoyi
 * @date 2022-11-27
 */
@Getter
@Setter
@TableName("stock_bill")
public class StockBill extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 序号 */
    @Excel(name = "序号")
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /** 类型 */
    @Excel(name = "类型")
    @TableField("type")
    private Integer type;

    /**
     * 总金额
     */
    @Excel(name = "总金额")
    @TableField(exist = false)
    private BigDecimal totalMoney;

    /**
     * 总数量
     */
    @Excel(name = "总数量")
    @TableField(exist = false)
    private Long totalNum;
    /** 仓库 */
    @Excel(name = "仓库")
    @TableField("store_id")
    private String storeId;

    /** 出入库时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "出入库时间", width = 30, dateFormat = "yyyy-MM-dd")
    @TableField("busi_date")
    private Date busiDate;

    /** 状态 */
    @Excel(name = "状态")
    @TableField("status")
    private Integer status;

    /** 录入时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "录入时间", width = 30, dateFormat = "yyyy-MM-dd")
    @TableField("operate_date")
    private Date operateDate;

    /** 录入人 */
    @Excel(name = "录入人")
    @TableField("operator_id")
    private Long operatorId;

    @Excel(name = "备注")
    @TableField("remark")
    private String remark;

    @TableField(exist = false)
    private List<StockBillItem> itemFrom;
}
