import java.lang.reflect.Method;
import java.time.format.TextStyle;
import java.util.Locale;

public class DateUtils {
	public static String toPascalCase(Object temporalEnum) {
		try {
			Method method = temporalEnum.getClass()
					                .getMethod("getDisplayName", TextStyle.class, Locale.class);
			return (String) method.invoke(temporalEnum, TextStyle.FULL, Locale.ENGLISH);
		}
		catch (NoSuchMethodException e) {
			throw new IllegalArgumentException(
					"Object of type " + temporalEnum.getClass() + " does not have getDisplayName", e);
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
