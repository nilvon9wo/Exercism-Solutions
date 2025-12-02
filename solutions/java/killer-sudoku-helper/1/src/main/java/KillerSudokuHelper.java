import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class KillerSudokuHelper {

	public List<List<Integer>> combinationsInCage(Integer cageSum, Integer cageSize) {
		return this.combinationsInCage(cageSum, cageSize, List.of());
	}

	public List<List<Integer>> combinationsInCage(Integer cageSum, Integer cageSize, List<Integer> exclude) {
		List<Integer> candidates = IntStream.rangeClosed(1, 9)
				                           .filter(n -> !exclude.contains(n))
				                           .boxed()
				                           .collect(Collectors.toList());

		CageContext context = new CageContext(candidates, cageSize, cageSum);
		return this.combinations(context);
	}

	private List<List<Integer>> combinations(CageContext context) {
		return context.size() == 0
			       ? context.target() == 0
                             ? List.of(List.of())
                             : List.of()
			       : this.combineCandidates(context);
	}

	private List<List<Integer>> combineCandidates(CageContext context) {
		int candidatesSize = context.candidates().size();
		return IntStream.range(0, candidatesSize)
				       .boxed()
				       .flatMap(i -> this.combineWithCandidate(context, i))
				       .collect(Collectors.toList());
	}

	private Stream<List<Integer>> combineWithCandidate(CageContext context, Integer i) {
		int n = context.candidates()
				        .get(i);
		if (n > context.target()) {
			return Stream.empty();
		}

		CageContext nextContext = context.getNext(i, n);
		return this.combinations(nextContext)
				       .stream()
				       .map(list -> this.prependCandidate(list, n));
	}

	private List<Integer> prependCandidate(List<Integer> list, int n) {
		List<Integer> newList = new ArrayList<>();
		newList.add(n);
		newList.addAll(list);
		return newList;
	}
}
