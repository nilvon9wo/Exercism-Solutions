#pragma warning disable IDE0079
#pragma warning disable IDE0130
#pragma warning disable CA1050

public class SplitSecondStopwatch(TimeProvider time)
{
    private long _startTimestamp;
    private long _currentLapStartTimestamp;
    private TimeSpan _currentLapElapsed;
    private TimeSpan _totalElapsed;

    public StopwatchState State { get; private set; } = StopwatchState.Ready;

    public TimeSpan CurrentLap => State == StopwatchState.Running
        ? _currentLapElapsed + time.GetElapsedTime(_currentLapStartTimestamp)
        : _currentLapElapsed;

    public TimeSpan Total => State == StopwatchState.Running
        ? _totalElapsed + time.GetElapsedTime(_startTimestamp)
        : _totalElapsed;

    public IReadOnlyCollection<TimeSpan> PreviousLaps { get; private set; } = [];

    public void Start()
    {
        if (State == StopwatchState.Running)
        {
            throw new InvalidOperationException("Start can only be called when the stopwatch is not running.");
        }

        long currentTimestamp = time.GetTimestamp();

        switch (State)
        {
            case StopwatchState.Ready:
                _startTimestamp = currentTimestamp;
                _currentLapStartTimestamp = currentTimestamp;
                _currentLapElapsed = TimeSpan.Zero;
                _totalElapsed = TimeSpan.Zero;
                break;
            case StopwatchState.Stopped:
                _startTimestamp = currentTimestamp;
                _currentLapStartTimestamp = currentTimestamp;
                break;
        }

        State = StopwatchState.Running;
    }

    public void Stop()
    {
        if (State != StopwatchState.Running)
        {
            throw new InvalidOperationException("Stop can only be called when the stopwatch is running.");
        }

        _currentLapElapsed += time.GetElapsedTime(_currentLapStartTimestamp);
        _totalElapsed += time.GetElapsedTime(_startTimestamp);

        State = StopwatchState.Stopped;
    }

    public void Reset()
    {
        if (State == StopwatchState.Ready || State == StopwatchState.Running)
        {
            throw new InvalidOperationException("Reset can only be called when the stopwatch is stopped.");
        }

        State = StopwatchState.Ready;
        _startTimestamp = 0;
        _currentLapStartTimestamp = 0;
        _currentLapElapsed = TimeSpan.Zero;
        _totalElapsed = TimeSpan.Zero;
        PreviousLaps = [];
    }

    public void Lap()
    {
        if (State != StopwatchState.Running)
        {
            throw new InvalidOperationException("Lap can only be called when the stopwatch is running.");
        }

        TimeSpan currentLapTime = _currentLapElapsed + time.GetElapsedTime(_currentLapStartTimestamp);

        List<TimeSpan> previousLapsList = PreviousLaps.ToList();
        previousLapsList.Add(currentLapTime);
        PreviousLaps = previousLapsList.AsReadOnly();

        _currentLapElapsed = TimeSpan.Zero;
        _currentLapStartTimestamp = time.GetTimestamp();
    }
}

#pragma warning disable IDE0079
#pragma warning disable IDE0130
#pragma warning disable CA1050
public enum StopwatchState
{
    Ready,
    Running,
    Stopped
}