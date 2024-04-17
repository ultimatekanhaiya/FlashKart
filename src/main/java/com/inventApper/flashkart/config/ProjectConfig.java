package com.inventApper.flashkart.config;

import com.cloudinary.Cloudinary;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ProjectConfig {

    @Bean
    public ModelMapper mapper() {
        return new ModelMapper();
    }

    @Bean
    public Cloudinary getCloudinary() {
        Map config = new HashMap();
        config.put("cloud_name", "dnvaa58xz");
        config.put("api_key", "851572622767976");
        config.put("api_secret", "iCR5dWXXYFHXC0YJvuMgNGwCA7c");
        config.put("secure", true);
        return new Cloudinary(config);

    }

}
