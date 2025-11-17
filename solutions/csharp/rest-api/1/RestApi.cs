using Newtonsoft.Json;

using System;
using System.Collections.Generic;
using System.Diagnostics.CodeAnalysis;
using System.Globalization;
using System.Linq;

// ReSharper disable once CheckNamespace
public class RestApi
{
	private readonly UserDao _userManagement;
	private readonly LoanManagement _loanManagement;

	public RestApi(string database)
	{
		_userManagement = new(database);
		_loanManagement = new(_userManagement);
	}

	[SuppressMessage("Design", "CA1054:URI-like parameters should not be strings", Justification = "Required by test.")]
	public string Get(string url, string? payload = null)
		=> url == "/users"
			? JsonConvert.SerializeObject(_userManagement.Get(payload))
			: throw new NotImplementedException($"Endpoint not found: {url}");

	[SuppressMessage("Design", "CA1054:URI-like parameters should not be strings", Justification = "Required by test.")]
	public string Post(string url, string payload)
	{
		switch (url)
		{
			case "/add":
				{
					User newUser = _userManagement.Add(payload);
					return JsonConvert.SerializeObject(newUser);
				}

			case "/iou":
				{
					Loan loan = _loanManagement.CreateLoan(payload);
					List<User> users = _userManagement.Get(loan.Users);
					return JsonConvert.SerializeObject(users);
				}

			default:
				throw new NotImplementedException($"Endpoint not found: {url}");
		}
	}
}

//=======================================================================

// ReSharper disable once CheckNamespace
public class Loan
{
	public required User Lender { get; init; }
	public required User Borrower { get; init; }
	public required int Amount { get; init; }

	public IEnumerable<User> Users
		=> new HashSet<User> { Lender, Borrower };
}

//=======================================================================

// ReSharper disable once CheckNamespace
internal sealed class LoanFactory
{
	private readonly UserDao _userManagement;

	public LoanFactory(UserDao userManagement)
		=> _userManagement = userManagement;

	public Loan Create(string payload)
	{
		Dictionary<string, object> loanRequest
			= JsonConvert.DeserializeObject<Dictionary<string, object>>(payload)
			  ?? throw new ArgumentException("IOU Data must not resolve to null.", nameof(payload));
		string lender = loanRequest["lender"]
							.ToString()
						?? throw new ArgumentException("Lender must not resolve to null.", nameof(payload));
		string borrower = loanRequest["borrower"]
							  .ToString()
						  ?? throw new ArgumentException(
							  "Borrower must not resolve to null.",
							  nameof(payload)
						  );
		return new()
		{
			Lender = _userManagement[lender],
			Borrower = _userManagement[borrower],
			Amount = Convert.ToInt16(loanRequest["amount"], CultureInfo.InvariantCulture),
		};
	}
}

//=======================================================================

// ReSharper disable once CheckNamespace

internal sealed class LoanManagement
{
	private readonly LoanFactory _loanFactory;

	public LoanManagement(UserDao userManagement)
		=> _loanFactory = new(userManagement);

	public Loan CreateLoan(string payload)
	{
		Loan loan = _loanFactory.Create(payload);

		if (loan.Lender.OwesTo.TryGetValue(loan.Borrower.Name, out int _))
		{
			_ = loan.Lender.RepayDebt(loan);
			_ = loan.Borrower.SettleLoan(loan);
		}
		else
		{
			_ = loan.Lender.Loan(loan);
			_ = loan.Borrower.Borrow(loan);
		}

		return loan;
	}
}

//=======================================================================

// ReSharper disable once CheckNamespace
public class User
{
	[JsonProperty("name")]
	public required string Name { get; init; }

	[JsonProperty("owes")]
	[JsonConverter(typeof(SortedDictionaryJsonConverter<string, int>))]
	public Dictionary<string, int> OwesTo { get; init; } = new();

	[JsonProperty("owed_by")]
	[JsonConverter(typeof(SortedDictionaryJsonConverter<string, int>))]
	public Dictionary<string, int> OwedBy { get; init; } = new();

	[JsonProperty("balance")]
	public int Balance
		=> OwedBy.Values.Sum() - OwesTo.Values.Sum();

	public User Loan(Loan loan)
	{
		if (loan is null)
		{
			throw new ArgumentNullException(nameof(loan));
		}

		_ = UpdateDictionary(OwedBy, loan.Borrower.Name, loan.Amount);
		return this;
	}

	public User Borrow(Loan loan)
	{
		if (loan is null)
		{
			throw new ArgumentNullException(nameof(loan));
		}

		_ = UpdateDictionary(OwesTo, loan.Lender.Name, loan.Amount);
		return this;
	}

