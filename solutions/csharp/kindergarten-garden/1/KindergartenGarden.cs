using System;
using System.Collections.Generic;
using System.Linq;

public class KindergartenGarden
{
	private readonly string _diagram;

	private readonly string[] _students = new string[] {
		"Alice",
		"Bob",
		"Charlie",
		"David",
		"Eve",
		"Fred",
		"Ginny",
		"Harriet",
		"Ileana",
		"Joseph",
		"Kincaid",
		"Larry"
	};

	public KindergartenGarden(string diagram) =>
		_diagram = diagram;

	public IEnumerable<Plant> Plants(string student)
	{
		int index = Array.BinarySearch(_students, student);
		if (index < 0)
		{
			throw new ArgumentOutOfRangeException(nameof(student), "Student does not exist.");
		}

		Plant[][] plantRows = _diagram.Split('\n')
			.Select(x => x.ToPlants())
			.ToArray();

		int firstPlant = index * 2;
		int secondPlant = firstPlant + 1;
		return new Plant[]
		{
			plantRows[0][firstPlant],
			plantRows[0][secondPlant],
			plantRows[1][firstPlant],
			plantRows[1][secondPlant],
		};
	}
}

public enum Plant
{
	[PlantCode('V')]
	Violets,

	[PlantCode('R')]
	Radishes,

	[PlantCode('C')]
	Clover,

	[PlantCode('G')]
	Grass
}

public static class StringExtensions
{
	public static Plant[] ToPlants(this string value) =>
		value.ToCharArray()
			.Select(x => x.ToPlant())
			.ToArray();
}

public static class CharacterExtensions
{
	private static Dictionary<char, Plant> __plantByCode;

	private static Dictionary<char, Plant> _plantByCode
	{
		get
		{
			__plantByCode ??= Enum.GetValues(typeof(Plant))
					.Cast<Plant>()
					.Aggregate(
						new Dictionary<char, Plant>(),
							(seed, plant) =>
								{
									char code = plant.ToCode();
									seed[code] = plant;
									return seed;
								}
					);
			return __plantByCode;
		}
	}

	public static Plant ToPlant(this char character) =>
		_plantByCode.TryGetValue(character, out Plant plant)
			? plant
			: throw new ArgumentOutOfRangeException(nameof(character), $"No plant exists for character `{character}.");
}

public static class PlantExtensions
{
	public static char ToCode(this Plant plant) =>
		ToValue(plant);

	private static char ToValue(this Plant plant)
	{
		PlantCodeAttribute[] attributes = GetAttributes(plant);
		return attributes.Length > 0
			? attributes[0].Value
			: default;
	}

	private static PlantCodeAttribute[] GetAttributes(this Plant plant)
	{
		PlantCodeAttribute[] attributes = (PlantCodeAttribute[])plant
		   .GetType()
		   .GetField(plant.ToString())
		   .GetCustomAttributes(typeof(PlantCodeAttribute), false);

		return attributes;
	}
}

[AttributeUsage(AttributeTargets.Field)]
public class PlantCodeAttribute : Attribute
{
	public PlantCodeAttribute(char value) =>
		Value = value;

	public char Value { get; }
}
