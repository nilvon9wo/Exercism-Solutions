import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class Flattener {

    List<Object> flatten(List<?> list) {
        return list.stream()
                       .flatMap(this::flatten)
                       .collect(Collectors.toList());
    }

    private Stream<Object> flatten(Object element) {
        return this.flattenElement(element)
                       .stream();
    }

    private List<Object> flattenElement(Object element) {
        return element == null
                       ? List.of()
                       : (element instanceof List<?> nestedList)
                                 ? this.flatten(nestedList)
                                 : List.of(element);
    }
}
