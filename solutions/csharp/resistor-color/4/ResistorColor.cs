using System.Collections.Generic;

public static class ResistorColor
{
    private static readonly List<string> OrderedColors = new List<string>{
      "black",
      "brown",
      "red",
      "orange",
      "yellow",
      "green",
      "blue",
      "violet",
      "grey",
      "white"
    };

    public static int ColorCode(string color) => 
        OrderedColors.IndexOf(color);

    public static string[] Colors() => 
        OrderedColors.ToArray();
}