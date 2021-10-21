package com.project.notif.mail;

import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.springframework.ui.ModelMap;

/**
 * @author : thucnh
 * @mailto : thucnh96.dev@gmail.com
 * @created :15/10/2021 - 9:37 AM
 */
public class MailTemplateCreator {
    public static String createTempate(ModelMap model, String tempateName) throws Exception {
        try {
            VelocityEngine velocityEngine = new VelocityEngine();
            velocityEngine.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
            velocityEngine.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
            velocityEngine.init();
            //String mailBody = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "templates/" + tempateName, "UTF-8", model);
            String mailBody = null;
            return mailBody;
        } catch (Exception e) {
            return null;
        }
    }
}
