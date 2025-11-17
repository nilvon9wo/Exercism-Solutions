defmodule ScaleGenerator do
  @one_semitone "m"
  @full_tone "M"
  @augmented_second "A"
  @steps_by_semitones %{
    @one_semitone => 1,
    @full_tone => 2,
    @augmented_second => 3
  }

  @flats_by_sharps %{
    "A" => "A",
    "A#" => "Bb",
    "B" => "B",
    "C" => "C",
    "C#" => "Db",
    "D" => "D",
    "D#" => "Eb",
    "E" => "E",
    "F" => "F",
    "F#" => "Gb",
    "G" => "G",
    "G#" => "Ab"
  }
  @sharps_by_flats for {sharp, flat} <- @flats_by_sharps,
                       into: %{},
                       do: {flat, sharp}
  @pitches_expressed_with_sharps Map.keys(@flats_by_sharps)
  @pitches_expressed_with_flats Enum.map(@pitches_expressed_with_sharps, &@flats_by_sharps[&1])
  @pitches_in_chromatic_scale length(@pitches_expressed_with_sharps) + 1
  @requires_flat_scale ~w(F Bb Eb Ab Db Gb d g c f bb eb)

  @doc """
  Find the note for a given interval (`step`) in a `scale` after the `tonic`.

  "m": one semitone
  "M": two semitones (full tone)
  "A": augmented second (three semitones)

  Given the `tonic` "D" in the `scale` (C C# D D# E F F# G G# A A# B C), you
  should return the following notes for the given `step`:

  "m": D#
  "M": E
  "A": F
  """
  @spec step(scale :: list(String.t()), tonic :: String.t(), step :: String.t()) ::
          list(String.t())
  def step(scale, tonic, step) do
    fixed_tonic = fix_case(tonic)
    index_of_tonic = Enum.find_index(scale, &(&1 == fixed_tonic))
    target_index = calculate_target_index(index_of_tonic, step)
    {target_note, _} = List.pop_at(scale, target_index)
    target_note
  end

  defp calculate_target_index(index_of_tonic, step) do
    step_size = @steps_by_semitones[step]

    if index_of_tonic + step_size < @pitches_in_chromatic_scale - 1,
      do: index_of_tonic + step_size,
      else: index_of_tonic + step_size - @pitches_in_chromatic_scale + 1
  end

  @doc """
  The chromatic scale is a musical scale with thirteen pitches, each a semitone
  (half-tone) above or below another.

  Notes with a sharp (#) are a semitone higher than the note below them, where
  the next letter note is a full tone except in the case of B and E, which have
  no sharps.

  Generate these notes, starting with the given `tonic` and wrapping back
  around to the note before it, ending with the tonic an octave higher than the
  original. If the `tonic` is lowercase, capitalize it.

  "C" should generate: ~w(C C# D D# E F F# G G# A A# B C)
  """
  @spec chromatic_scale(tonic :: String.t()) :: list(String.t())
  def chromatic_scale(tonic \\ "C") do
    uppercase_tonic = String.upcase(tonic)

    @pitches_expressed_with_sharps
    |> Enum.find_index(&(&1 == uppercase_tonic))
    |> chromatic_scale(_accumulator = [uppercase_tonic])
  end

  def chromatic_scale(_index_of_tonic, accumulator)
      when length(accumulator) === @pitches_in_chromatic_scale,
      do: finish_scale(accumulator)

  def chromatic_scale(index_of_tonic, accumulator)
      when index_of_tonic > @pitches_in_chromatic_scale,
      do: chromatic_scale(0, [hd(@pitches_expressed_with_sharps) | accumulator])

  def chromatic_scale(index_of_tonic, accumulator),
    do:
      chromatic_scale(
        index_of_tonic,
        [
          step(@pitches_expressed_with_sharps, hd(accumulator), @one_semitone) | accumulator
        ]
      )

  @doc """
  Sharp notes can also be considered the flat (b) note of the tone above them,
  so the notes can also be represented as:

  A Bb B C Db D Eb E F Gb G Ab

  Generate these notes, starting with the given `tonic` and wrapping back
  around to the note before it, ending with the tonic an octave higher than the
  original. If the `tonic` is lowercase, capitalize it.

  "C" should generate: ~w(C Db D Eb E F Gb G Ab A Bb B C)
  """
  @spec flat_chromatic_scale(tonic :: String.t()) :: list(String.t())
  def flat_chromatic_scale(tonic \\ "C"),
    do:
      @sharps_by_flats[fix_case(tonic)]
      |> chromatic_scale()
      |> Enum.map(&@flats_by_sharps[&1])

  defp fix_case(tonic) do
    fixed_case = String.upcase(tonic)

    if String.length(tonic) == 2,
      do: String.replace_suffix(fixed_case, "B", "b"),
      else: fixed_case
  end

  @doc """
  Certain scales will require the use of the flat version, depending on the
  `tonic` (key) that begins them, which is C in the above examples.

    For any of the following tonics, use the flat chromatic scale:

    F Bb Eb Ab Db Gb d g c f bb eb

  For all others, use the regular chromatic scale.
  """
  @spec find_chromatic_scale(tonic :: String.t()) :: list(String.t())
  def find_chromatic_scale(tonic) do
    if requires_flat_scale(tonic),
      do: flat_chromatic_scale(tonic),
      else: chromatic_scale(tonic)
  end

  @doc """
  The `pattern` string will let you know how many steps to make for the next
  note in the scale.

  For example, a C Major scale will receive the pattern "MMmMMMm", which
  indicates you will start with C, make a full step over C# to D, another over
  D# to E, then a semitone, stepping from E to F (again, E has no sharp). You
  can follow the rest of the pattern to get:

  C D E F G A B C
  """
  @spec scale(tonic :: String.t(), pattern :: String.t()) :: list(String.t())
  def scale(tonic, pattern) do
    steps = String.graphemes(pattern)
    fixed_tonic = fix_case(tonic)
    accumulator = [fixed_tonic]

    if requires_flat_scale(tonic),
      do: scale(@pitches_expressed_with_flats, fixed_tonic, steps, accumulator),
      else: scale(@pitches_expressed_with_sharps, fixed_tonic, steps, accumulator)
  end

  defp scale(_scale, _tonic, [] = _pattern, accumulator),
    do: Enum.reverse(accumulator)

  defp scale(scale, tonic, [pattern_head | pattern_tail], accumulator) do
    step = step(scale, tonic, pattern_head)
    scale(scale, step, pattern_tail, [step | accumulator])
  end

  defp finish_scale(accumulator),
    do:
      accumulator
      |> Enum.reverse()
      |> Enum.reject(&(&1 === nil))

  defp requires_flat_scale(tonic),
    do:
      Enum.member?(@requires_flat_scale, tonic) or
        (String.length(tonic) === 2 and
           String.last(tonic) === "b")
end
