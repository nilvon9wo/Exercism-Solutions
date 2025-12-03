import java.util.Comparator;
import java.util.function.ToIntFunction;

public enum Axis {
	ROW(Coordinate::row, Coordinate::column),
	COLUMN(Coordinate::column, Coordinate::row);

	private final ToIntFunction<Coordinate> primary;
	private final ToIntFunction<Coordinate> secondary;
	Axis(ToIntFunction<Coordinate> primary, ToIntFunction<Coordinate> secondary) {
		this.primary = primary;
		this.secondary = secondary;
	}

	public int primaryIndexOf(Coordinate coordinate) {
		return this.primary.applyAsInt(coordinate);
	}

	public int secondaryIndexOf(Coordinate coordinate) {
		return this.secondary.applyAsInt(coordinate);
	}

	public Comparator<Coordinate> comparator() {
		return Comparator.comparingInt(this::secondaryIndexOf);
	}
}
