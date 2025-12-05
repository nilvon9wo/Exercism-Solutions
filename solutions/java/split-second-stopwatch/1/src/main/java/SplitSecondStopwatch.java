import java.util.ArrayList;
import java.util.List;

public class SplitSecondStopwatch {
    private final SplitSecondStopwatchHelpers stopwatchHelpers;
    private SplitSecondStopwatch(SplitSecondStopwatchHelpers stopwatchHelpers) {
        this.stopwatchHelpers = stopwatchHelpers;
    }

    public SplitSecondStopwatch(){
        this(new SplitSecondStopwatchHelpers());
    }

    private StopwatchState currentState = StopwatchState.READY;
    private int currentLapSeconds = 0;
    private int totalSeconds = 0;
    private final List<Integer> previousLapSecondsList = new ArrayList<>();

    public void start() {
        switch (this.currentState) {
            case READY, STOPPED
                    -> this.currentState = StopwatchState.RUNNING;

            case RUNNING
                    -> throw new IllegalStateException("cannot start an already running stopwatch");
        }
    }

    public void stop() {
        if (this.currentState != StopwatchState.RUNNING) {
            throw new IllegalStateException("cannot stop a stopwatch that is not running");
        }

        this.currentState = StopwatchState.STOPPED;
    }

    public void reset() {
        if (this.currentState != StopwatchState.STOPPED) {
            throw new IllegalStateException("cannot reset a stopwatch that is not stopped");
        }

        this.currentState = StopwatchState.READY;
        this.currentLapSeconds = 0;
        this.totalSeconds = 0;
        this.previousLapSecondsList.clear();
    }

    public void lap() {
        if (this.currentState != StopwatchState.RUNNING) {
            throw new IllegalStateException("cannot lap a stopwatch that is not running");
        }

        this.previousLapSecondsList.add(this.currentLapSeconds);
        this.currentLapSeconds = 0;
    }

    public String state() {
        return switch (this.currentState) {
            case READY -> "ready";
            case RUNNING -> "running";
            case STOPPED -> "stopped";
        };
    }

    public String currentLap() {
        return this.stopwatchHelpers.formatSeconds(this.currentLapSeconds);
    }

    public String total() {
        return this.stopwatchHelpers.formatSeconds(this.totalSeconds);
    }

    public List<String> previousLaps() {
        return this.previousLapSecondsList.stream()
                       .map(this.stopwatchHelpers::formatSeconds)
                       .toList();
    }

    public void advanceTime(String timeString) {
        if (this.currentState == StopwatchState.RUNNING) {
            int secondsToAdd = this.stopwatchHelpers.parseTimeStringToSeconds(timeString);
            this.currentLapSeconds += secondsToAdd;
            this.totalSeconds += secondsToAdd;
        }
    }
}
