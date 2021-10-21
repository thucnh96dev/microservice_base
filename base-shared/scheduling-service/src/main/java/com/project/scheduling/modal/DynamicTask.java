package com.project.scheduling.modal;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : thucnh
 * @mailto : thucnh96.dev@gmail.com
 * @created :21/10/2021 - 11:56 AM
 */
@Data
@NoArgsConstructor
public class DynamicTask {
    private Long id;
    private String cronExpression;
}
