import java.util.Objects;
public final class Result<T> {
    private Result(T value, Exception exception) {
        this.value = value;
        this.exception = exception;
    }

    private final T value;
    public T getValue() {
        if (exception != null || value == null) {
            throw new IllegalStateException("Value is invalid");
        }
        return value;
    }

    private final Exception exception;
    public Exception getException() {
        if (exception == null) {
            throw new IllegalStateException("Exception is null");
        }
        return exception;
    }

    public static <T> Result<T> success(T value) {
        Objects.requireNonNull(value, "value");
        return new Result<>(value, null);
    }

    public static <T> Result<T> failure(Exception exception) {
        Objects.requireNonNull(exception, "exception");
        return new Result<>(null, exception);
    }

    public boolean isValid() {
        return exception == null;
    }
}