public class Twofer {
    public String twofer(String name) {
		String display = (name != null && !name.isBlank())
				? name.trim()
				: "you";
		return 	"One for " + display + ", one for me.";
    }
}
