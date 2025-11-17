defmodule Line do
  defstruct [:file_name, :text, :line_number]

  def new({text, index}, file_name, line_count) do
    text =
      if index + 1 != line_count,
        do: text <> "\n",
        else: text

    %Line{
      file_name: file_name,
      text: text,
      line_number: index + 1
    }
  end
end

defmodule Grep do
  @case_insensitive "-i"
  @inverted "-v"
  @match_entire_line "-x"
  @print_file_names "-l"
  @print_line_numbers "-n"

  @spec grep(String.t(), [String.t()], [String.t()]) :: String.t()
  def grep(pattern, flags, files),
    do:
      Enum.map(files, &read_file/1)
      |> Enum.map(&split_lines/1)
      |> List.flatten()
      |> Enum.filter(&has_pattern(pattern, flags, &1))
      |> Enum.reject(&(&1.text == ""))
      |> format(flags, files)

  defp read_file(file_name) do
    {:ok, file} = File.read(file_name)
    {file_name, file}
  end

  defp split_lines({file_name, file_text}) do
    lines = String.split(file_text, "\n")

    lines
    |> Enum.with_index()
    |> Enum.map(&Line.new(&1, file_name, length(lines)))
  end

  defp has_pattern(pattern, flags, %Line{text: text}),
    do: has_pattern(pattern, flags, text)

  defp has_pattern(pattern, flags, text) do
    result =
      cond do
        Enum.member?(flags, @case_insensitive) ->
          has_pattern(
            String.downcase(pattern),
            Enum.reject(flags, &(&1 == @case_insensitive)),
            String.downcase(text)
          )

        Enum.member?(flags, @match_entire_line) ->
          text == pattern <> "\n"

        true ->
          String.contains?(text, pattern)
      end

    if Enum.member?(flags, @inverted),
      do: !result,
      else: result
  end

  defp format([] = _lines, _flags, _files),
    do: ""

  defp format([_head | _tail] = lines, flags, files),
    do:
      lines
      |> Enum.map(&format(&1, flags, files))
      |> Enum.dedup()
      |> Enum.join()

  defp format([%Line{} = line], flags, files),
    do: format(line, flags, files)

  defp format(
         %Line{
           file_name: file_name,
           text: text,
           line_number: line_number
         },
         flags,
         files
       ) do
    file_name_display =
      if length(files) > 1,
        do: file_name <> ":",
        else: ""

    line_numbers_display =
      if Enum.member?(flags, @print_line_numbers),
        do: to_string(line_number) <> ":",
        else: ""

    if Enum.member?(flags, @print_file_names),
      do: file_name <> "\n",
      else: file_name_display <> line_numbers_display <> text
  end
end