	private Dictionary<string, int> UpdateDictionary(Dictionary<string, int> dictionary, string name, int amount)
	{
		dictionary[name] = OwesTo.GetValueOrDefault(name, 0) + amount;
		return Clean(dictionary, name);
	}

	internal User RepayDebt(Loan loan)
		=> UpdateBalances(
			OwesTo,
			OwedBy,
			loan.Borrower.Name,
			loan.Amount
		);

	internal User SettleLoan(Loan loan)
		=> UpdateBalances(
			OwedBy,
			OwesTo,
			loan.Lender.Name,
			loan.Amount
		);

	private User UpdateBalances(
		Dictionary<string, int> targetDictionary,
		IDictionary<string, int> otherDictionary,
		string name,
		int amount
	)
	{
		targetDictionary[name] -= amount;
		if (targetDictionary[name] < 0)
		{
			otherDictionary[name] = -targetDictionary[name];
			targetDictionary[name] = 0;
		}

		_ = Clean(targetDictionary, name);
		return this;
	}

	private static Dictionary<string, int> Clean(Dictionary<string, int> dictionary, string name)
	{
		if (dictionary[name] == 0)
		{
			_ = dictionary.Remove(name);
		}

		return dictionary;
	}
}

//=======================================================================

// ReSharper disable once CheckNamespace
[SuppressMessage("Performance", "CA1812:Avoid uninstantiated internal classes", Justification = "False negative.")]
internal sealed class UserInputDto
{
	[JsonProperty("user")]
	public required string Name { get; init; }
}

//=======================================================================

// ReSharper disable once CheckNamespace
internal sealed class UserDao
{
	private readonly Dictionary<string, User> _usersByName;

	public UserDao(string database)
	{
		if (string.IsNullOrWhiteSpace(database))
		{
			throw new ArgumentException($"'{nameof(database)}' cannot be null or whitespace.", nameof(database));
		}

		_usersByName = JsonConvert.DeserializeObject<List<User>>(database)
						   ?.Where(x => !string.IsNullOrWhiteSpace(x.Name))
						   .ToDictionary(x => x.Name, x => x)
					   ?? throw new ArgumentException("Database must not resolve to null.", nameof(database));
	}

	public User this[string key]
		=> _usersByName.TryGetValue(key, out User? value)
			? value
			: throw new ArgumentException($"User named {key} does not exist");

	public List<User> Get(string? payload = null)
	{
		if (payload == null)
		{
			return GetSelectedUsers();
		}

		HashSet<string> requestedUsers
			= JsonConvert.DeserializeObject<Dictionary<string, List<string>>>(payload)
				  ?["users"]
				  .ToHashSet()
			  ?? new();
		return GetSelectedUsers(requestedUsers);
	}

	public List<User> Get(IEnumerable<User> users)
	{
		HashSet<string> userNames = users.Select(x => x.Name)
			.ToHashSet();
		return GetSelectedUsers(userNames);
	}

	private List<User> GetSelectedUsers(IReadOnlySet<string>? requestedUsers = null)
	{
		List<User> users = requestedUsers != null
			? _usersByName.Values
				.Where(user => requestedUsers.Contains(user.Name))
				.ToList()
			: _usersByName.Values.ToList();

		return users.OrderBy(user => user.Name)
			.ToList();
	}

	public User Add(string payload)
	{
		UserInputDto input = JsonConvert.DeserializeObject<UserInputDto>(payload)
							 ?? throw new ArgumentException("Invalid payload.", nameof(payload));
		User newUser = new() { Name = input.Name };
		_usersByName[newUser.Name] = newUser;
		return newUser;
	}
}

//=======================================================================

//=======================================================================

// ReSharper disable once CheckNamespace
public class SortedDictionaryJsonConverter<TKey, TValue> : JsonConverter<Dictionary<TKey, TValue>>
	where TKey : notnull
{
	public override void WriteJson(JsonWriter writer, Dictionary<TKey, TValue>? value, JsonSerializer serializer)
	{
		if (serializer is null)
		{
			throw new ArgumentNullException(nameof(serializer));
		}

		IOrderedEnumerable<KeyValuePair<TKey, TValue>> sortedKeyValuePairs = value!.OrderBy(kv => kv.Key);
		Dictionary<TKey, TValue> sortedDictionary = sortedKeyValuePairs.ToDictionary(kv => kv.Key, kv => kv.Value);
		serializer.Serialize(writer, sortedDictionary);
	}

	public override Dictionary<TKey, TValue>? ReadJson(
		JsonReader reader,
		Type objectType,
		Dictionary<TKey, TValue>? existingValue,
		bool hasExistingValue,
		JsonSerializer serializer
	)
		=> serializer is null
			? throw new ArgumentNullException(nameof(serializer))
			: serializer.Deserialize<Dictionary<TKey, TValue>>(reader);
}

//=======================================================================