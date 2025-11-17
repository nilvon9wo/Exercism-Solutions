defmodule RailFenceCipher do
  alias Direction
  alias Post

  @doc """
  Encode a given plaintext to the corresponding rail fence ciphertext
  """
  @spec encode(String.t(), pos_integer) :: String.t()
  def encode(message, rails),
    do:
      encode(
        String.graphemes(message),
        rails,
        _current_direction = :down,
        _current_rail = 1,
        _old_encrypted_map = %{}
      )

  def encode(
        [] = _message,
        _rails,
        _current_direction,
        _current_rail,
        old_encrypted_map
      ),
      do: to_encrypted_string(old_encrypted_map)

  def encode(
        [first | rest] = _message,
        rails,
        current_direction,
        current_rail,
        old_encrypted_map
      ) do
    new_encrypted_map = Map.update(old_encrypted_map, current_rail, first, &(&1 <> first))

    {next_direction, next_rail} =
      Direction.decide_whats_next(current_direction, current_rail, rails)

    encode(rest, rails, next_direction, next_rail, new_encrypted_map)
  end

  defp to_encrypted_string(old_encrypted_map, old_encrypted_message \\ "") do
    if Enum.empty?(old_encrypted_map) do
      old_encrypted_message
    else
      rail =
        old_encrypted_map
        |> Map.keys()
        |> Enum.min()

      new_encrypted_message = old_encrypted_message <> old_encrypted_map[rail]
      new_encrypted_map = Map.drop(old_encrypted_map, [rail])
      to_encrypted_string(new_encrypted_map, new_encrypted_message)
    end
  end

  @doc """
  Decode a given rail fence ciphertext to the corresponding plaintext
  """
  @spec decode(String.t(), pos_integer) :: String.t()
  def decode(message, rails),
    do:
      message
      |> Post.from(rails)
      |> Enum.sort_by(& &1.sort_order)
      |> Enum.map(& &1.character)
      |> Enum.join()
end

defmodule Direction do
  def decide_whats_next(:up, current_rail = rails, rails),
    do: {:down, current_rail - 1}

  def decide_whats_next(:up, current_rail, _rails),
    do: {:up, current_rail + 1}

  def decide_whats_next(:down, current_rail = 1, _rails),
    do: {:up, current_rail + 1}

  def decide_whats_next(:down, current_rail, _rails),
    do: {:down, current_rail - 1}
end

defmodule Post do
  alias Direction
  @enforce_keys [:height, :sort_order]
  defstruct [:height, :sort_order, character: ""]

  def from(message, rails),
    do:
      message
      |> create_empty_posts(rails)
      |> Enum.sort(&by_height_before_order/2)
      |> Enum.zip(String.graphemes(message))
      |> Enum.map(&put_character_on_post/1)

  defp by_height_before_order(post1, post2) do
    if post1.height != post2.height,
      do: post1.height < post2.height,
      else: post1.sort_order < post2.sort_order
  end

  defp create_empty_posts(message, rails),
    do:
      1..String.length(message)
      |> Enum.map(& &1)
      |> create_empty_posts(
        rails,
        _current_direction = :down,
        _current_rail = 1,
        _accumulator = []
      )

  defp create_empty_posts(
         [] = _open_positions,
         _rails,
         _current_direction,
         _current_rail,
         accumulator
       ),
       do: accumulator

  defp create_empty_posts(
         [current_position | remaining_positions] = _open_positions,
         rails,
         current_direction,
         current_rail,
         accumulator
       ) do
    {next_direction, next_rail} =
      Direction.decide_whats_next(current_direction, current_rail, rails)

    empty_post = %Post{
      height: current_rail,
      sort_order: current_position
    }

    create_empty_posts(
      remaining_positions,
      rails,
      next_direction,
      next_rail,
      [empty_post | accumulator]
    )
  end

  defp put_character_on_post({empty_post, character}),
    do: %Post{empty_post | character: character}
end
