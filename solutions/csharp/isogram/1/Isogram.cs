using System.Linq;
using System.Text.RegularExpressions;

namespace Isogram {
    public static class Isogram {
        public static bool IsIsogram(string word) {
            char[] letters = Regex.Replace(word, @"[-|\s]", "")
                .ToUpper()
                .ToCharArray();

            int distinctLetterCount = letters.Distinct()
                .Count();
            return letters.Length == distinctLetterCount;
        }
    }
}