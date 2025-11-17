public class ElonsToyCar {
    private int distance = 0;
    private static final int DRIVE_INCREMENT = 20;
    private int battery = 100;
    private static final int BATTERY_DECREMENT = 1;

    public static ElonsToyCar buy() {
        return new ElonsToyCar();
    }

    public String distanceDisplay() {
        return "Driven " + distance + " meters";
    }

    public String batteryDisplay() {
        return hasBatteryEnergy()
               ? "Battery at " + battery + "%"
			   : "Battery empty";
    }

    public void drive() {
        if (hasBatteryEnergy()) {
            distance += DRIVE_INCREMENT;
            battery -= BATTERY_DECREMENT;
        }
    }

    private boolean hasBatteryEnergy() {
        return battery > 0;
    }
}
