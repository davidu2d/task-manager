package com.hiveplace.task_manager.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Builder
@Data
@AllArgsConstructor
public class Token implements Serializable {
    private String AccessToken;
    private String username;
}
