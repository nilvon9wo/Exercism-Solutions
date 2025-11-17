defmodule Diamond do
  @doc """
  Given a letter, it prints a diamond starting with 'A',
  with the supplied letter at the widest point.
  """
  @spec build_shape(char) :: String.t()
  def build_shape(letter) do
    diamond =
      List.flatten([
        make_top_rows(letter),
        make_bottom_rows(letter)
      ])
      |> Enum.join("\n")

    diamond <> "\n"
  end

  defp make_top_rows(last_letter)
       when last_letter === ?A,
       do: ["A"]

  defp make_top_rows(last_letter),
    do:
      ?A..last_letter
      |> Enum.map(&make_row(&1, last_letter))

  defp make_bottom_rows(last_letter)
       when last_letter === ?A,
       do: []

  defp make_bottom_rows(last_letter),
    do:
      ?A..(last_letter - 1)
      |> Enum.reverse()
      |> Enum.map(&make_row(&1, last_letter))

  defp make_row(current_letter, last_letter) do
    Enum.join(
      create_row(
        create_outer_spaces(last_letter - current_letter),
        current_letter,
        create_inner_spaces(current_letter - ?A)
      )
    )
  end

  defp create_outer_spaces(distance_from_last_letter)
       when distance_from_last_letter === 0,
       do: ""

  defp create_outer_spaces(distance_from_last_letter),
    do: create_spaces(1..distance_from_last_letter)

  defp create_inner_spaces(distance_from_a)
       when distance_from_a === 0,
       do: ""

  defp create_inner_spaces(distance_from_a)
       when distance_from_a === 1,
       do: " "

  defp create_inner_spaces(distance_from_a),
    do: create_spaces(1..(distance_from_a * 2 - 1))

  defp create_spaces(spaces_needed),
    do: Enum.map(spaces_needed, &to_space/1)

  defp to_space(_count),
    do: " "

  defp create_row(outer_spaces, current_letter, _inner_spaces)
       when current_letter === ?A,
       do: [outer_spaces, stringify(current_letter), outer_spaces]

  defp create_row(outer_spaces, current_letter, inner_spaces),
    do: [
      outer_spaces,
      stringify(current_letter),
      inner_spaces,
      stringify(current_letter),
      outer_spaces
    ]

  defp stringify(ascii_code),
    do: List.to_string([ascii_code])
end
