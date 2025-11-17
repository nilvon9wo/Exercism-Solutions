defmodule Frequency do
  @doc """
  Count letter frequency in parallel.

  Returns a map of characters to frequencies.

  The number of worker processes to use can be set with 'workers'.
  """
  @spec frequency([String.t()], pos_integer) :: map
  def frequency([], _),
    do: %{}

  def frequency(texts, worker_count),
    do:
      texts
      |> divide_texts_between_workers(worker_count)
      |> Enum.map(&request_frequencies_async/1)
      |> Enum.map(&get_frequencies/1)
      |> combine_frequencies()

  defp divide_texts_between_workers(texts, worker_count),
    do:
      divide_texts_between_workers(_divided_texts = %{}, texts, worker_count, _next_receiver = 0)

  defp divide_texts_between_workers(divided_texts, [], _worker_count, _next_receiver),
    do: Map.values(divided_texts)

  defp divide_texts_between_workers(divided_texts, texts, worker_count, next_receiver)
       when next_receiver > worker_count,
       do: divide_texts_between_workers(divided_texts, texts, worker_count, _next_receiver = 0)

  defp divide_texts_between_workers(
         divided_texts,
         [first_text | more_texts],
         worker_count,
         next_receiver
       ),
       do:
         divided_texts
         |> Map.update(next_receiver, [first_text], &[first_text | &1])
         |> divide_texts_between_workers(more_texts, worker_count, next_receiver + 1)

  defp request_frequencies_async(texts) do
    caller = self()

    spawn(fn ->
      send(caller, {:result, FrequencyCounter.count(texts)})
    end)
  end

  defp get_frequencies(_pid) do
    receive do
      {:result, result} -> result
    end
  end

  defp combine_frequencies([first_map | [second_map | more_maps]]),
    do: combine_frequencies([Map.merge(first_map, second_map, &add_values/3) | more_maps])

  defp combine_frequencies([only_map]),
    do: only_map

  defp add_values(_key, value1, value2),
    do: value1 + value2
end

defmodule FrequencyCounter do
  def count(texts),
    do:
      texts
      |> Enum.join()
      |> String.replace(~r/[\p{P}\p{S}\d\s]+/, "")
      |> String.downcase()
      |> String.graphemes()
      |> Enum.reduce(%{}, &count_occurrences/2)

  defp count_occurrences(value, accumulator),
    do: Map.update(accumulator, value, 1, &(&1 + 1))
end
