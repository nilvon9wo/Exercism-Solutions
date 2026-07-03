public enum GameStatus {
    Playing,
    Finished,
    Loop;

    public String toString() {
        return this.name()
                   .toLowerCase();
    }
}
