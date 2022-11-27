package com.ruoyi.customer.domain;

import java.util.Date;
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
 * 拜访信息对象 customer_visit
 *
 * @author xiaochen
 * @date 2022-11-27
 */
@Getter
@Setter
@TableName("customer_visit")
public class CustomerVisit extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 唯一id */
    @Excel(name = "唯一id")
    @TableId(type = IdType.AUTO)
    private String id;

    /** 客户id */
    @Excel(name = "客户id")
    @TableField("customer_id")
    private String customerId;

    /** 联系人id */
    @Excel(name = "联系人id")
    @TableField("linkman_id")
    private String linkmanId;

    /** 拜访方式, 1 上门走访, 2 电话拜访 */
    @Excel(name = "拜访方式, 1 上门走访, 2 电话拜访")
    @TableField("visit_type")
    private Integer visitType;

    /** 拜访原因 */
    @Excel(name = "拜访原因")
    @TableField("visit_reason")
    private String visitReason;

    /** 交流内容 */
    @Excel(name = "交流内容")
    @TableField("content")
    private String content;

    /** 拜访时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "拜访时间", width = 30, dateFormat = "yyyy-MM-dd")
    @TableField("visit_date")
    private Date visitDate;

    /** 录入人 */
    @Excel(name = "录入人")
    @TableField("input_user")
    private Long inputUser;

    /** 录入时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "录入时间", width = 30, dateFormat = "yyyy-MM-dd")
    @TableField("input_time")
    private Date inputTime;


    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("customerId", getCustomerId())
            .append("linkmanId", getLinkmanId())
            .append("visitType", getVisitType())
            .append("visitReason", getVisitReason())
            .append("content", getContent())
            .append("visitDate", getVisitDate())
            .append("inputUser", getInputUser())
            .append("inputTime", getInputTime())
            .toString();
    }
}
