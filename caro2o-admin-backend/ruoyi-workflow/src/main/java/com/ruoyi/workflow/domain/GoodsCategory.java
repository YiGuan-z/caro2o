package com.ruoyi.workflow.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.core.domain.TreeEntity;

/**
 * 物品分类信息对象 goods_category
 *
 * @author Mi-Manchi
 * @date 2022-11-27
 */
@TableName("goods_category")
public class GoodsCategory extends TreeEntity
{
    private static final long serialVersionUID = 1L;

    /** 序号 */
    @TableId(type = IdType.AUTO)
    @TableField("id")
    private Long id;

    /** 分类名称 */
    @Excel(name = "分类名称")
    @TableField("category_name")
    private String categoryName;

    /** 描述 */
    @Excel(name = "描述")
    @TableField("category_desc")
    private String categoryDesc;

    /** id层级结构 */
    @TableField("busi_path")
    private String busiPath;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setCategoryName(String categoryName)
    {
        this.categoryName = categoryName;
    }

    public String getCategoryName()
    {
        return categoryName;
    }
    public void setCategoryDesc(String categoryDesc)
    {
        this.categoryDesc = categoryDesc;
    }

    public String getCategoryDesc()
    {
        return categoryDesc;
    }
    public void setBusiPath(String busiPath)
    {
        this.busiPath = busiPath;
    }

    public String getBusiPath()
    {
        return busiPath;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("categoryName", getCategoryName())
                .append("categoryDesc", getCategoryDesc())
                .append("busiPath", getBusiPath())
                .append("parentId", getParentId())
                .toString();
    }
}