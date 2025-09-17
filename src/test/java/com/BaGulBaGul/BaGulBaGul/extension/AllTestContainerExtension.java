package com.BaGulBaGul.BaGulBaGul.extension;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit.jupiter.SpringExtension;

public class AllTestContainerExtension implements BeforeAllCallback {

    static private final MysqlTestContainerExtension mysql = new MysqlTestContainerExtension();
    static private final RedisTestContainerExtension redis = new RedisTestContainerExtension();

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        if(!checkUseFlag(context)) {
            return;
        }
        mysql.beforeAll(context);
        redis.beforeAll(context);
    }

    private boolean checkUseFlag(ExtensionContext context) {
        ApplicationContext applicationContext = SpringExtension.getApplicationContext(context);
        Environment environment = applicationContext.getEnvironment();

        // Environment 객체에서 프로퍼티 값을 읽어옵니다.
        // 값이 없을 경우 기본값으로 true를 사용합니다.
        return environment.getProperty("test.use_testcontainers", Boolean.class, true);
    }
}
