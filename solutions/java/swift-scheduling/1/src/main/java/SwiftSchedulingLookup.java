import java.lang.reflect.Constructor;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public final class SwiftSchedulingLookup {
    private static final List<Class<? extends DateTimeProvider>> DATE_TIME_PROVIDERS = List.of(
            NowProvider.class,
            AsapProvider.class,
            EndOfWeekProvider.class,
            MonthProvider.class,
            QuarterProvider.class
    );

    private final Map<Pattern, Class<? extends DateTimeProvider>> providerTypesByPattern = discoverProviders();
    private final Map<Class<?>, DateTimeProvider> cachedProviders = new HashMap<>();

    private static Map<Pattern, Class<? extends DateTimeProvider>> discoverProviders() {
        return DATE_TIME_PROVIDERS.stream()
                       .collect(toPatternMapCollector());
    }

    private static Collector<Class<? extends DateTimeProvider>,
                                    ?,
                                    LinkedHashMap<Pattern, Class<? extends DateTimeProvider>>> toPatternMapCollector() {
        return Collectors.toMap(
                SwiftSchedulingLookup::extractPattern,
                clazz -> clazz,
                (a, b) -> a,
                LinkedHashMap::new
        );
    }

    private static Pattern extractPattern(Class<? extends DateTimeProvider> clazz) {
        try {
            return (Pattern) clazz.getField("PATTERN")
                                     .get(null);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }

    public DateTimeProvider getProvider(String description) {
        return providerTypesByPattern.entrySet()
                       .stream()
                       .filter(entry -> matchesPattern(description, entry))
                       .findFirst()
                       .map(this::getOrCreateProvider)
                       .orElseThrow(() -> new IllegalArgumentException("No provider found for description: " + description));
    }

    private DateTimeProvider getOrCreateProvider(Map.Entry<Pattern, Class<? extends DateTimeProvider>> entry) {
        return cachedProviders.computeIfAbsent(
                entry.getValue(),
                SwiftSchedulingLookup::newInstance
        );
    }

    private static boolean matchesPattern(
            String description,
            Map.Entry<Pattern, Class<? extends DateTimeProvider>> entry
    ) {
        return entry.getKey()
                       .matcher(description)
                       .matches();
    }

    private static DateTimeProvider newInstance(Class<?> type) {
        try {
            Constructor<?> constructor = type.getDeclaredConstructor();
            return (DateTimeProvider) constructor.newInstance();
        }
        catch (Exception e) {
            throw new IllegalStateException("Failed to construct: " + type, e);
        }
    }
}
