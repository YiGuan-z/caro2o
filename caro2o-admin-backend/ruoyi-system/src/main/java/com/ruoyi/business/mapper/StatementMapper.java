package com.ruoyi.business.mapper;

import com.ruoyi.business.domain.Statement;
import com.ruoyi.business.query.StatementQuery;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

public interface StatementMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Statement record);

    Statement selectByPrimaryKey(Long id);

    List<Statement> selectAll();

    int updateByPrimaryKey(Statement record);

    List<Statement> selectForList(StatementQuery qo);

    void updateAmount(@Param("statementId") Long statementId, @Param("totalAmount") BigDecimal totalAmount, @Param("totalQuantity") BigDecimal totalQuantity, @Param("disCountPrice") BigDecimal disCountPrice);

    void pay(@Param("statementId") Long statementId, @Param("userId") Long userId, @Param("status") Integer status);

    Statement getByAppointmentId(Long appointmentId);

}