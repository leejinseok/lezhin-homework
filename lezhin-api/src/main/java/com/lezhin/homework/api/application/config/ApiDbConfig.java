package com.lezhin.homework.api.application.config;

import com.lezhin.homework.core.db.config.CoreDbConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({CoreDbConfig.class})
public class ApiDbConfig {
}
