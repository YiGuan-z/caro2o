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

import java.util.Date;

/**
 * 客户联系人对象 customer_linkmane
 *
 * @author csj
 * @date 2022-11-28
 */
@Setter
@Getter
@TableName("customer_linkmane")
public class CustomerLinkmane extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    @TableId(type = IdType.AUTO)
    private String id;

    /** 客户 */
    @Excel(name = "客户")
    @TableField("customer_id")
    private String customerName;

    @TableField(exist = false)
    private String legalLeader;

    /** 联系人名字 */
    @Excel(name = "联系人名字")
    @TableField("linkman")
    private String linkman;

    /** 性别 */
    @Excel(name = "性别")
    @TableField("gender")
    private Integer gender;

    /** 年龄 */
    @Excel(name = "年龄")
    @TableField("age")
    private Long age;

    /** 联系人电话 */
    @Excel(name = "联系人电话")
    @TableField("phone")
    private String phone;

    /** 职位 */
    @Excel(name = "职位")
    @TableField("position")
    private String position;

    /** 任职状态 */
    @Excel(name = "任职状态")
    @TableField("position_state")
    private Integer positionState;

    /** 部门 */
    @Excel(name = "部门")
    @TableField("department")
    private String department;

    /** 录入人 */
    @Excel(name = "录入人Id")
    @TableField("input_user")
    private Long inputUser;

    @Excel(name = "录入人")
    @TableField("input_username")
    private String inputUserName;

    /** 录入时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "录入时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @TableField("input_time")
    private Date inputTime;


}
