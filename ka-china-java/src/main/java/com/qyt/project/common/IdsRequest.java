package com.qyt.project.common;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class IdsRequest implements Serializable {

    /**
     * ids
     */
    private List<Long> ids;

    private static final long serialVersionUID = 1L;
}