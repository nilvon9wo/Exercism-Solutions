import java.util.Arrays;

class BirdWatcher {
    private final int[] birdsPerDay;

    public BirdWatcher(int[] birdsPerDay) {
        this.birdsPerDay = birdsPerDay.clone();
    }

    private static final int[] lastWeekCounts = new int[] {0, 2, 5, 3, 7, 8, 4 };

    public int[] getLastWeek() {
        return lastWeekCounts;
    }

    public int getToday() {
        return birdsPerDay[birdsPerDay.length - 1];
    }

    public void incrementTodaysCount() {
        int lastIndex = birdsPerDay.length - 1;
        birdsPerDay[lastIndex]++;
    }

    public boolean hasDayWithoutBirds() {
        return Arrays.stream(birdsPerDay)
                     .anyMatch(x -> x == 0);
    }

    public int getCountForFirstDays(int numberOfDays) {
        return Arrays.stream(birdsPerDay)
                     .limit(numberOfDays)
                     .sum();
    }

    public int getBusyDays() {
        return (int) Arrays.stream(birdsPerDay)
                           .filter(x -> x >= 5)
                           .count();
    }
}
