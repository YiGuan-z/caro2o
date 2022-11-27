package com.ruoyi.contract.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 合同项信息对象 contract
 *
 * @author xiaochen
 * @date 2022-11-27
 */
@TableName("contract")
public class ContractItemInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /** 客户id */
    @Excel(name = "客户id")
    @TableField("customer_id")
    private String customerId;

    /** 合同名称 */
    @Excel(name = "合同名称")
    @TableField("contract_name")
    private String contractName;

    /** 合同编号 */
    @Excel(name = "合同编号")
    @TableField("contract_code")
    private String contractCode;

    /** 合同金额 */
    @Excel(name = "合同金额")
    @TableField("amounts")
    private BigDecimal amounts;

    /** 合同生效开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "合同生效开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    @TableField("start_date")
    private Date startDate;

    /** 合同失效时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "合同失效时间", width = 30, dateFormat = "yyyy-MM-dd")
    @TableField("end_date")
    private Date endDate;

    /** 附件 */
    @Excel(name = "附件")
    @TableField("appendix")
    private String appendix;

    /** 是否盖章确认 0 否 1 是 */
    @Excel(name = "是否盖章确认 0 否 1 是")
    @TableField("affix_seal_state")
    private Integer affixSealState;

    /** 审核状态 0 未审核 1 审核通过 -1 审核不通过 */
    @Excel(name = "审核状态 0 未审核 1 审核通过 -1 审核不通过")
    @TableField("audit_state")
    private Integer auditState;

    /** 是否作废 -1 作废 0 在用 */
    @Excel(name = "是否作废 -1 作废 0 在用")
    @TableField("nullify_state")
    private Integer nullifyState;

    /** 录入人 */
    @Excel(name = "录入人")
    @TableField("input_user")
    private Long inputUser;

    /** 录入时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "录入时间", width = 30, dateFormat = "yyyy-MM-dd")
    @TableField("input_time")
    private Date inputTime;

    public void setId(String id)
    {
        this.id = id;
    }

    public String getId()
    {
        return id;
    }
    public void setCustomerId(String customerId)
    {
        this.customerId = customerId;
    }

    public String getCustomerId()
    {
        return customerId;
    }
    public void setContractName(String contractName)
    {
        this.contractName = contractName;
    }

    public String getContractName()
    {
        return contractName;
    }
    public void setContractCode(String contractCode)
    {
        this.contractCode = contractCode;
    }

    public String getContractCode()
    {
        return contractCode;
    }
    public void setAmounts(BigDecimal amounts)
    {
        this.amounts = amounts;
    }

    public BigDecimal getAmounts()
    {
        return amounts;
    }
    public void setStartDate(Date startDate)
    {
        this.startDate = startDate;
    }

    public Date getStartDate()
    {
        return startDate;
    }
    public void setEndDate(Date endDate)
    {
        this.endDate = endDate;
    }

    public Date getEndDate()
    {
        return endDate;
    }
    public void setAppendix(String appendix)
    {
        this.appendix = appendix;
    }

    public String getAppendix()
    {
        return appendix;
    }
    public void setAffixSealState(Integer affixSealState)
    {
        this.affixSealState = affixSealState;
    }

    public Integer getAffixSealState()
    {
        return affixSealState;
    }
    public void setAuditState(Integer auditState)
    {
        this.auditState = auditState;
    }

    public Integer getAuditState()
    {
        return auditState;
    }
    public void setNullifyState(Integer nullifyState)
    {
        this.nullifyState = nullifyState;
    }

    public Integer getNullifyState()
    {
        return nullifyState;
    }
    public void setInputUser(Long inputUser)
    {
        this.inputUser = inputUser;
    }

    public Long getInputUser()
    {
        return inputUser;
    }
    public void setInputTime(Date inputTime)
    {
        this.inputTime = inputTime;
    }

    public Date getInputTime()
    {
        return inputTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("customerId", getCustomerId())
            .append("contractName", getContractName())
            .append("contractCode", getContractCode())
            .append("amounts", getAmounts())
            .append("startDate", getStartDate())
            .append("endDate", getEndDate())
            .append("appendix", getAppendix())
            .append("affixSealState", getAffixSealState())
            .append("auditState", getAuditState())
            .append("nullifyState", getNullifyState())
            .append("inputUser", getInputUser())
            .append("inputTime", getInputTime())
            .toString();
    }
}
