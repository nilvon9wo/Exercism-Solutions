// ReSharper disable once CheckNamespace
public abstract class Cell
{
    private int _value;

    public int Value
    {
        get
            => _value;
        set
        {
            if (_value != value)
            {
                _value = value;
                OnChanged(value);
            }
        }
    }

    protected Cell(int value)
        => _value = value;

    public EventHandler<int>? Changed { get; set; }

    private void OnChanged(int newValue)
        => Changed?.Invoke(this, newValue);
}