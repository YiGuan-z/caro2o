package com.ruoyi.workflow.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 仓库信息对象 store
 *
 * @author Mi-Manchi
 * @date 2022-11-27
 */
@TableName("store")
public class Warehouse extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 序号 */
    @TableId(type = IdType.AUTO)
    private String id;

    /** 仓库名字 */
    @Excel(name = "仓库名字")
    @TableField("store_name")
    private String storeName;

    /** 地址 */
    @Excel(name = "地址")
    @TableField("store_address")
    private String storeAddress;

    /** 状态 */
    @TableField("state")
    private Integer state;

    public void setId(String id)
    {
        this.id = id;
    }

    public String getId()
    {
        return id;
    }
    public void setStoreName(String storeName)
    {
        this.storeName = storeName;
    }

    public String getStoreName()
    {
        return storeName;
    }
    public void setStoreAddress(String storeAddress)
    {
        this.storeAddress = storeAddress;
    }

    public String getStoreAddress()
    {
        return storeAddress;
    }
    public void setState(Integer state)
    {
        this.state = state;
    }

    public Integer getState()
    {
        return state;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("storeName", getStoreName())
            .append("storeAddress", getStoreAddress())
            .append("state", getState())
            .toString();
    }
}
