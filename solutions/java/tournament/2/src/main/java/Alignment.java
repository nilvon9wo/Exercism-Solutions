enum Alignment {
    LEFT("-"),
    RIGHT("");

    Alignment(String flag) {
        this.flag = flag;
    }

    private final String flag;
    public String getFlag() {
        return flag;
    }
}