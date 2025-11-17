namespace Pangram {
    public static class Pangram {
        private static readonly char[] Alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".ToCharArray();
        public static bool IsPangram(string input) => hasAllLetters(letterList: input);

        private static bool hasAllLetters(string letterList) =>
            System.Linq.Enumerable.All(
                source: Alphabet, predicate: letter =>
                    letterList.ToUpper().Contains(value: letter)
            );
    }
}