package com.project.notif.sms;

import com.project.notif.AbstractMessageService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author : thucnh
 * @mailto : thucnh96.dev@gmail.com
 * @created :15/10/2021 - 9:09 AM
 */
@Service("SmsService")
public class SmsService implements AbstractMessageService {
    @Override
    public Object sendMessage(Map<String, Object> params) {
        return null;
    }
}
