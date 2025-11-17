using System;

internal readonly struct HexCoordinates
{
	public int Q { get; }
	public int R { get; }
	public int S { get; }

	public HexCoordinates(int q, int r, int s)
	{
		if (q + r + s != 0)
		{
			throw new ArgumentException("Invalid axial coordinates: q + r + s must equal 0.");
		}

		Q = q;
		R = r;
		S = s;
	}

	public static HexCoordinates FromAxialCoordinates(int q, int r)
	{
		int s = -q - r;
		return new HexCoordinates(q, r, s);
	}
}