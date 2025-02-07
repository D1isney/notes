package com.wms.vo;

import com.wms.pojo.Task;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TaskVo extends Task {
    private String createUsername;
    private String updateUsername;
}
