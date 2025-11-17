using System;
using System.Collections.Generic;
using System.Linq;

// ReSharper disable once CheckNamespace
public static class ZebraPuzzle
{
	private static readonly Lazy<IEnumerable<Person>> _solution = new(
		() =>
		{
			Person[] candidates = PersonFactory
				.CreateAll()
				.Where(person => Constraints.HouseRules.All(rule => Tester.Test(person, rule)))
				.ToArray();
			return PersonSetFinder.FindDistinctPersonSets(candidates)
				.First(
					set =>
						Constraints.NeighborRules.All(rule => Tester.Test(set, rule))
				);
		}
	);

	private static Nationality FindNationalityFor(Func<Person, bool> predicate)
		=> Find(predicate)
			.Nationality;

	private static Person Find(Func<Person, bool> predicate)
		=> _solution.Value
			.Single(predicate);

	public static Nationality DrinksWater()
		=> FindNationalityFor(person => person.Drink == Drink.Water);

	public static Nationality OwnsZebra()
		=> FindNationalityFor(person => person.Pet == Pet.Zebra);
}

//=======================================================================

// ReSharper disable once CheckNamespace

internal static class Constraints
{
	internal static readonly (object subject, object fact, bool isTrue)[] HouseRules =
	{
		(Nationality.Englishman, Color.Red, true), // 2. The Englishman lives in the red house.
		(Nationality.Spaniard, Pet.Dog, true), // 3. The Spaniard owns the dog.
		(Drink.Coffee, Color.Green, true), // 4. Coffee is drunk in the green house.
		(Nationality.Ukranian, Drink.Tea, true), // 5. The Ukrainian drinks tea.
		(Smoke.OldGold, Pet.Snails, true), // 7. The Old Gold smoker owns snails.
		(Smoke.Kools, Color.Yellow, true), // 8. Kools are smoked in the yellow house.
		(Drink.Milk, House.Middle, true), // 9. Milk is drunk in the middle house.
		(Nationality.Norwegian, House.FarLeft, true), // 10. The Norwegian lives in the first house.
		(Smoke.Chesterfields, Pet.Fox,
			false), // 11. The man who smokes Chesterfields lives in the house next to the man with the fox.
		(Smoke.Kools, Pet.Horse, false), // 12. Kools are smoked in the house next to the house where the horse is kept.
		(Smoke.LuckyStrike, Drink.OrangeJuice, true), // 13. The Lucky Strike smoker drinks orange juice.
		(Nationality.Japanese, Smoke.Parliaments, true), // 14. The Japanese smokes Parliaments.
		(Nationality.Norwegian, Color.Blue, false), // 15.The Norwegian lives next to the blue house.
	};

	internal static readonly (object subject, object fact, Neighbor relation)[] NeighborRules =
	{
		(Color.Green, Color.Ivory,
			Neighbor.RightOf), // 6. The green house is immediately to the right of the ivory house.
		(Smoke.Chesterfields, Pet.Fox,
			Neighbor.Either), // 11. The man who smokes Chesterfields lives in the house next to the man with the fox.
		(Smoke.Kools, Pet.Horse,
			Neighbor.Either), // 12. Kools are smoked in the house next to the house where the horse is kept.
		(Nationality.Norwegian, Color.Blue, Neighbor.Either), // 15.The Norwegian lives next to the blue house.
	};
}

//=======================================================================

// ReSharper disable once CheckNamespace
internal static class Tester
{
	internal static bool Applies(Person person, object aspect)
		=> aspect switch
		{
			Color color => person.Color == color,
			Nationality nationality => person.Nationality == nationality,
			Pet pet => person.Pet == pet,
			Drink drink => person.Drink == drink,
			Smoke smoke => person.Smokes == smoke,
			_ => person.House == (House)aspect,
		};

	internal static bool Test(Person person, (object subject, object fact, bool isTrue) rule)
		=> Applies(person, rule.subject)
			? (rule.isTrue && Applies(person, rule.fact)) || (!rule.isTrue && !Applies(person, rule.fact))
			: (rule.isTrue && !Applies(person, rule.fact)) || !rule.isTrue;

	internal static bool Test(IEnumerable<Person> set, (object subject, object target, Neighbor fact) rule)
	{
		IEnumerable<Person> persons = set as Person[] ?? set.ToArray();
		Person[] sorted = persons.OrderBy(o => o.House)
			.ToArray();
		IEnumerable<(Person left, Person right)> pairs = sorted.Zip(sorted.Skip(1), (a, b) => (left: a, right: b));
		return rule.fact switch
		{
			Neighbor.LeftOf => pairs.Any(o => Applies(o.left, rule.subject) && Applies(o.right, rule.target)),
			Neighbor.RightOf => pairs.Any(o => Applies(o.right, rule.subject) && Applies(o.left, rule.target)),
			_ => Test(persons, (rule.subject, rule.target, Neighbor.LeftOf))
				 || Test(persons, (rule.subject, rule.target, Neighbor.RightOf)),
		};
	}
}

