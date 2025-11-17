using System;
using System.Collections.Generic;
using System.Linq;

public static class ResistorColorDuo
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

    public static int Value(string[] colors)
    {
        string code = "";
        foreach(string color in new List<string>(colors).Take(2))
        {
            code += OrderedColors.IndexOf(color)
                .ToString();
        }

        return int.Parse(code);
    }
}
