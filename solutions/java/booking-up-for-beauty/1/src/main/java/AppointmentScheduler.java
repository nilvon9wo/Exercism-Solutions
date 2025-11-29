import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;

class AppointmentScheduler {
	private static final DateTimeFormatter parser = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");

    public LocalDateTime schedule(String appointmentDateDescription) {
        return LocalDateTime.parse(appointmentDateDescription, parser);
    }

	public boolean hasPassed(LocalDateTime appointmentDate) {
		LocalDateTime now = LocalDateTime.now();
		return now.isAfter(appointmentDate);
	}

    public boolean isAfternoonAppointment(LocalDateTime appointmentDate) {
	    int hour = appointmentDate.getHour();
		return hour >= 12 && hour < 18;
    }

    public String getDescription(LocalDateTime appointmentDate) {
		int hour = appointmentDate.getHour();
	    boolean isAfternoon = hour >= 12;
		int displayHour = (isAfternoon)
								  ? hour - 12
								  : hour;
	    String timeOfDay = isAfternoon
			               ? "PM"
			               : "AM";

	    return "You have an appointment on "
					   + DateUtils.toPascalCase(appointmentDate.getDayOfWeek()) + ", "
					   + DateUtils.toPascalCase(appointmentDate.getMonth()) + " "
					   + appointmentDate.getDayOfMonth() + ", "
					   + appointmentDate.getYear() + ", at "
					   + displayHour + ":"
				       + String.format("%02d", appointmentDate.getMinute()) + " "
					   + timeOfDay + ".";
    }

    public LocalDate getAnniversaryDate() {
	    int year = LocalDate.now()
			               .getYear();
	    return LocalDate.of(year, Month.SEPTEMBER, 15);
	}
}
