import java.util.Objects;

class PythagoreanTriplet {
    private final int a;
    private final int b;
    private final int c;

    PythagoreanTriplet(int a, int b, int c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    static TripletListBuilder makeTripletsList() {
        return new TripletListBuilder();
    }

    @Override
    public boolean equals(Object other) {
        return this == other
               || other instanceof PythagoreanTriplet triplet
                        && a == triplet.a
                        && b == triplet.b
                        && c == triplet.c;
    }

    @Override
    public int hashCode() {
        return Objects.hash(a, b, c);
    }
}