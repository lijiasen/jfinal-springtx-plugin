package com.gaols.plugins;

import com.jfinal.log.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;

/**
 * Copy from jfinal.
 */
public class SqlReporter implements InvocationHandler {

    private Connection conn;
    private static final Log log = Log.getLog(com.jfinal.plugin.activerecord.SqlReporter.class);

    SqlReporter(Connection conn) {
        this.conn = conn;
    }

    @SuppressWarnings("rawtypes")
    Connection getConnection() {
        Class clazz = conn.getClass();
        return (Connection) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{Connection.class}, this);
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        try {
            if (method.getName().equals("prepareStatement")) {
                String info = "Sql: " + args[0];
                log.info(info);
            }
            return method.invoke(conn, args);
        } catch (InvocationTargetException e) {
            throw e.getTargetException();
        }
    }
}
