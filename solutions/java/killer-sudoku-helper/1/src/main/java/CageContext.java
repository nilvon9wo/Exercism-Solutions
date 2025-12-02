import java.util.List;

public record CageContext(List<Integer> candidates, int size, int target) {
	public CageContext getNext(Integer i, int n) {
		int candidatesSize  = this.candidates()
				          .size();
		List<Integer> remaining = this.candidates()
				                          .subList(i + 1, candidatesSize);
		return new CageContext(
				remaining,
				this.size() - 1,
				this.target() - n
		);
	}
}