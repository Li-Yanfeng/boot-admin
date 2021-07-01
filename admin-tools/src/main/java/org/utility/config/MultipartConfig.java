package org.utility.config;

import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;

import javax.servlet.MultipartConfigElement;
import java.io.File;

/**
 * 多文件 配置
 *
 * @author Li Yanfeng
 * @link https://blog.csdn.net/llibin1024530411/article/details/79474953
 */
public class MultipartConfig {

    /**
     * 文件上传临时路径
     */
    @Bean
    MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        String location = System.getProperty("user.home") + "/.admin/file/tmp";
        File tmpFile = new File(location);
        if (!tmpFile.exists()) {
            if (!tmpFile.mkdirs()) {
                System.out.println("create was not successful.");
            }
        }
        factory.setLocation(location);
        return factory.createMultipartConfig();
    }
}
