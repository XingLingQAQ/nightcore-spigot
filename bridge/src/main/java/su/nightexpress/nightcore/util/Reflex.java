package su.nightexpress.nightcore.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Reflex {

    @Nullable
    public static Class<?> getClass(@NotNull String path, @NotNull String name) {
        return getClass(path + "." + name);
    }

    @Nullable
    public static Class<?> getInnerClass(@NotNull String path, @NotNull String name) {
        return getClass(path + "$" + name);
    }

    @Nullable
    public static Class<?> getNMSClass(@NotNull String path, @NotNull String realName) {
        return getNMSClass(path, realName, null);
    }

    @Nullable
    public static Class<?> getNMSClass(@NotNull String path, @NotNull String realName, @Nullable String obfName) {
        Class<?> byRealName = getClass(path + "." + realName, false);
        if (byRealName != null) {
            return byRealName;
        }

        if (obfName != null) {
            return getClass(path + "." + obfName, false);
        }

        return null;
    }

    private static Class<?> getClass(@NotNull String path) {
        return getClass(path, true);
    }

    private static Class<?> getClass(@NotNull String path, boolean printError) {
        try {
            return Class.forName(path);
        }
        catch (ClassNotFoundException exception) {
            if (printError) exception.printStackTrace();
            return null;
        }
    }

    public static Constructor<?> getConstructor(@NotNull Class<?> source, Class<?>... types) {
        try {
            Constructor<?> constructor = source.getDeclaredConstructor(types);
            constructor.setAccessible(true);
            return constructor;
        }
        catch (ReflectiveOperationException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public static Object invokeConstructor(@NotNull Constructor<?> constructor, Object... obj) {
        try {
            return constructor.newInstance(obj);
        }
        catch (ReflectiveOperationException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    @NotNull
    public static <T> List<T> getFields(@NotNull Class<?> source, @NotNull Class<T> type) {
        List<T> list = new ArrayList<>();

        for (Field field : Reflex.getFields(source)) {
            if (!field.getDeclaringClass().equals(source)) continue;
            //if (!field.canAccess(null)) continue;
            if (!Modifier.isStatic(field.getModifiers())) continue;
            if (!Modifier.isFinal(field.getModifiers())) continue;
            if (!type.isAssignableFrom(field.getType())) continue;
            if (!field.trySetAccessible()) continue;

            try {
                list.add(type.cast(field.get(null)));
            }
            catch (IllegalArgumentException | IllegalAccessException exception) {
                exception.printStackTrace();
            }
        }

        return list;
    }

    @NotNull
    public static List<Field> getFields(@NotNull Class<?> source) {
        List<Field> result = new ArrayList<>();

        Class<?> clazz = source;
        while (clazz != null && clazz != Object.class) {
            if (!result.isEmpty()) {
                result.addAll(0, Arrays.asList(clazz.getDeclaredFields()));
            }
            else {
                Collections.addAll(result, clazz.getDeclaredFields());
            }
            clazz = clazz.getSuperclass();
        }

        return result;
    }

    public static Field getField(@NotNull Class<?> source, @NotNull String name) {
        try {
            return source.getDeclaredField(name);
        }
        catch (NoSuchFieldException exception) {
            Class<?> superClass = source.getSuperclass();
            return superClass == null ? null : getField(superClass, name);
        }
    }

    public static Object getFieldValue(@NotNull Object source, @NotNull String realName, @NotNull String obfName) {
        Object byName = getFieldValue(source, realName);
        return byName == null ? getFieldValue(source, obfName) : byName;
    }

    public static Object getFieldValue(@NotNull Object source, @NotNull String name) {
        try {
            Class<?> clazz = source instanceof Class<?> ? (Class<?>) source : source.getClass();
            Field field = getField(clazz, name);
            if (field == null) return null;

            field.setAccessible(true);
            return field.get(source);
        }
        catch (IllegalAccessException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public static boolean setFieldValue(@NotNull Object source, @NotNull String name, @Nullable Object value) {
        try {
            boolean isStatic = source instanceof Class;
            Class<?> clazz = isStatic ? (Class<?>) source : source.getClass();

            Field field = getField(clazz, name);
            if (field == null) return false;

            field.setAccessible(true);
            field.set(isStatic ? null : source, value);
            return true;
        }
        catch (IllegalAccessException exception) {
            exception.printStackTrace();
        }
        return false;
    }

    public static Method getMethod(@NotNull Class<?> source, @NotNull String realName, @NotNull String obfName, @NotNull Class<?>... params) {
        Method byName = getMethod(source, realName, params);
        return byName == null ? getMethod(source, obfName, params) : byName;
    }

    public static Method getMethod(@NotNull Class<?> source, @NotNull String name, @NotNull Class<?>... params) {
        try {
            return source.getDeclaredMethod(name, params);
        }
        catch (NoSuchMethodException exception) {
            Class<?> superClass = source.getSuperclass();
            return superClass == null ? null : getMethod(superClass, name);
        }
    }

    public static Object invokeMethod(@NotNull Method method, @Nullable Object by, @Nullable Object... param) {
        method.setAccessible(true);
        try {
            return method.invoke(by, param);
        }
        catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException exception) {
            exception.printStackTrace();
        }
        return null;
    }
}
