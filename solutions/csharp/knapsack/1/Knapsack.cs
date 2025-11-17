#pragma warning disable IDE0079
#pragma warning disable CA1050
#pragma warning disable IDE0130
public static class Knapsack
{
    public static int MaximumValue(int maximumWeight, (int weight, int value)[] items)
    {
        Item[] itemArray = [.. items.Select(x => new Item(x.weight, x.value))];
        KnapsackState knapsackState = new(itemArray, maximumWeight);
        return CalculateMaximumValue(knapsackState);
    }

    private static int CalculateMaximumValue(KnapsackState knapsackState) 
        => OptimalValueTable.Build(knapsackState)
            .GetSelectedItems()
            .Sum(item => item.Value);
}

public readonly struct Item(int weight, int value)
{
    public int Weight { get; } = weight;
    public int Value { get; } = value;
}

public readonly struct KnapsackState(Item[] items, int maxWeightCapacity)
{
    public Item[] Items { get; } = items;
    public int MaxWeightCapacity { get; } = maxWeightCapacity;
    public int ItemCount
        => Items.Length;
}

public readonly struct OptimalValueTable
{
    private readonly DynamicProgrammingGrid _dynamicProgrammingGrid;
    private readonly KnapsackState _knapsackState;

    private OptimalValueTable(DynamicProgrammingGrid dynamicProgrammingGrid, KnapsackState knapsackState)
    {
        _dynamicProgrammingGrid = dynamicProgrammingGrid;
        _knapsackState = knapsackState;
    }

    public static OptimalValueTable Build(KnapsackState knapsackState)
    {
        DynamicProgrammingGrid dynamicProgrammingGrid = new(knapsackState.ItemCount, knapsackState.MaxWeightCapacity);
        FillDynamicProgrammingGrid(dynamicProgrammingGrid, knapsackState);
        return new(dynamicProgrammingGrid, knapsackState);
    }

    private static void FillDynamicProgrammingGrid(DynamicProgrammingGrid grid, KnapsackState state) 
        => Enumerable.Range(1, state.ItemCount)
            .ToList()
            .ForEach(currentItemIndex =>
            {
                Item currentItem = state.Items[currentItemIndex - 1];
                CellCalculationContext calculationContext = new(grid, currentItem, currentItemIndex);
                FillGridRowForItem(calculationContext, state.MaxWeightCapacity);
            });

    private static void FillGridRowForItem(CellCalculationContext context, int maxWeightCapacity) 
        => Enumerable.Range(0, maxWeightCapacity + 1)
            .ToList()
            .ForEach(currentWeightCapacity =>
            {
                int optimalValueForCurrentCell = CalculateOptimalValueForCell(context, currentWeightCapacity);
                context.Grid.SetMaxValueForItemsAndWeight(context.ItemIndex, currentWeightCapacity, optimalValueForCurrentCell);
            });

    private static int CalculateOptimalValueForCell(CellCalculationContext context, int weightCapacity)
    {
        if (context.CurrentItem.Weight > weightCapacity)
        {
            return MaxValueForItemsAndWeight(context, weightCapacity);
        }

        int previousItemIndex = context.ItemIndex - 1;
        int maxValueWithoutCurrentItem = context.Grid.GetMaxValueForItemsAndWeight(previousItemIndex, weightCapacity);

        int remainingWeightCapacity = weightCapacity - context.CurrentItem.Weight;
        int maxValueWithCurrentItem = context.Grid.GetMaxValueForItemsAndWeight(previousItemIndex, remainingWeightCapacity);
        int totalValueWithCurrentItem = maxValueWithCurrentItem + context.CurrentItem.Value;

        return Math.Max(maxValueWithoutCurrentItem, totalValueWithCurrentItem);
    }

    private static int MaxValueForItemsAndWeight(CellCalculationContext context, int weightCapacity)
        => context.Grid.GetMaxValueForItemsAndWeight(context.ItemIndex - 1, weightCapacity);

    public int GetOptimalValue(int itemIndex, int weightCapacity)
        => _dynamicProgrammingGrid.GetMaxValueForItemsAndWeight(itemIndex, weightCapacity);

    public bool WasItemSelected(int itemIndex, int weightCapacity)
    {
        int currentValue = GetOptimalValue(itemIndex, weightCapacity);
        int previousValue = GetOptimalValue(itemIndex - 1, weightCapacity);
        return currentValue != previousValue;
    }

    public List<Item> GetSelectedItems()
    {
        SelectionState selectionState = CreateInitialSelectionState();
        IEnumerable<int> itemIndicesInReverseOrder = Enumerable.Range(1, _knapsackState.ItemCount)
            .Reverse();

        return itemIndicesInReverseOrder
            .Aggregate(selectionState, ProcessSingleItemSelection)
            .SelectedItems;
    }

    private SelectionState CreateInitialSelectionState()
        => new([], _knapsackState.MaxWeightCapacity);

    private SelectionState ProcessSingleItemSelection(SelectionState currentState, int itemIndex)
        => IsItemSelectionValid(currentState, itemIndex)
            ? currentState
            : AddItemToSelection(currentState, itemIndex);

    private bool IsItemSelectionValid(SelectionState currentState, int itemIndex)
        => currentState.RemainingWeightCapacity <= 0
           || !WasItemSelected(itemIndex, currentState.RemainingWeightCapacity);

    private SelectionState AddItemToSelection(SelectionState currentState, int itemIndex)
    {
        Item selectedItem = _knapsackState.Items[itemIndex - 1];
        List<Item> updatedSelectedItems = [.. currentState.SelectedItems, selectedItem];
        int updatedRemainingWeight = currentState.RemainingWeightCapacity - selectedItem.Weight;

        return new(updatedSelectedItems, updatedRemainingWeight);
    }
}

public readonly struct DynamicProgrammingGrid
{
    private readonly int[,] _maxValueByItemAndWeight;
    public int ItemRows { get; }
    public int WeightColumns { get; }

    public DynamicProgrammingGrid(int itemCount, int maxWeightCapacity)
    {
        ItemRows = itemCount + 1;
        WeightColumns = maxWeightCapacity + 1;
        _maxValueByItemAndWeight = new int[ItemRows, WeightColumns];
    }

    public int GetMaxValueForItemsAndWeight(int itemIndex, int weightCapacity)
        => _maxValueByItemAndWeight[itemIndex, weightCapacity];

    public void SetMaxValueForItemsAndWeight(int itemIndex, int weightCapacity, int maxValue)
        => _maxValueByItemAndWeight[itemIndex, weightCapacity] = maxValue;
}

public readonly struct CellCalculationContext(DynamicProgrammingGrid grid, Item currentItem, int itemIndex)
{
    public DynamicProgrammingGrid Grid { get; } = grid;
    public Item CurrentItem { get; } = currentItem;
    public int ItemIndex { get; } = itemIndex;
}

internal readonly struct SelectionState(List<Item> selectedItems, int remainingWeightCapacity)
{
    public List<Item> SelectedItems { get; } = selectedItems;
    public int RemainingWeightCapacity { get; } = remainingWeightCapacity;
}