-module(saddle_points).

-export([saddle_points/1]).

-record(point, {row_index, column_index, value}).

%% It's called a "saddle point" because
%% it is greater than or equal to every element in its row
%% and less than or equal to every element in its column.
saddle_points(ValueRows) ->
  Points = createPoints(ValueRows),
  PointsWithGreatestOrEqualValueInOwnRow =
    findPointsWithGreatestOrEqualValueInOwnRow(Points),
  SaddlePoints =
    hasLeastOrEqualValueInColumn(Points, PointsWithGreatestOrEqualValueInOwnRow),
  toTuples(SaddlePoints).

findPointsWithGreatestOrEqualValueInOwnRow(Points) ->
  MaxValueForEachRow = findMaxValueForEachRow(Points),
  lists:filter(
    fun
      ({point, RowIndex, _ColumnIndex, Value}) ->
        Value >= maps:get(RowIndex, MaxValueForEachRow)
    end,
    Points
  ).

findMaxValueForEachRow(Points) ->
  PointsByRows = getPointsByRows(Points),
  maps:map(
    fun(_Key, PointsOnRow) ->
      findMaxValueFor(PointsOnRow)
    end,
    PointsByRows
  ).

hasLeastOrEqualValueInColumn(Points, PointsWithGreatestOrEqualValueInOwnRow) ->
  MinValueForEachColumn = findLeastValueForEachColumn(Points),
  lists:filter(
    fun
      ({point, _RowIndex, ColumnIndex, Value}) ->
        Value =< maps:get(ColumnIndex, MinValueForEachColumn)
    end,
    PointsWithGreatestOrEqualValueInOwnRow
  ).

findLeastValueForEachColumn(Points) ->
  PointsByColumns = getPointsByColumns(Points),
  maps:map(
    fun(_Key, PointsOnColumn) ->
      findMinValueFor(PointsOnColumn)
    end,
    PointsByColumns
  ).

findMaxValueFor(Points) ->
  Values = lists:map(fun extractValue/1, Points),
  lists:max(Values).

findMinValueFor(Points) ->
  Values = lists:map(fun extractValue/1, Points),
  lists:min(Values).

createPoints(ValueRows) ->
  ValueRowsWithIndexes = addIndexes(ValueRows),
  convertToPoints(ValueRowsWithIndexes, _PointsAccumulator = []).

addIndexes(ValueRows) ->
  ValueRowsWithIndex = lists:map(fun addInnerIndex/1, ValueRows),
  lists:zip(lists:seq(0, length(ValueRowsWithIndex) - 1), ValueRowsWithIndex).

addInnerIndex(InnerList) ->
  lists:zip(lists:seq(0, length(InnerList) - 1), InnerList).

convertToPoints(ValueRowsWithIndexes, PointsAccumulator)
  when length(ValueRowsWithIndexes) == 0 ->
  PointsAccumulator;

convertToPoints(ValueRowsWithIndexes, PointsAccumulator) ->
  UpdatedPointsAccumulator =
    convertRowToPoints(hd(ValueRowsWithIndexes), PointsAccumulator),
  convertToPoints(tl(ValueRowsWithIndexes), UpdatedPointsAccumulator).

convertRowToPoints({_RowIndex, ValuesWithColumnIndexes}, PointsAccumulator)
  when length(ValuesWithColumnIndexes) == 0 ->
  PointsAccumulator;

convertRowToPoints({RowIndex, ValuesWithColumnIndexes}, PointsAccumulator) ->
  [{ColumnIndex, Value} | RemainingValuesWithColumnIndexes] = ValuesWithColumnIndexes,
  Point = #point{
    row_index = RowIndex,
    column_index = ColumnIndex,
    value = Value
  },
  convertRowToPoints(
    {RowIndex, RemainingValuesWithColumnIndexes},
    [Point | PointsAccumulator]
  ).

getPointsByRows(Points) ->
  groupBy(fun extractRowIndex/1, Points).

getPointsByColumns(Points) ->
  groupBy(fun extractColumnIndex/1, Points).

groupBy(Function, List) ->
  PointsByRows = lists:foldr(
    fun appendDefinition/2,
    dict:new(),
    [{Function(X), X} || X <- List]
  ),
  Rows = dict:to_list(PointsByRows),
  maps:from_list(Rows).

appendDefinition({Key, Value}, Definition) ->
  dict:append(Key, Value, Definition).

extractRowIndex({point, RowIndex, _ColumnIndex, _Value}) ->
  RowIndex.

extractColumnIndex({point, _RowIndex, ColumnIndex, _Value}) ->
  ColumnIndex.

extractValue({point, _RowIndex, _ColumnIndex, Value}) ->
  Value.

toTuples(SaddlePoints) ->
  lists:map(
    fun ({point, RowIndex, ColumnIndex, _Value}) ->
      {RowIndex, ColumnIndex}
    end,
    SaddlePoints
  ).



