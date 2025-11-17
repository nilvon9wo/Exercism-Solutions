using System;
using System.Collections.Generic;
using System.Diagnostics.CodeAnalysis;

public class SecurityPassMaker
{
	private readonly HashSet<Type> _specificSecurityTypes = new()
	{
		typeof(SecurityJunior),
		typeof(SecurityIntern),
		typeof(PoliceLiaison),
	};

	public string GetDisplayName(TeamSupport support) =>
		support switch
		{
			Manager _ => "Too Important for a Security Pass",
			Security security => _specificSecurityTypes.Contains(security.GetType())
					? security.Title
					: "Security Team Member Priority Personnel",
			_ => support.Title,
		};
}

/**** Please do not alter the code below ****/

[SuppressMessage("Style", "IDE1006:Naming Styles", Justification = "Legacy code")]
public interface TeamSupport { string Title { get; } }

public abstract class Staff : TeamSupport { public abstract string Title { get; } }

public class Manager : TeamSupport { public string Title { get; } = "The Manager"; }

public class Chairman : TeamSupport { public string Title { get; } = "The Chairman"; }

public class Physio : Staff { public override string Title { get; } = "The Physio"; }

public class OffensiveCoach : Staff { public override string Title { get; } = "Offensive Coach"; }

public class GoalKeepingCoach : Staff { public override string Title { get; } = "Goal Keeping Coach"; }

public class Security : Staff { public override string Title { get; } = "Security Team Member"; }

public class SecurityJunior : Security { public override string Title { get; } = "Security Junior"; }

public class SecurityIntern : Security { public override string Title { get; } = "Security Intern"; }

public class PoliceLiaison : Security { public override string Title { get; } = "Police Liaison Officer"; }
