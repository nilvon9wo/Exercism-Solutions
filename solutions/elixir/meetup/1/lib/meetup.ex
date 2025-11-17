defmodule Meetup do
  @moduledoc """
  Calculate meetup dates.
  """
  @type weekday ::
          :monday
          | :tuesday
          | :wednesday
          | :thursday
          | :friday
          | :saturday
          | :sunday

  @weekdays [:monday, :tuesday, :wednesday, :thursday, :friday, :saturday, :sunday]

  @type schedule :: :first | :second | :third | :fourth | :last | :teenth
  @ordinal [:first, :second, :third, :fourth]

  @doc """
  Calculate a meetup date.

  The schedule is in which week (1..4, last or "teenth") the meetup date should
  fall.
  """
  @spec meetup(pos_integer, pos_integer, weekday, schedule) :: :calendar.date()
  def meetup(year, month, target_weekday, :teenth = _schedule),
    do:
      find_day(
        year,
        month,
        target_weekday,
        _base_day = 13,
        _base_offset = & &1
      )

  def meetup(year, target_month, target_weekday, :last = _schedule) do
    fourth_target_day = meetup(year, target_month, target_weekday, :fourth)
    candidate_date = Date.add(fourth_target_day, 7)
    {_candidate_year, candidate_month, _candidate_day} = Date.to_erl(candidate_date)

    if target_month == candidate_month,
      do: candidate_date,
      else: fourth_target_day
  end

  def meetup(year, month, target_weekday, schedule),
    do:
      find_day(
        year,
        month,
        target_weekday,
        _base_day = 1,
        _base_offset = &find_by_indexed_ordinal(schedule, &1)
      )

  defp find_by_indexed_ordinal(schedule, day_difference),
    do: 7 * Enum.find_index(@ordinal, &(&1 == schedule)) + day_difference

  def find_day(year, month, target_weekday, base_day, base_offset) do
    {:ok, nth_of_month} = Date.new(year, month, base_day)
    nth_day_number = Date.day_of_week(nth_of_month)
    target_day_number = to_day_number(target_weekday)

    day_difference = target_day_number - nth_day_number

    candidate_day =
      Date.add(
        nth_of_month,
        base_offset.(day_difference)
      )

    if nth_day_number <= target_day_number,
      do: candidate_day,
      else: Date.add(candidate_day, 7)
  end

  defp to_day_number(day_atom),
    do: 1 + Enum.find_index(@weekdays, &(&1 == day_atom))
end
