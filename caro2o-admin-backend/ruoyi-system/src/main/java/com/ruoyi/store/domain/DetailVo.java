package com.ruoyi.store.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

/**
 *  出入库明细对象
 */
@Getter
@Setter
public class DetailVo {
    private Long id;
    private Integer type;
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date operateDate;
    private String goodsName;
    private String brand;
    private String storeId;
    private Integer amounts;
    private String storeName;
    private BigDecimal price;
}
