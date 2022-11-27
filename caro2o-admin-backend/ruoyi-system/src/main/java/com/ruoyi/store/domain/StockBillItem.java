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
@TableName("stock_bill")
public class StockBillItem extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 序号 */
    @Excel(name = "序号")
    @TableId(type = IdType.ASSIGN_ID)
    @TableField("id")
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

    public void setId(String id)
    {
        this.id = id;
    }

    public String getId()
    {
        return id;
    }
    public void setType(Integer type)
    {
        this.type = type;
    }

    public Integer getType()
    {
        return type;
    }
    public void setStoreId(String storeId)
    {
        this.storeId = storeId;
    }

    public String getStoreId()
    {
        return storeId;
    }
    public void setBusiDate(Date busiDate)
    {
        this.busiDate = busiDate;
    }

    public Date getBusiDate()
    {
        return busiDate;
    }
    public void setStatus(Integer status)
    {
        this.status = status;
    }

    public Integer getStatus()
    {
        return status;
    }
    public void setOperateDate(Date operateDate)
    {
        this.operateDate = operateDate;
    }

    public Date getOperateDate()
    {
        return operateDate;
    }
    public void setOperatorId(Long operatorId)
    {
        this.operatorId = operatorId;
    }

    public Long getOperatorId()
    {
        return operatorId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("type", getType())
            .append("storeId", getStoreId())
            .append("busiDate", getBusiDate())
            .append("status", getStatus())
            .append("operateDate", getOperateDate())
            .append("operatorId", getOperatorId())
            .append("remark", getRemark())
            .toString();
    }
}
