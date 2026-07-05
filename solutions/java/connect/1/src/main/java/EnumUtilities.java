import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

public final class EnumUtilities {

    public static <TEnum extends Enum<TEnum>, TAttribute extends Annotation, TKey>
    Map<TKey, TEnum> toEnumByAttributeValue(Class<TEnum> enumClass,
                                            Class<TAttribute> attributeClass,
                                            Class<TKey> keyClass) {

        Map<TKey, TEnum> result = new HashMap<>();

        for (TEnum enumValue : enumClass.getEnumConstants()) {

            try {
                // 1. get enum field
                var field = enumValue.getClass()
                                     .getField(enumValue.name());

                // 2. get annotation (THIS is what attributeClass is for)
                TAttribute attribute = field.getAnnotation(attributeClass);

                if (attribute == null) {
                    continue; // matches C# implicit "missing attribute = skip"
                }

                // 3. extract key from annotation (THIS replaces C# GetValue<TAttribute,TKey>)
                TKey key = extractKey(attribute, keyClass);

                result.put(key, enumValue);

            } catch (NoSuchFieldException e) {
                throw new IllegalStateException(
                        "Enum constant not found: " + enumValue.name(),
                        e
                );
            }
        }

        return result;
    }

    private static <TAttribute extends Annotation, TKey>
    TKey extractKey(TAttribute attribute, Class<TKey> keyClass) {

        try {
            for (Method method : attribute.annotationType().getDeclaredMethods()) {

                if (method.getParameterCount() == 0
                    && wrap(method.getReturnType()).equals(wrap(keyClass))) {

                    Object value = method.invoke(attribute);
                    return keyClass.cast(value);
                }
            }

            throw new IllegalStateException(
                    "No attribute property found matching key type: " + keyClass
            );

        } catch (Exception e) {
            throw new IllegalStateException("Failed to extract key from attribute", e);
        }
    }

    private static Class<?> wrap(Class<?> type) {
        if (!type.isPrimitive()) return type;

        return switch (type.getName()) {
            case "char" -> Character.class;
            case "int" -> Integer.class;
            case "boolean" -> Boolean.class;
            case "byte" -> Byte.class;
            case "short" -> Short.class;
            case "long" -> Long.class;
            case "float" -> Float.class;
            case "double" -> Double.class;
            default -> type;
        };
    }

    public static <TEnum extends Enum<TEnum>, TTarget extends Enum<TTarget>>
    TTarget to(TEnum enumValue, Class<TTarget> targetEnumClass) {

        if (enumValue == null) {
            throw new IllegalArgumentException("enumValue must not be null");
        }

        String enumName = enumValue.name();

        try {
            return Enum.valueOf(targetEnumClass, enumName);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "No matching enum value found in " + targetEnumClass + " for " + enumValue,
                    e
            );
        }
    }

    public static <T extends Annotation>
    T getAttributeValue(Enum<?> enumValue, Class<T> attributeClass) {

        if (enumValue == null) {
            throw new IllegalArgumentException("enumValue must not be null");
        }

        try {
            Field fieldInfo = enumValue.getDeclaringClass()
                                       .getField(enumValue.name());

            T[] attributes = (T[]) fieldInfo.getDeclaredAnnotationsByType(attributeClass);

            if (attributes.length == 1) {
                return attributes[0];
            }

            if (attributes.length > 1) {
                throw new IllegalStateException(
                        "Enum value has multiple attributes of the specified type."
                );
            }

            throw new IllegalStateException(
                    "Enum value does not have an attribute of the specified type."
            );

        } catch (NoSuchFieldException e) {
            throw new IllegalStateException(
                    "Enum constant not found: " + enumValue.name(),
                    e
            );
        }
    }
}