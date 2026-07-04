import java.lang.reflect.Field;
import java.util.EnumMap;
import java.util.Map;

public class StatFieldMap {
    public static final Map<StatColumn, Field> FIELD_BY_COLUMNS = initFieldMap();

    private static Map<StatColumn, Field> initFieldMap() {
        Map<StatColumn, Field> map = new EnumMap<>(StatColumn.class);

        for (Field field : TeamRecord.class.getDeclaredFields()) {
            StatField annotation = field.getAnnotation(StatField.class);
            if (annotation != null) {
                field.setAccessible(true);
                map.put(annotation.value(), field);
            }
        }

        return map;
    }
}
