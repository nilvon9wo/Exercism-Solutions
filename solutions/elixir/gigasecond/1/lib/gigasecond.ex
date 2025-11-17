defmodule Gigasecond do
  @doc """
  Calculate a date one billion seconds after an input date.
  """
  @spec from({{pos_integer, pos_integer, pos_integer}, {pos_integer, pos_integer, pos_integer}}) ::
          :calendar.datetime()

  @gigasecond 1_000_000_000
  def from({{year, month, day}, {hours, minutes, seconds}}),
    do:
      from(
        "#{pad_with_zeros(year, 4)}" <>
          "-#{pad_with_zeros(month)}" <>
          "-#{pad_with_zeros(day)}" <>
          "T#{pad_with_zeros(hours)}" <>
          ":#{pad_with_zeros(minutes)}" <>
          ":#{pad_with_zeros(seconds)}Z"
      )

  def from(iso_8601)
      when is_binary(iso_8601),
      do: from(DateTime.from_iso8601(iso_8601))

  def from({:ok, datetime, _}),
    do:
      DateTime.add(datetime, @gigasecond)
      |> to_tuples()

  defp pad_with_zeros(number, width \\ 2),
    do:
      number
      |> Integer.to_string()
      |> String.pad_leading(width, "0")

  defp to_tuples(%DateTime{
         year: year,
         month: month,
         day: day,
         hour: hour,
         minute: minute,
         second: second
       }),
       do: {{year, month, day}, {hour, minute, second}}
end
