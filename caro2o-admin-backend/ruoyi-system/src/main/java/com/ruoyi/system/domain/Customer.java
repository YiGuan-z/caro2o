package com.ruoyi.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 客户信息对象 customer
 *
 * @author csj
 * @date 2022-11-27
 */
@Setter
@Getter
@TableName("customer")
public class Customer extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    @TableId(type = IdType.AUTO)
    private String id;

    /** 企业名称 */
    @Excel(name = "企业名称")
    @TableField("customer_name")
    private String customerName;

    /** 法定代表人 */
    @Excel(name = "法定代表人")
    @TableField("legal_leader")
    private String legalLeader;

    /** 成立时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "成立时间", width = 30, dateFormat = "yyyy-MM-dd")
    @TableField("register_date")
    private Date registerDate;

    /** 经营状态 */
    @Excel(name = "经营状态")
    @TableField("open_state")
    private Integer openState;

    /** 所属省份 */
    @Excel(name = "所属省份")
    @TableField("province")
    private String province;

    /** 注册资本,(万元) */
    @Excel(name = "注册资本,(万元)")
    @TableField("reg_capital")
    private BigDecimal regCapital;

    /** 所属行业 */
    @Excel(name = "所属行业")
    @TableField("industry")
    private String industry;

    /** 经营范围 */
    @Excel(name = "经营范围")
    @TableField("scope")
    private String scope;

    /** 注册地址 */
    @Excel(name = "注册地址")
    @TableField("reg_addr")
    private String regAddr;

    /** 录入时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "录入时间", width = 30, dateFormat = "yyyy-MM-dd")
    @TableField("input_time")
    private Date inputTime;

    /** 录入人 */
    @Excel(name = "录入人Id")
    @TableField("input_userId")
    private Long inputUser;

    @Excel(name = "录入人")
    @TableField("input_user")
    private String inputUserName;

    @Excel(name = "营销人")
    @TableField("input_market")
    private String userMarket;

}
