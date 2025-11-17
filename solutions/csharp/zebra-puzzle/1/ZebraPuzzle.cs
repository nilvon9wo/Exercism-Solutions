using System;
using System.Collections.Generic;
using System.Linq;

// ReSharper disable once CheckNamespace

internal static class Constraints
{
	// 2. The Englishman lives in the red house.
	internal static bool EnglishmanLivesInRedHouse(ColorNationGrouping grouping)
		=> (grouping.Nations, Nationality.Englishman).IsSameIndex(grouping.Colors, Color.Red);

	// 3. The Spaniard owns the dog.
	internal static bool SpaniardOwnsDog(ColorNationDrinkPetsGrouping grouping)
		=> (grouping.Pets, Pet.Dog).IsSameIndex(grouping.Grouping.Grouping.Nations, Nationality.Spaniard);

	// 4. Coffee is drunk in the green house.
	internal static bool CoffeeIsDrunkInGreenHouse(ColorNationDrinkGrouping grouping)
		=> (grouping.Drinks, Drink.Coffee).IsSameIndex(grouping.Grouping.Colors, Color.Green);

	// 5. The Ukrainian drinks tea.
	internal static bool UkranianDrinksTea(ColorNationDrinkGrouping grouping)
		=> (grouping.Drinks, Drink.Tea).IsSameIndex(grouping.Grouping.Nations, Nationality.Ukranian);

	// 6. The green house is immediately to the right of the ivory house.
	internal static bool GreenHouseIsRightOfIvoryHouse(Color[] colors)
		=> (colors, Color.Green).IsRightOf(colors, Color.Ivory);

	// 7. The Old Gold smoker owns snails.
	internal static bool OldGoldSmokerOwnsSnails(ColorNationDrinkPetsSmokesGrouping grouping)
		=> (grouping.Smokes, Smoke.OldGold).IsSameIndex(grouping.Grouping.Pets, Pet.Snails);

	// 8. Kools are smoked in the yellow house.
	internal static bool KoolsAreSmokedInYellowHouse(ColorNationDrinkPetsSmokesGrouping grouping)
		=> (grouping.Smokes, Smoke.Kools).IsSameIndex(grouping.Grouping.Grouping.Grouping.Colors, Color.Yellow);

	// 9. Milk is drunk in the middle house.
	internal static bool MilkIsDrunkInMiddleHouse(ColorNationDrinkGrouping grouping)
		=> grouping.Drinks[2] == Drink.Milk;

	// 10. The Norwegian lives in the first house.
	internal static bool NorwegianLivesInFirstHouse(ColorNationGrouping grouping)
		=> grouping.Nations[0] == Nationality.Norwegian;

	// 11. The man who smokes Chesterfields lives in the house next to the man with the fox.
	internal static bool ManWhoSmokesChesterfieldsLivesNextToManWithFox(ColorNationDrinkPetsSmokesGrouping grouping)
		=> (grouping.Smokes, Smoke.Chesterfields).IsNextTo(grouping.Grouping.Pets, Pet.Fox);

	// 12. Kools are smoked in the house next to the house where the horse is kept.
	internal static bool KoolsSmokedInHouseNextToHouseWhereHorseKept(ColorNationDrinkPetsSmokesGrouping grouping)
		=> (grouping.Smokes, Smoke.Kools).IsNextTo(grouping.Grouping.Pets, Pet.Horse);

	//13. The Lucky Strike smoker drinks orange juice.
	internal static bool LuckStrikeSmokerDrinksOrangeJuice(ColorNationDrinkPetsSmokesGrouping grouping)
		=> (grouping.Smokes, Smoke.LuckyStrike).IsSameIndex(grouping.Grouping.Grouping.Drinks, Drink.OrangeJuice);

	// 14. The Japanese smokes Parliaments.
	internal static bool JapaneseSmokesParliaments(ColorNationDrinkPetsSmokesGrouping grouping)
		=> (grouping.Smokes, Smoke.Parliaments).IsSameIndex(
			grouping.Grouping.Grouping.Grouping.Nations,
			Nationality.Japanese
		);

