defmodule Robot do
  @enforce_keys [:direction, :position]
  defstruct [:direction, :position]

  @clockwise %{
    north: :east,
    east: :south,
    south: :west,
    west: :north
  }
  @valid_directions Map.keys(@clockwise)

  @counterclockwise @clockwise
                    |> Enum.map(fn {key, value} ->
                      {value, key}
                    end)
                    |> Map.new()

  @right "R"
  @left "L"
  @advance "A"
  @valid_instructions [@right, @left, @advance]

  def new(direction \\ :north),
    do: new(direction, _position = {0, 0})

  def new(direction, {x, y} = position)
      when is_integer(x) and
             is_integer(y) do
    if Enum.member?(@valid_directions, direction),
      do: %Robot{direction: direction, position: position},
      else: {:error, "invalid direction"}
  end

  def new(_, _position) do
    {:error, "invalid position"}
  end

  def move(%Robot{} = robot, instructions) do
    if valid_instructions?(instructions),
      do: do_move(robot, instructions),
      else: {:error, "invalid instruction"}
  end

  defp valid_instructions?(instructions),
    do: Enum.all?(instructions, &Enum.member?(@valid_instructions, &1))

  def do_move(%Robot{} = robot, instructions)
      when is_list(instructions),
      do: Enum.reduce(instructions, robot, &do_move(&1, &2))

  def do_move(@right, %Robot{} = robot),
    do: turn_right(robot)

  def do_move(@left, %Robot{} = robot),
    do: turn_left(robot)

  def do_move(@advance, %Robot{} = robot),
    do: advance(robot)

  defp turn_right(%Robot{direction: direction} = robot),
    do: %{robot | direction: Map.get(@clockwise, direction)}

  defp turn_left(%Robot{direction: direction} = robot),
    do: %{robot | direction: Map.get(@counterclockwise, direction)}

  defp advance(%Robot{direction: :north, position: {x, y}} = robot),
    do: %{robot | position: {x, y + 1}}

  defp advance(%Robot{direction: :east, position: {x, y}} = robot),
    do: %{robot | position: {x + 1, y}}

  defp advance(%Robot{direction: :south, position: {x, y}} = robot),
    do: %{robot | position: {x, y - 1}}

  defp advance(%Robot{direction: :west, position: {x, y}} = robot),
    do: %{robot | position: {x - 1, y}}
end

defmodule RobotSimulator do
  @doc """
  Create a Robot Simulator given an initial direction and position.

  Valid directions are: `:north`, `:east`, `:south`, `:west`
  """

  alias Robot

  @spec create(direction :: atom, position :: {integer, integer}) :: any
  def create(direction \\ :north, position \\ {0, 0}),
    do: Robot.new(direction, position)

  @doc """
  Simulate the robot's movement given a string of instructions.

  Valid instructions are: "R" (turn right), "L", (turn left), and "A" (advance)
  """
  @spec simulate(robot :: any, instructions :: String.t()) :: any
  def simulate(%Robot{} = robot, instructions)
      when is_binary(instructions),
      do: Robot.move(robot, String.graphemes(instructions))

  @doc """
  Return the robot's direction.

  Valid directions are: `:north`, `:east`, `:south`, `:west`
  """
  @spec direction(robot :: any) :: atom
  def direction(%Robot{} = robot),
    do: robot.direction

  @doc """
  Return the robot's position.
  """
  @spec position(robot :: any) :: {integer, integer}
  def position(%Robot{} = robot),
    do: robot.position
end
