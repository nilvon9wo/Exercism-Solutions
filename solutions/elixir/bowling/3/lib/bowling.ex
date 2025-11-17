defmodule Bowling do
  defstruct [:frame_count, :is_new_frame, :rolls, :bonus_type, :bonus_roll_count]

  @doc """
    Creates a new game of bowling that can be used to store the results of
    the game
  """

  @maximum_pin_count 10

  @spec start() :: any
  def start,
    do: %Bowling{
      frame_count: 0,
      is_new_frame: true,
      rolls: [],
      bonus_type: :none,
      bonus_roll_count: 0
    }

  @doc """
    Records the number of pins knocked down on a single roll. Returns `any`
    unless there is something wrong with the given number of pins, in which
    case it returns a helpful message.
  """

  @spec roll(any, integer) :: any | String.t()
  def roll(%Bowling{} = _game, pins_knocked_down)
      when pins_knocked_down < 0,
      do: {:error, "Negative roll is invalid"}

  def roll(%Bowling{} = _game, pins_knocked_down)
      when pins_knocked_down > @maximum_pin_count,
      do: {:error, "Pin count exceeds pins on the lane"}

  def roll(
        %Bowling{rolls: [last_roll | _], is_new_frame: false, bonus_type: :none} = _game,
        pins_knocked_down
      )
      when last_roll + pins_knocked_down > 10,
      do: {:error, "Pin count exceeds pins on the lane"}

  def roll(
        %Bowling{
          rolls: [last_roll | [10 | _older_rolls]],
          frame_count: 10,
          bonus_type: :strike,
          bonus_roll_count: 1
        } = _game,
        pins_knocked_down
      )
      when last_roll + pins_knocked_down > 10 and
             (last_roll != 10 and pins_knocked_down != 10),
      do: {:error, "Pin count exceeds pins on the lane"}

  def roll(
        %Bowling{
          rolls: [last_roll | [10 | _older_rolls]],
          frame_count: 10,
          bonus_type: :strike,
          bonus_roll_count: 1
        } = _game,
        10 = _pins_knocked_down
      )
      when last_roll != 10,
      do: {:error, "Pin count exceeds pins on the lane"}

  # Strike Bonus Rolls -----------------------------------

  def roll(
        %Bowling{
          rolls: [penultimate_roll | [10 | _older_rolls]] = rolls,
          frame_count: 10,
          bonus_type: :strike,
          bonus_roll_count: 1
        } = game,
        pins_knocked_down
      ),
      do: %{
        game
        | rolls: [pins_knocked_down | rolls],
          is_new_frame: false,
          bonus_type: :strike,
          bonus_roll_count: 2
      }

  def roll(
        %Bowling{
          rolls: [10 | _older_rolls] = rolls,
          frame_count: 10,
          bonus_type: :strike,
          bonus_roll_count: 0
        } = game,
        pins_knocked_down
      ),
      do: %{
        game
        | rolls: [pins_knocked_down | rolls],
          is_new_frame: false,
          bonus_type: :strike,
          bonus_roll_count: 1
      }

  # Spare Bonus Rolls -----------------------------------

  def roll(
        %Bowling{rolls: [last_roll | [penultimate_roll | _older_rolls]] = rolls, frame_count: 10} =
          game,
        pins_knocked_down
      )
      when last_roll + penultimate_roll == 10,
      do: %{
        game
        | rolls: [pins_knocked_down | rolls],
          is_new_frame: false,
          bonus_type: :spare,
          bonus_roll_count: 1
      }

  # Normal Rolls -----------------------------------

  def roll(%Bowling{frame_count: 10} = _game, _pins_knocked_down),
    do: {:error, "Cannot roll after game is over"}

  def roll(
        %Bowling{rolls: rolls, frame_count: frame_count, is_new_frame: true} = game,
        10 = pins_knocked_down
      ),
      do: %{
        game
        | rolls: [pins_knocked_down | rolls],
          frame_count: frame_count + 1,
          is_new_frame: true,
          bonus_type: :strike
      }

  def roll(
        %Bowling{
          rolls: [last_roll | _other_rolls] = rolls,
          frame_count: frame_count,
          is_new_frame: false
        } = game,
        pins_knocked_down
      )
      when last_roll + pins_knocked_down == 10,
      do: %{
        game
        | rolls: [pins_knocked_down | rolls],
          frame_count: frame_count + 1,
          is_new_frame: true,
          bonus_type: :spare
      }

  def roll(
        %Bowling{rolls: rolls, frame_count: frame_count, is_new_frame: false} = game,
        pins_knocked_down
      ),
      do: %{
        game
        | rolls: [pins_knocked_down | rolls],
          frame_count: frame_count + 1,
          is_new_frame: true,
          bonus_type: :none
      }

  def roll(
        %Bowling{rolls: rolls, frame_count: frame_count, is_new_frame: true} = game,
        pins_knocked_down
      ),
      do: %{
        game
        | rolls: [pins_knocked_down | rolls],
          frame_count: frame_count,
          is_new_frame: false,
          bonus_type: :none
      }

  @doc """
    Returns the running_total of a given game of bowling if the game is complete.
    If the game isn't complete, it returns a helpful message.
  """

  @spec score(any) :: integer | String.t()
  def score(%Bowling{frame_count: frame_count})
      when frame_count < 10,
      do: {:error, "Score cannot be taken until the end of the game"}

  def score(%Bowling{
        bonus_type: :strike,
        bonus_roll_count: bonus_roll_count
      })
      when bonus_roll_count != 2,
      do: {:error, "Score cannot be taken until the end of the game"}

  def score(%Bowling{
        rolls: [last_roll | [penultimate_roll | [10 | _]]] = rolls,
        bonus_type: :strike,
        bonus_roll_count: 2
      }),
      do: score(%Bowling{rolls: rolls, bonus_type: :none}) - last_roll - penultimate_roll

  def score(%Bowling{
        bonus_type: :spare,
        bonus_roll_count: bonus_roll_count
      })
      when bonus_roll_count != 1,
      do: {:error, "Score cannot be taken until the end of the game"}

  def score(%Bowling{rolls: [last_roll | _] = rolls, bonus_type: :spare, bonus_roll_count: 1}),
    do: score(%Bowling{rolls: rolls, bonus_type: :none}) - last_roll

  def score(%Bowling{rolls: rolls, bonus_type: :none}),
    do: score(Enum.reverse(rolls), _accumulator = 0)

  defp score([] = _rolls, accumulator),
    do: accumulator

  defp score([10 = head_roll | [next_throw_1 | [next_throw_2 | _]] = tail_rolls], accumulator),
    do: score(tail_rolls, accumulator + head_roll + next_throw_1 + next_throw_2)

  defp score([head_roll | [next_throw_1 | [next_throw_2 | _] = tail_rolls]], accumulator)
       when head_roll + next_throw_1 == 10,
       do: score(tail_rolls, accumulator + head_roll + next_throw_1 + next_throw_2)

  defp score([head_roll | [next_throw_1 | tail_rolls]], accumulator),
    do: score(tail_rolls, accumulator + head_roll + next_throw_1)

  defp score([head_roll | tail_rolls], accumulator),
    do: score(tail_rolls, accumulator + head_roll)
end
