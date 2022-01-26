package com.prabhutech.prabhupackages.wallet.core.api.utils.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Reflect {
    public static Object invokeStaticVerify(String clazzName, String annotationName, String verifyName, Object... args) {
        Class o = null;
        try {
            o = Class.forName(clazzName);
            for (Method method : o.getMethods())
                for (Annotation annotation : method.getAnnotations())
                    if (annotation.toString().contains(annotationName) && ((com.prabhutech.coop.wallet.core.api.utils.reflection.RepoGetter) annotation).name().equals(verifyName)) {
                        return method.invoke(null, args);
                    }
        } catch (ClassNotFoundException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object invokeStaticNoVerify(String clazzName, String annotationName, Object... args) {
        Class o = null;
        try {
            o = Class.forName(clazzName);
            for (Method method : o.getMethods())
                for (Annotation annotation : method.getAnnotations())
                    if (annotation.toString().contains(annotationName)) {
                        return method.invoke(null, args);
                    }
        } catch (ClassNotFoundException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object dynamicAPICall(String clazzName, String annotationName, Object... args) {
        Class o = null;
        try {
            o = Class.forName(clazzName);
            for (Method method : o.getMethods())
                for (Annotation annotation : method.getAnnotations())
                    if (annotation.toString().contains(annotationName)) {
                        return method.invoke(null, args);
                    }
        } catch (ClassNotFoundException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object invokeStatic(String clazzName, String annotationName, String value, Object... args) {
        Class o = null;
        try {
            o = Class.forName(clazzName);
            for (Method method : o.getMethods())
                for (Annotation annotation : method.getAnnotations())
                    if (annotation.toString().contains(annotationName) && ((com.prabhutech.coop.wallet.core.api.utils.reflection.ApiNameTarget) annotation).value().equals(value)) {
                        return method.invoke(null, args);
                    }
        } catch (ClassNotFoundException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * The called method must me a static else you would receive "Null Receiver" error.
     *
     * @param clazzName - String
     * @param reqName   - String
     * @param args      - Object args
     * @return - args or null
     */
    public static Object repoGet(String clazzName, String reqName, Object... args) {
        Class o = null;
        try {
            o = Class.forName(clazzName);
            for (Method method : o.getMethods())
                for (Annotation annotation : method.getAnnotations())
                    if (annotation.toString().contains("RepoGet") && ((com.prabhutech.coop.wallet.core.api.utils.reflection.RepoGet) annotation).value().equals(reqName)) {
                        return method.invoke(null, args);
                    }
        } catch (ClassNotFoundException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object dynamicRepoGet(String clazzName, String reqName, Object... args) {
        Class o = null;
        try {
            o = Class.forName(clazzName);
            for (Method method : o.getMethods())
                for (Annotation annotation : method.getAnnotations())
                    if (annotation.toString().contains("DynamicRepoGet") && ((com.prabhutech.coop.wallet.core.api.utils.reflection.DynamicRepoGet) annotation).value().equals(reqName)) {
                        return method.invoke(null, args);
                    }
        } catch (ClassNotFoundException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object getAPIContractVar(String clazz, String varName) {
        Class o = null;
        try {
            o = Class.forName(clazz);
            for (Annotation annotation : o.getAnnotations()) {
                if (annotation.toString().contains("IBaseAPIContract")) {
                    return o.getDeclaredField(varName).get(null);
                }
            }
        } catch (NoSuchFieldException | ClassNotFoundException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Class<?> classOnly(String uri) {
        Class c = null;
        try {
            c = Class.forName(uri);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return c;
    }

    public static Object createObj(String clazz) {
        Class o = null;
        try {
            o = Class.forName(clazz);
            return o.newInstance();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }
}
