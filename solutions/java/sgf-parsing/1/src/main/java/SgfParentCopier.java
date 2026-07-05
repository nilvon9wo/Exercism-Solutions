import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SgfParentCopier {
    public static Map<String, List<String>> copy(SgfNode parent) {
        return new HashMap<>(parentProperties(parent));
    }

    private static Map<String, List<String>> parentProperties(SgfNode parent) {
        try {
            var field = SgfNode.class.getDeclaredField("properties");
            field.setAccessible(true);
            //noinspection unchecked
            return (Map<String, List<String>>) field.get(parent);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
