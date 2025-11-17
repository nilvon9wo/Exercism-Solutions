using System;
using System.Collections.Generic;
using System.Linq;

public static class ResistorColorTrio
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

    public static string Label(string[] colors)
    {
        string code = "";
        foreach(string color in new List<string>(colors).Take(2))
        {
            code += OrderedColors.IndexOf(color)
                .ToString();
        }

        int zeroCount = OrderedColors.IndexOf(colors[2]);
        double ohms = int.Parse(code) * Math.Pow(10, zeroCount);
        return (ohms % 1000 != 0)
            ? ohms + " ohms"
            : ohms/1000 + " kiloohms";
    }
}
