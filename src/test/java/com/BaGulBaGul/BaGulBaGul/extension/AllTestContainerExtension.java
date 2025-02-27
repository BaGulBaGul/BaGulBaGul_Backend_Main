package com.BaGulBaGul.BaGulBaGul.extension;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class AllTestContainerExtension implements BeforeAllCallback {

    static private final MysqlTestContainerExtension mysql = new MysqlTestContainerExtension();
    static private final RedisTestContainerExtension redis = new RedisTestContainerExtension();

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        mysql.beforeAll(context);
        redis.beforeAll(context);
    }
}
