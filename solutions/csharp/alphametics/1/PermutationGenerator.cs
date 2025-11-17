using System;
using System.Collections;
using System.Collections.Generic;

public class PermutationGenerator : IEnumerator<double>
{
	private readonly double _upperBound;

	public PermutationGenerator(int requiredDigits)
	{
		if (requiredDigits < 1)
		{
			throw new ArgumentException("Generator can't return less than one digit.");
		}

		if (requiredDigits > 10)
		{
			throw new ArgumentException("Generator can't return more than ten digits.");
		}

		_upperBound = Math.Pow(10, requiredDigits);
		SetInitialCurrent(requiredDigits);
	}

	private void SetInitialCurrent(int requiredDigits)
	{
		Current = (int)((requiredDigits == 1)
					? 0
					: (double)Math.Pow(10, requiredDigits - 1));

		if (!Current.AllUniqueDigits())
		{
			_ = MoveNext();
		}
	}

	public int Current { get; private set; }

	object IEnumerator.Current =>
		Current;

	double IEnumerator<double>.Current =>
		Current;
	public bool MoveNext()
	{
		do
		{
			Current++;
		}
		while (!Current.AllUniqueDigits() && Current < _upperBound);

		if (Current >= _upperBound)
		{
			Current = default;
			return false;
		}

		return true;
	}

	public void Reset() =>
		throw new NotImplementedException();

	public void Dispose() =>
		throw new NotImplementedException();
}