	// 15. The Norwegian lives next to the blue house.
	internal static bool NorwegianLivesNextToBlueHouse(ColorNationGrouping grouping)
		=> (grouping.Nations, Nationality.Norwegian).IsNextTo(grouping.Colors, Color.Blue);
}

//=======================================================================

// ReSharper disable once CheckNamespace
public static class ZebraPuzzle
{
	private static readonly (Color[] colors, Drink[] drinks, Smoke[] smokes, Pet[] pets, Nationality[] nations) _solved
		= _constrainedHouses.First();

	private static IEnumerable<PropertySet> _constrainedHouses
		=> PropertyPermutations.Colors.Where(Constraints.GreenHouseIsRightOfIvoryHouse)
			.SelectMany(_ => PropertyPermutations.Nationalities, ColorNationGrouping.CreateGroupings)
			.Where(Constraints.NorwegianLivesInFirstHouse)
			.Where(Constraints.EnglishmanLivesInRedHouse)
			.Where(Constraints.NorwegianLivesNextToBlueHouse)
			.SelectMany(_ => PropertyPermutations.Drinks, ColorNationDrinkGrouping.CreateGroupings)
			.Where(Constraints.MilkIsDrunkInMiddleHouse)
			.Where(Constraints.CoffeeIsDrunkInGreenHouse)
			.Where(Constraints.UkranianDrinksTea)
			.SelectMany(_ => PropertyPermutations.Pets, ColorNationDrinkPetsGrouping.CreateGroupings)
			.Where(Constraints.SpaniardOwnsDog)
			.SelectMany(_ => PropertyPermutations.Smokes, ColorNationDrinkPetsSmokesGrouping.CreateGroupings)
			.Where(Constraints.OldGoldSmokerOwnsSnails)
			.Where(Constraints.KoolsAreSmokedInYellowHouse)
			.Where(Constraints.ManWhoSmokesChesterfieldsLivesNextToManWithFox)
			.Where(Constraints.KoolsSmokedInHouseNextToHouseWhereHorseKept)
			.Where(Constraints.LuckStrikeSmokerDrinksOrangeJuice)
			.Where(Constraints.JapaneseSmokesParliaments)
			.Select(PropertySet.From);

	public static Nationality DrinksWater()
		=> (Nationality)_solved.drinks.Single(drink => drink == Drink.Water);

	public static Nationality OwnsZebra()
		=> (Nationality)_solved.pets.Single(pet => pet == Pet.Zebra);
}

//=======================================================================

// ReSharper disable once CheckNamespace
public enum Color { Red, Green, Ivory, Yellow, Blue }

//=======================================================================

// ReSharper disable once CheckNamespace
public enum Drink { Coffee, Tea, Milk, OrangeJuice, Water }

//=======================================================================

// ReSharper disable once CheckNamespace
public enum Nationality { Englishman, Spaniard, Ukranian, Japanese, Norwegian }

//=======================================================================

// ReSharper disable once CheckNamespace
public enum Pet { Dog, Snails, Fox, Horse, Zebra }

//=======================================================================

// ReSharper disable once CheckNamespace
internal static class PropertyPermutations
{
	public static readonly IEnumerable<Color[]> Colors = Permute<Color>();
	public static readonly IEnumerable<Nationality[]> Nationalities = Permute<Nationality>();
	public static readonly IEnumerable<Drink[]> Drinks = Permute<Drink>();
	public static readonly IEnumerable<Pet[]> Pets = Permute<Pet>();
	public static readonly IEnumerable<Smoke[]> Smokes = Permute<Smoke>();

	private static IEnumerable<TEnum[]> Permute<TEnum>()
		=> ToEnumerable<TEnum>()
			.Permutations()
			.Select(p => p.ToArray());

	private static IEnumerable<TEnum> ToEnumerable<TEnum>()
		=> Enum.GetValues(typeof(TEnum))
			.Cast<TEnum>();
}

//=======================================================================

// ReSharper disable once CheckNamespace

