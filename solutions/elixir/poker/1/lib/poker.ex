defmodule Suits do
  @suit_by_character %{
    "C" => :club,
    "D" => :diamond,
    "H" => :heart,
    "S" => :spade
  }

  def from_code(card_code),
    do: Map.fetch!(@suit_by_character, String.last(card_code))
end

defmodule Ranks do
  # In this context, it is sufficient to assume the Ace is high
  @value_by_character %{
    "J" => 11,
    "Q" => 12,
    "K" => 13,
    "A" => 14
  }

  def from_code(card_code),
    do:
      card_code
      |> String.split_at(-1)
      |> elem(0)

  def is_higher_first(nil = _rank_1, nil = _rank_2),
    do: false

  def is_higher_first(_rank_1, nil = _rank_2),
    do: true

  def is_higher_first(nil = _rank_1, _rank_2),
    do: false

  def is_higher_first(rank_1, rank_2),
    do: to_value(rank_1) > to_value(rank_2)

  def to_value(rank) do
    value = Integer.parse(rank)

    if :error == value,
      do: Map.fetch(@value_by_character, rank),
      else: value
  end

  def next_rank_down("A"),
    do: "K"

  def next_rank_down("K"),
    do: "Q"

  def next_rank_down("Q"),
    do: "J"

  def next_rank_down("J"),
    do: "10"

  def next_rank_down("2"),
    do: "A"

  def next_rank_down(rank),
    do: to_string(elem(to_value(rank), 0) - 1)
end

defmodule Card do
  defstruct [:rank, :suit]

  def from_code(card_code),
    do: %Card{
      rank: Ranks.from_code(card_code),
      suit: Suits.from_code(card_code)
    }
end

defmodule Hand do
  defstruct [
    :codes,
    :cards,
    :rank_grouping,
    :suit_grouping,
    :has_straight,
    :has_flush,
    :has_full_house_or_better
  ]

  def from_code(hand_code),
    do:
      %Hand{
        codes: hand_code,
        cards:
          hand_code
          |> Enum.map(&Card.from_code/1)
          |> Enum.sort(&Hand.is_higher_first/2)
      }
      |> add_groupings()
      |> add_flags()

  def is_higher_first(%Card{rank: rank_1}, %Card{rank: rank_2}),
    do: Ranks.is_higher_first(rank_1, rank_2)

  defp add_groupings(%Hand{cards: cards} = hand),
    do: %Hand{
      hand
      | rank_grouping: create_groups(cards, :rank),
        suit_grouping: create_groups(cards, :suit)
    }

  defp create_groups(cards, symbol) do
    groups = Enum.group_by(cards, &Map.fetch!(&1, symbol))
    :maps.filter(&has_multiple_members/2, groups)
  end

  defp has_multiple_members(_key, values),
    do: length(values) >= 2

  defp add_flags(%Hand{} = hand),
    do: %Hand{
      hand
      | has_straight: has_straight(hand),
        has_flush: has_flush(hand),
        has_full_house_or_better: has_full_house_or_better(hand)
    }

  def member?(%Hand{cards: cards}, rank),
    do:
      cards
      |> Enum.map(& &1.rank)
      |> Enum.member?(rank)

  def drop_first_card(%Hand{cards: [_first_card | other_cards]} = hand),
    do: %{hand | cards: other_cards}

  defp has_straight(%Hand{cards: [%Card{rank: highest_rank} | other_cards] = cards} = hand),
    do:
      has_high_straight(cards, Ranks.next_rank_down(highest_rank)) ||
        is_low_scoring_straight(hand)

  defp has_high_straight(cards, _next_expected_value)
       when length(cards) == 1,
       do: true

  defp has_high_straight([%Card{rank: highest_rank} | other_cards], next_expected_value) do
    highest_rank

    if Enum.any?(other_cards, &(&1.rank == next_expected_value)),
      do: has_high_straight(other_cards, Ranks.next_rank_down(next_expected_value)),
      else: false
  end

  def is_low_scoring_straight(%Hand{cards: cards}),
    do:
      Enum.any?(cards, &(&1.rank == "5")) &&
        Enum.any?(cards, &(&1.rank == "4")) &&
        Enum.any?(cards, &(&1.rank == "3")) &&
        Enum.any?(cards, &(&1.rank == "2")) &&
        Enum.any?(cards, &(&1.rank == "A"))

  def has_rank_groups(%Hand{
        rank_grouping: rank_grouping
      }),
      do: !Enum.empty?(rank_grouping)

  def number_of_groupings(%Hand{} = hand, grouping),
    do:
      hand
      |> Map.fetch!(grouping)
      |> Map.to_list()
      |> length()

  def highest_group_rank(%Hand{rank_grouping: rank_grouping}),
    do:
      rank_grouping
      |> Enum.to_list()
      |> Enum.map(&elem(&1, 0))
      |> Enum.max(&Ranks.is_higher_first/2)

  def drop_rank_group(%Hand{rank_grouping: rank_grouping} = hand, rank),
    do: %Hand{hand | rank_grouping: Map.drop(rank_grouping, [rank])}

  defp has_flush(%Hand{suit_grouping: suit_grouping}),
    do:
      suit_grouping
      |> Map.values()
      |> Enum.any?(&(length(&1) == 5))

  defp has_full_house_or_better(hand),
    do:
      (has_target_length(hand, 3) && has_target_length(hand, 2)) ||
        has_target_length(hand, 4)

  defp has_target_length(%Hand{rank_grouping: rank_grouping}, target_length),
    do:
      rank_grouping
      |> Map.values()
      |> Enum.any?(&(length(&1) == target_length))
