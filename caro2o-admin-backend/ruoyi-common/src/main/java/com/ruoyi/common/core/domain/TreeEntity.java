package com.ruoyi.common.core.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Tree基类
 * 
 * @author ruoyi
 */
@Setter
@Getter
public class TreeEntity<T> extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 父菜单名称 */
    @TableField(exist = false)
    private String parentName;

    /** 父菜单ID */
    @TableField(exist = false)
    private Long parentId;

    /** 显示顺序 */
    @TableField(exist = false)
    private Integer orderNum;

    /** 祖级列表 */
    @TableField(exist = false)
    private String ancestors;

    /** 子部门 */
    @TableField(exist = false)
    private List<T> children = new ArrayList<>();
    
}