internal record struct PropertySet(
	Color[] Colors,
	Drink[] Drinks,
	Smoke[] Smokes,
	Pet[] Pets,
	Nationality[] Nations
)
{
	public static PropertySet From(ColorNationDrinkPetsSmokesGrouping grouping)
		=> new(
			grouping.Grouping.Grouping.Grouping.Colors,
			grouping.Grouping.Grouping.Drinks,
			grouping.Smokes,
			grouping.Grouping.Pets,
			grouping.Grouping.Grouping.Grouping.Nations
		);

	public static implicit operator (Color[] colors, Drink[] drinks, Smoke[] smokes, Pet[] pets, Nationality[] nations)(
		PropertySet value
	)
		=> (value.Colors, value.Drinks, value.Smokes, value.Pets, value.Nations);

	public static implicit operator PropertySet(
		(Color[] colors, Drink[] drinks, Smoke[] smokes, Pet[] pets, Nationality[] nations) value
	)
		=> new(
			value.colors,
			value.drinks,
			value.smokes,
			value.pets,
			value.nations
		);
}

//=======================================================================

// ReSharper disable once CheckNamespace
public enum Smoke { OldGold, Kools, Chesterfields, LuckyStrike, Parliaments }

//=======================================================================

// ReSharper disable once CheckNamespace

internal record ColorNationDrinkGrouping(ColorNationGrouping Grouping, Drink[] Drinks)
{
	internal static ColorNationDrinkGrouping CreateGroupings(ColorNationGrouping grouping, Drink[] drinks)
		=> new(grouping, drinks);
}

//=======================================================================

// ReSharper disable once CheckNamespace

internal record ColorNationDrinkPetsGrouping(ColorNationDrinkGrouping Grouping, Pet[] Pets)
{
	internal static ColorNationDrinkPetsGrouping CreateGroupings(ColorNationDrinkGrouping grouping, Pet[] pets)
		=> new(grouping, pets);
}

//=======================================================================

// ReSharper disable once CheckNamespace

internal record ColorNationDrinkPetsSmokesGrouping(ColorNationDrinkPetsGrouping Grouping, Smoke[] Smokes)
{
	internal static ColorNationDrinkPetsSmokesGrouping CreateGroupings(
		ColorNationDrinkPetsGrouping grouping,
		Smoke[] smokes
	)
		=> new(grouping, smokes);
}

//=======================================================================

// ReSharper disable once CheckNamespace

internal record ColorNationGrouping(Color[] Colors, Nationality[] Nations)
{
	internal static ColorNationGrouping CreateGroupings(Color[] colors, Nationality[] nations)
		=> new(colors, nations);
}

//=======================================================================

// ReSharper disable once CheckNamespace
internal static class ArrayExtensions
{
	internal static bool IsRightOf<TProperty1, TProperty2>(
		this (TProperty1[] a, TProperty1 v) right,
		TProperty2[] arrayToCheck,
		TProperty2 targetValue
	)
		=> right.a.IndexOf(right.v) == (arrayToCheck.IndexOf(targetValue) + 1);

	internal static bool IsSameIndex<TProperty1, TProperty2>(
		this (TProperty1[] a, TProperty1 v) propertyPair,
		TProperty2[] arrayToCheck,
		TProperty2 targetValue
	)
		=> propertyPair.a.IndexOf(propertyPair.v) == arrayToCheck.IndexOf(targetValue);

	internal static bool IsNextTo<TProperty1, TProperty2>(
		this (TProperty1[] a, TProperty1 v) x,
		TProperty2[] firstArray,
		TProperty2 targetValue
	)
		=> (x.a, x.v).IsRightOf(firstArray, targetValue)
		   || (firstArray, targetValue).IsRightOf(x.a, x.v);

	private static int IndexOf<T>(this T[] arr, T obj)
		=> Array.IndexOf(arr, obj);
}

//=======================================================================

// ReSharper disable once CheckNamespace
internal static class EnumerableExtensions
{
	internal static IEnumerable<IEnumerable<T>> Permutations<T>(this IEnumerable<T> values)
	{
		IEnumerable<T> enumerable = values as T[] ?? values.ToArray();
		return enumerable.Count() == 1
			? enumerable.ToSingleton()
			: enumerable.SelectMany(v => Permutations(enumerable.Except(v.ToSingleton())), (v, p) => p.Prepend(v));
	}

	private static IEnumerable<T> ToSingleton<T>(this T item)
	{
		yield return item;
	}
}

//=======================================================================