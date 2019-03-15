package com.gihub.afobo;

import lombok.Data;

import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

@Data
class Attempt {
    private String clusterName;
    private int attemptNumber;
    private Date startDate;
    private Date endDate;
    private boolean failed;
    private boolean completed;
    private String vmCount;
    private String buildName;
    private Map<String, String> options = new TreeMap<>();
}
