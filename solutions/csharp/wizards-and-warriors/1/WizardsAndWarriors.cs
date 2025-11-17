using System;

internal abstract class Character
{
	private readonly string _characterType;

	protected Character(string characterType)
	{
		_characterType = characterType ?? throw new ArgumentNullException(nameof(characterType));
	}

	public abstract int DamagePoints(Character target);

	public virtual bool Vulnerable()
	{
		return false;
	}

	public override string ToString()
	{
		return $"Character is a {_characterType}";
	}
}

internal class Warrior : Character
{
	public Warrior() : base("Warrior")
	{
	}

	public override int DamagePoints(Character target)
	{
		return target.Vulnerable()
			? 10
			: 6;
	}
}

internal class Wizard : Character
{
	public Wizard() : base("Wizard")
	{
	}

	private bool _hasPreparedSpell = false;

	public override bool Vulnerable()
	{
		return !_hasPreparedSpell;
	}

	public override int DamagePoints(Character _target)
	{
		return _hasPreparedSpell
			? 12
			: 3;
	}

	public void PrepareSpell()
	{
		_hasPreparedSpell = true;
	}
}
