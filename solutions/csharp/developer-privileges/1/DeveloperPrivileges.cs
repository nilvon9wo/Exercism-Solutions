using System.Collections.Generic;
using System.Linq;

public class Authenticator
{
	public Identity Admin => new()
	{
		Email = "admin@ex.ism",
		FacialFeatures = new()
		{
			EyeColor = "green",
			PhiltrumWidth = 0.9M
		},
		NameAndAddress = new List<string>()
		{
			"Chanakya",
			"Mumbai",
			"India"
		}
	};

	public IDictionary<string, Identity> Developers =>
		new List<Identity>() {
			new()
			{
				Email = "bert@ex.ism",
				FacialFeatures = new()
				{
					EyeColor = "blue",
					PhiltrumWidth = 0.8M
				},
				NameAndAddress = new List<string>()
				{
					"Bertrand",
					"Paris",
					"France"
				}
			},
			new()
			{
				Email = "anders@ex.ism",
				FacialFeatures = new()
				{
					EyeColor = "brown",
					PhiltrumWidth = 0.85M
				},
				NameAndAddress = new List<string>()
				{
					"Anders",
					"Redmond",
					"USA"
				}
			}
		}
			.ToDictionary(x => x.NameAndAddress[0], x => x);
}

//**** please do not modify the FacialFeatures class ****
public class FacialFeatures
{
	public string EyeColor { get; set; }
	public decimal PhiltrumWidth { get; set; }
}

//**** please do not modify the Identity class ****
public class Identity
{
	public string Email { get; set; }
	public FacialFeatures FacialFeatures { get; set; }
	public IList<string> NameAndAddress { get; set; }
}