//=======================================================================

// ReSharper disable once CheckNamespace
public class Person
{
	public Color Color { get; }
	public Nationality Nationality { get; }
	public Pet Pet { get; }
	public Drink Drink { get; }
	public Smoke Smokes { get; }
	public House House { get; }

	public Person(
		Color color,
		Nationality nationality,
		Pet pet,
		Drink drinks,
		Smoke smokes,
		House house
	)
		=> (Color, Nationality, Pet, Drink, Smokes, House) =
			(color, nationality, pet, drinks, smokes, house);

	public bool NoConflict(Person other)
	{
		_ = other ?? throw new ArgumentNullException(nameof(other));
		return (Color != other.Color)
			   && (Nationality != other.Nationality)
			   && (Pet != other.Pet)
			   && (Drink != other.Drink)
			   && (Smokes != other.Smokes)
			   && (House != other.House);
	}
}

//=======================================================================

// ReSharper disable once CheckNamespace
internal static class PersonFactory
{
	internal static IEnumerable<Person> CreateAll()
	{
		IEnumerable<Person> possibilities = CreateRange()
			.SelectMany(CreateForColor);

		foreach (Person? possible in possibilities)
		{
			yield return possible;
		}
	}

	private static IEnumerable<Person> CreateForColor(int color)
		=> CreateRange()
			.SelectMany(nationality => CreateForNationality(color, nationality));

	private static IEnumerable<Person> CreateForNationality(int color, int nationality)
		=> CreateRange()
			.SelectMany(pet => CreateFor(color, nationality, pet));

	private static IEnumerable<Person> CreateFor(int color, int nationality, int pet)
		=> CreateRange()
			.SelectMany(
				drink => CreateForDrink(
					color,
					nationality,
					pet,
					drink
				)
			);

	private static IEnumerable<Person> CreateForDrink(
		int color,
		int nationality,
		int pet,
		int drink
	)
		=> CreateRange()
			.SelectMany(
				smoke => CreateHousePermutations(
					color,
					nationality,
					pet,
					drink,
					smoke
				)
			);

	private static IEnumerable<Person> CreateHousePermutations(
		int color,
		int nationality,
		int pet,
		int drink,
		int smoke
	)
		=> CreateRange()
			.Select(
				house =>
					new Person(
						(Color)color,
						(Nationality)nationality,
						(Pet)pet,
						(Drink)drink,
						(Smoke)smoke,
						(House)house
					)
			);

	private static IEnumerable<int> CreateRange()
		=> Enumerable.Range(0, 5);
}

//=======================================================================

// ReSharper disable once CheckNamespace
internal static class PersonSetFinder
{
	internal static IEnumerable<IEnumerable<Person>> FindDistinctPersonSets(Person[] allCandidates)
		=> FindDistinctPersonSets(allCandidates, Enumerable.Empty<Person>());

	private static IEnumerable<IEnumerable<Person>> FindDistinctPersonSets(
		Person[] allCandidates,
		IEnumerable<Person> partialCandidateSet
	)
	{
		IEnumerable<Person> candidateSet = partialCandidateSet as Person[] ?? partialCandidateSet.ToArray();

		if (candidateSet.Count() == 5)
		{
			yield return candidateSet;
			yield break;
		}

		foreach (IEnumerable<Person>? subSet in FindCompatibleCandidates(candidateSet, allCandidates))
		{
			yield return subSet;
		}
	}

	private static IEnumerable<IEnumerable<Person>> FindCompatibleCandidates(
		IEnumerable<Person> existingCandidates,
		Person[] allCandidates
	)
		=> allCandidates
			.Where(candidate => IsCandidateValid(existingCandidates, candidate))
			.SelectMany(
				candidate => FindDistinctPersonSets(allCandidates, existingCandidates.Append(candidate))
					.Distinct()
			)
			.Distinct();

	private static bool IsCandidateValid(IEnumerable<Person> existingPeople, Person candidate)
	{
		Person[] existingArray = existingPeople as Person[] ?? existingPeople.ToArray();
		return !existingArray.Contains(candidate) && existingArray.All(existing => existing.NoConflict(candidate));
	}
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
public enum Smoke { OldGold, Kools, Chesterfields, LuckyStrike, Parliaments }

//=======================================================================

// ReSharper disable once CheckNamespace

public enum House { FarLeft, Left, Middle, Right, FarRight }

//=======================================================================

// ReSharper disable once CheckNamespace

public enum Neighbor { LeftOf, RightOf, Either }

//=======================================================================