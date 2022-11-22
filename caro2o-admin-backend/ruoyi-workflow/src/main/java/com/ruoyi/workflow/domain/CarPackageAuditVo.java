package com.ruoyi.workflow.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CarPackageAuditVo extends CarPackageAudit {

    private String taskId;
    private String taskName;
    private String auditors;
}
