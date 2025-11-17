defmodule Clock do
  defstruct hour: 0, minute: 0

  @doc """
  Returns a clock that can be represented as a string:

      iex> Clock.new(8, 9) |> to_string
      "08:09"
  """
  @spec new(integer, integer) :: Clock
  def new(hour, minute)
      when hour < 0,
      do: new(24 + rem(hour, 24), minute)

  def new(hour, minute)
      when minute < 0 do
    negative_hours = div(minute, 60)
    positive_minutes = 60 + rem(minute, 60)
    new(hour + negative_hours - 1, positive_minutes)
  end

  def new(hour, minute),
    do: %Clock{
      hour: rem(hour + div(minute, 60), 24),
      minute: rem(minute, 60)
    }

  @doc """
  Adds two clock times:

      iex> Clock.new(10, 0) |> Clock.add(3) |> to_string
      "10:03"
  """
  @spec add(Clock, integer) :: Clock
  def add(%Clock{hour: hour, minute: minute}, add_minute),
    do: new(hour, minute + add_minute)

  defimpl String.Chars, for: Clock do
    def to_string(%Clock{hour: hour, minute: minute}),
      do: "#{stringify(hour)}:#{stringify(minute)}"

    def to_string(%Clock{}),
      do: ""

    defp stringify(value),
      do:
        value
        |> Integer.to_string()
        |> String.pad_leading(2, "0")
  end
end
