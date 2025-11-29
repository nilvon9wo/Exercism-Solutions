import java.util.HashMap;
import java.util.Map;

public class DialingCodes {
	private final Map<Integer, String> codeByCountries = new HashMap<>();
	private final Map<String, Integer> countryByCodes = new HashMap<>();

    public Map<Integer, String> getCodes() {
		return this.codeByCountries;
    }

    public void setDialingCode(Integer code, String country) {
		this.codeByCountries.put(code, country);
	    this.countryByCodes.put(country, code);
    }

    public String getCountry(Integer code) {
		return this.codeByCountries.get(code);
    }

	public void addNewDialingCode(Integer code, String country) {
		if (!codeByCountries.containsKey(code) && !countryByCodes.containsKey(country)) {
			codeByCountries.put(code, country);
			countryByCodes.put(country, code);
		}
	}

    public Integer findDialingCode(String country) {
	    return this.countryByCodes.get(country);
    }

	public void updateCountryDialingCode(Integer code, String country) {
		this.removeOldMappingByCountry(country);
		this.removeOldMappingByCode(code);
		codeByCountries.put(code, country);
		countryByCodes.put(country, code);
	}

	private void removeOldMappingByCode(Integer code) {
		String oldCountry = codeByCountries.get(code);
		if (oldCountry != null) {
			countryByCodes.remove(oldCountry);
		}
	}

	private void removeOldMappingByCountry(String country) {
		Integer oldCode = countryByCodes.get(country);
		if (oldCode != null) {
			codeByCountries.remove(oldCode);
		}
	}
}