end

defmodule HandGroupingShape do
  defstruct [:largest_group_size, :largest_group_rank, :number_of_groups]

  def shape_of_rank_groupings(%Hand{rank_grouping: rank_grouping}) do
    rank_grouping_list = Map.to_list(rank_grouping)
    {largest_group_rank, largest_group} = Enum.max(rank_grouping_list, &has_largest_list_size/2)

    %HandGroupingShape{
      largest_group_size: length(largest_group),
      largest_group_rank: largest_group_rank,
      number_of_groups: length(rank_grouping_list)
    }
  end

  defp has_largest_list_size({_, rank_grouping_list_1}, {_, rank_grouping_list_2}),
    do: length(rank_grouping_list_1) > length(rank_grouping_list_2)

  def has_best_group_shaping(
        {%HandGroupingShape{
           largest_group_size: largest_group_size_1,
           largest_group_rank: largest_group_rank_1,
           number_of_groups: number_of_groups_1
         }, _hand_1},
        {%HandGroupingShape{
           largest_group_size: largest_group_size_2,
           largest_group_rank: largest_group_rank_2,
           number_of_groups: number_of_groups_2
         }, _hand_2}
      ),
      do:
        largest_group_size_1 > largest_group_size_2 ||
          (largest_group_size_1 == largest_group_size_2 && number_of_groups_1 > number_of_groups_2) ||
          (largest_group_size_1 == largest_group_size_2 &&
             number_of_groups_1 == number_of_groups_2 &&
             Ranks.is_higher_first(largest_group_rank_1, largest_group_rank_2))
end

