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
    static private final String USE_FLAG_NAME = "test.use_testcontainers";

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        if(!checkUseFlag()) {
            return;
        }
        mysql.beforeAll(context);
        redis.beforeAll(context);
    }

    private boolean checkUseFlag() {
        String property = System.getProperty(USE_FLAG_NAME);
        if(property != null && property.equals("true")) {
            return true;
        } else if(property != null) {
            return false;
        }

        String getenv = System.getenv(USE_FLAG_NAME);
        if(getenv != null && getenv.equals("true")) {
            return true;
        } else if(getenv != null) {
            return false;
        }

        return true;
    }
}
