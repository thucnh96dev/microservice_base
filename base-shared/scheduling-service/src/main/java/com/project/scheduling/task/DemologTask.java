package com.project.scheduling.task;

import org.springframework.stereotype.Component;

/**
 * @author : thucnh
 * @mailto : thucnh96.dev@gmail.com
 * @created :21/10/2021 - 1:25 PM
 */
@Component("demologTask")
public class DemologTask {

    public void taskWithParams(String param1, Integer param2) {
        System.out.println("taskWithParams：" + param1 + param2);
    }

    public void taskNoParams() {
        System.out.println("taskNoParams");
    }
}
