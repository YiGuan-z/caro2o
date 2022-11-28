package com.ruoyi.store.domain;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.TreeEntity;
import lombok.Getter;
import lombok.Setter;
/**
 * 物品分类信息对象 goods_category
 *
 * @author Mi-Manchi
 * @date 2022-11-27
 */
@Getter
@Setter
@TableName("goods_category")
public class GoodsCategory extends TreeEntity<GoodsCategory>
{
    private static final long serialVersionUID = 1L;

    /** 序号 */
    @TableId(type = IdType.AUTO)
    private String id;

    /** 分类名称 */
    @Excel(name = "分类名称")
    @TableField("category_name")
    private String categoryName;
    
    @TableField(exist = false)
    private String label;

    /** 描述 */
    @Excel(name = "描述")
    @TableField("category_desc")
    private String categoryDesc;

    /** id层级结构 */
    @Excel(name = "id层级结构")
    @TableField("busi_path")
    private String busiPath;
}