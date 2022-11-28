package com.ruoyi.store.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wby
 * @version 1.0
 * @date 2022/11/28 19:13
 */
@Setter
@Getter
@NoArgsConstructor
public class TreeData {

    private Long id;
    private String label;
    private List<TreeData> children = new ArrayList<>();

    public TreeData(Long id, String label) {
        this.id = id;
        this.label = label;
    }
}

