using System;

// ReSharper disable once CheckNamespace
public class BankAccount
{
    private decimal _balance;
    private bool _isOpen;
    private readonly object _balanceLock = new();

    public void Open()
        => _isOpen = true;

    public void Close()
        => _isOpen = false;

    public decimal Balance
        => !_isOpen
            ? throw new InvalidOperationException("Account is closed.")
            : _balance;

    public void UpdateBalance(decimal change)
    {
        if (!_isOpen)
        {
            throw new InvalidOperationException("Account is closed.");
        }

        lock (_balanceLock)
        {
            _balance += change;
        }
    }
}