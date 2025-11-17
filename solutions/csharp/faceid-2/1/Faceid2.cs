using System.Collections.Generic;

public class FacialFeatures
{
	public string EyeColor { get; }
	public decimal PhiltrumWidth { get; }

	public FacialFeatures(string eyeColor, decimal philtrumWidth)
	{
		EyeColor = eyeColor;
		PhiltrumWidth = philtrumWidth;
	}

	public override bool Equals(object other)
	{
		return other != null
			&& other is FacialFeatures that
			&& that.EyeColor == EyeColor
			&& that.PhiltrumWidth == PhiltrumWidth;
	}

	public static bool operator ==(FacialFeatures facialFeatures1, FacialFeatures facialFeatures2)
	{
		return Equals(facialFeatures1, facialFeatures2);
	}

	public static bool operator !=(FacialFeatures facialFeatures1, FacialFeatures facialFeatures2)
	{
		return !Equals(facialFeatures1, facialFeatures2);
	}

	public override int GetHashCode()
	{
		return (EyeColor.GetHashCode() * 17)
			+ PhiltrumWidth.GetHashCode();
	}
}

public class Identity
{
	public string Email { get; }
	public FacialFeatures FacialFeatures { get; }

	public Identity(string email, FacialFeatures facialFeatures)
	{
		Email = email;
		FacialFeatures = facialFeatures;
	}

	public override bool Equals(object other)
	{
		return other != null
			&& other is Identity that
			&& that.Email == Email
			&& that.FacialFeatures == FacialFeatures;
	}

	public static bool operator ==(Identity identity1, Identity identity2)
	{
		return Equals(identity1, identity2);
	}

	public static bool operator !=(Identity identity1, Identity identity2)
	{
		return !Equals(identity1, identity2);
	}

	public override int GetHashCode()
	{
		return (Email.GetHashCode() * 17)
			+ FacialFeatures.GetHashCode();
	}
}

public class Authenticator
{
	private static readonly Identity _adminIdentity
		= new("admin@exerc.ism", new("green", 0.9m));

	private readonly Dictionary<int, Identity> _identityByHashCode = new()
	{
		{ _adminIdentity.GetHashCode(), _adminIdentity }
	};

	public static bool AreSameFace(FacialFeatures faceA, FacialFeatures faceB)
	{
		return faceA.Equals(faceB);
	}

	public bool IsAdmin(Identity identity)
	{
		return _adminIdentity == identity;
	}

	public bool Register(Identity identity)
	{
		if (!IsRegistered(identity))
		{
			_identityByHashCode[identity.GetHashCode()] = identity;
			return true;
		}
		else
		{
			return false;
		}
	}

	public bool IsRegistered(Identity identity)
	{
		return _identityByHashCode.TryGetValue(identity.GetHashCode(), out Identity foundIdentity)
			&& identity == foundIdentity;
	}

	public static bool AreSameObject(Identity identityA, Identity identityB)
	{
		return ReferenceEquals(identityA, identityB);
	}
}
