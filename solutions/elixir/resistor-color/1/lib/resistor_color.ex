defmodule ResistorColor do
  @moduledoc false

  @spec colors() :: list(String.t())
  def colors,
    do: [
      "black",
      "brown",
      "red",
      "orange",
      "yellow",
      "green",
      "blue",
      "violet",
      "grey",
      "white"
    ]

  @spec code(String.t()) :: integer()
  def code(color),
    do: Enum.find_index(colors, &(&1 === color))
end