defmodule Hands do
  def find_best_hands(hands) do
    hands_with_straight_flush = Enum.filter(hands, &(&1.has_straight && &1.has_flush))

    if !Enum.empty?(hands_with_straight_flush),
      do: find_best_ignoring_straight_flush(hands_with_straight_flush),
      else: find_best_ignoring_straight_flush(hands)
  end

  defp find_best_ignoring_straight_flush(hands) do
    hands_with_full_house_or_better = Enum.filter(hands, & &1.has_full_house_or_better)

    if !Enum.empty?(hands_with_full_house_or_better) do
      find_best_ignoring_flush(hands_with_full_house_or_better)
    else
      hands_with_flush = Enum.filter(hands, & &1.has_flush)

      if !Enum.empty?(hands_with_flush),
        do: find_best_ignoring_flush(hands_with_flush),
        else: find_best_ignoring_flush(hands)
    end
  end

  defp find_best_ignoring_flush(hands) do
    hands_with_straight = Enum.filter(hands, & &1.has_straight)

    if !Enum.empty?(hands_with_straight) do
      find_best_straight(hands_with_straight)
    else
      hands_with_groupings = Enum.filter(hands, &Hand.has_rank_groups/1)

      if Enum.empty?(hands_with_groupings),
        do: find_highest_cards(hands),
        else: find_best_group(hands_with_groupings)
    end
  end

  defp find_best_straight(hands) do
    {low_scoring_straights, high_scoring_straights} =
      Enum.split_with(hands, &Hand.is_low_scoring_straight/1)

    if Enum.empty?(high_scoring_straights),
      do: find_highest_cards(low_scoring_straights),
      else: find_highest_cards(high_scoring_straights)
  end

  defp find_highest_cards(hands) do
    highest_card =
      hands
      |> Enum.map(& &1.cards)
      |> Enum.map(&hd/1)
      |> Enum.max(&Hand.is_higher_first/2)

    hands_with_highest_card = Enum.filter(hands, &Hand.member?(&1, highest_card.rank))

    if length(hands_with_highest_card) == 1 || length(hd(hands_with_highest_card).cards) == 1,
      do: hands_with_highest_card,
      else:
        hands_with_highest_card
        |> Enum.map(&Hand.drop_first_card/1)
        |> find_highest_cards()
  end

  defp find_best_group(hands) do
    find_best_by_rank(hands)
  end

  defp find_best_by_rank(hands) do
    best_hands =
      {_largest_group_shape, hands_with_max_number_of_groupings} =
      hands
      |> Enum.group_by(&HandGroupingShape.shape_of_rank_groupings(&1))
      |> Enum.max(&HandGroupingShape.has_best_group_shaping/2)

    if length(hands_with_max_number_of_groupings) == 1,
      do: hands_with_max_number_of_groupings,
      else: find_best_rank_groups(best_hands)
  end

  defp find_best_rank_groups({_largest_group_shape, hands_with_max_number_of_groupings}) do
    best_hands =
      {_rank, hands_with_highest_ranked_group} =
      hands_with_max_number_of_groupings
      |> Enum.group_by(&Hand.highest_group_rank/1)
      |> Map.to_list()
      |> Enum.max(&find_higher_rank_group/2)

    if length(hands_with_highest_ranked_group) == 1,
      do: hands_with_highest_ranked_group,
      else: compare_remaining_cards_by_rank(best_hands)
  end

  defp compare_remaining_cards_by_rank({rank, hands_with_highest_ranked_group}) do
    hands_without_best_group =
      Enum.map(hands_with_highest_ranked_group, &Hand.drop_rank_group(&1, rank))

    remaining_group_count =
      hands_without_best_group
      |> hd()
      |> Map.fetch!(:rank_grouping)
      |> Map.values()
      |> length()

    if remaining_group_count > 0,
      do: find_best_by_rank(hands_without_best_group),
      else: find_highest_cards(hands_without_best_group)
  end

  defp find_higher_rank_group({rank_1, _}, {rank_2, _}),
    do: Ranks.is_higher_first(rank_1, rank_2)
end

defmodule Poker do
  @doc """
  Given a list of poker hands, return a list containing the highest scoring hand.

  If two or more hands tie, return the list of tied hands in the order they were received.

  The basic rules and hand rankings for Poker can be found at:

  https://en.wikipedia.org/wiki/List_of_poker_hands

  For this exercise, we'll consider the game to be using no Jokers,
  so five-of-a-kind hands will not be tested. We will also consider
  the game to be using multiple decks, so it is possible for multiple
  players to have identical cards.

  Aces can be used in low (A 2 3 4 5) or high (10 J Q K A) straights, but do not count as
  a high card in the former case.

  For example, (A 2 3 4 5) will lose to (2 3 4 5 6).

  You can also assume all inputs will be valid, and do not need to perform error checking
  when parsing card values. All hands will be a list of 5 strings, containing a number
  (or letter) for the rank, followed by the suit.

  Ranks (lowest to highest): 2 3 4 5 6 7 8 9 10 J Q K A
  Suits (order doesn't matter): C D H S

  Example hand: ~w(4S 5H 4C 5D 4H) # Full house, 5s over 4s
  """
  @spec best_hand(list(list(String.t()))) :: list(list(String.t()))
  def best_hand(hand_code)
      when length(hand_code) == 1,
      do: hand_code

  def best_hand(hand_code),
    do:
      hand_code
      |> Enum.map(&Hand.from_code/1)
      |> Hands.find_best_hands()
      |> Enum.map(& &1.codes)
end
