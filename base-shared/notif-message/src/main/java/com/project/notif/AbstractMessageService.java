package com.project.notif;

import java.util.Map;

/**
 * @author : thucnh
 * @mailto : thucnh96.dev@gmail.com
 * @created :15/10/2021 - 9:05 AM
 */
public interface AbstractMessageService {
    Object sendMessage(Map<String, Object> params);
}
