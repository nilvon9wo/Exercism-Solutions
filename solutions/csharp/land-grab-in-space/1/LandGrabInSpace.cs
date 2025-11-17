using System;
using System.Collections.Generic;
using System.Linq;

public readonly struct Coord
{
	public Coord(ushort x, ushort y)
	{
		X = x;
		Y = y;
	}

	public ushort X { get; }
	public ushort Y { get; }
}

public readonly struct Plot
{
	private readonly Coord _coord1;
	private readonly Coord _coord2;
	private readonly Coord _coord3;
	private readonly Coord _coord4;

	public Plot(Coord coord1, Coord coord2, Coord coord3, Coord coord4)
	{
		_coord1 = coord1;
		_coord2 = coord2;
		_coord3 = coord3;
		_coord4 = coord4;
	}

	public double GetLongestSide() =>
		new List<double>()
		{
			Length(_coord1, _coord2),
			Length(_coord2, _coord3),
			Length(_coord3, _coord4),
			Length(_coord4, _coord1),
		}.Max();

	private double Length(Coord coord1, Coord coord2)
	{
		int a = Math.Abs(coord2.X - coord1.X);
		int b = Math.Abs(coord2.Y - coord1.Y);
		return Math.Sqrt(Math.Pow(a, 2) + Math.Pow(b, 2));
	}
}

public class ClaimsHandler
{
	private readonly List<Plot> _claims = new();

	public void StakeClaim(Plot plot) =>
		_claims.Add(plot);

	public bool IsClaimStaked(Plot plot) =>
		_claims.Contains(plot);

	public bool IsLastClaim(Plot plot) =>
		_claims.Last()
			.Equals(plot);

	public Plot GetClaimWithLongestSide()
	{
		Dictionary<double, Plot> plotByLongestSide = _claims
			.ToDictionary(x => x.GetLongestSide(), x => x);

		double longestSideKey = plotByLongestSide.Keys.Max();
		return plotByLongestSide[longestSideKey];
	}
}
