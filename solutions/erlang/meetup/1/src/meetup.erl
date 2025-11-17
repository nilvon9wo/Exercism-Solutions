-module(meetup).

-export([meetup/4]).

-define(WEEKDAYS, [monday, tuesday, wednesday, thursday, friday, saturday, sunday]).
-define(ORDINAL, [first, second, third, fourth]).

meetup(Year, Month, TargetWeekday, teenth = _Schedule) ->
  find_day(
    Year,
    Month,
    TargetWeekday,
    _BaseDay = 13,
    fun(Offset) ->
      _BaseOffset = Offset
    end
  );

meetup(Year, TargetMonth, TargetWeekday, last = _Schedule) ->
  FourthTargetDay = meetup(Year, TargetMonth, TargetWeekday, fourth),
  CandidateDate = add_days_to_day(FourthTargetDay, 7),
  {_CandidateYear, CandidateMonth, _CandidateDay} = CandidateDate,

  if
    TargetMonth == CandidateMonth ->
      CandidateDate;
    true ->
      FourthTargetDay
  end;

meetup(Year, Month, TargetWeekday, Schedule) ->
  find_day(
    Year,
    Month,
    TargetWeekday,
    _BaseDay = 1,
    _BaseOffset = fun(Offset) ->
      find_by_indexed_ordinal(Schedule, Offset)
                  end
  ).

find_by_indexed_ordinal(Schedule, DayDifference) ->
  7 * (index_of(Schedule, ?ORDINAL) - 1) + DayDifference.

find_day(Year, Month, TargetWeekday, BaseDay, BaseOffsetFunction) ->
  {ok, NthOfMonth} = create_date(Year, Month, BaseDay),
  NthDayNumber = find_day_of_week(NthOfMonth),
  TargetDayNumber = to_day_number(TargetWeekday),
  DayDifference = TargetDayNumber - NthDayNumber,
  CandidateDay = add_days_to_day(NthOfMonth, BaseOffsetFunction(DayDifference)),

  if
    NthDayNumber =< TargetDayNumber ->
      CandidateDay;
    true ->
      add_days_to_day(CandidateDay, 7)
  end.

create_date(Year, Month, BaseDay) ->
  {ok, {Year, Month, BaseDay}}.

find_day_of_week(NthOfMonth) ->
  calendar:day_of_the_week(NthOfMonth).

add_days_to_day(Date, DaysToAdd) ->
  GregorianDays = calendar:date_to_gregorian_days(Date),
  calendar:gregorian_days_to_date(GregorianDays + DaysToAdd).

to_day_number(DayAtom) ->
  index_of(DayAtom, ?WEEKDAYS).

index_of(Item, List) -> index_of(Item, List, 1).

index_of(_, [], _) -> not_found;
index_of(Item, [Item | _], Index) -> Index;
index_of(Item, [_ | Tl], Index) -> index_of(Item, Tl, Index + 1).