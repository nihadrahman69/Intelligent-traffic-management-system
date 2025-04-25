import java.util.*;
import java.io.*;
import java.nio.file.*;

class TrafficSignal {
    protected String location;
    protected String currentColor;
    protected long lastChanged;

    public TrafficSignal(String location) {
        this.location = location;
        this.currentColor = "Red";
        this.lastChanged = System.currentTimeMillis();
    }

    public String getLocation() {
        return location;
    }

    public String getCurrentColor() {
        return currentColor;
    }

    public void changeSignal(String color) {
        this.currentColor = color;
        this.lastChanged = System.currentTimeMillis();
        String msg = "Signal at " + location + " changed to " + color;
        System.out.println(msg);
        logEvent(msg);
    }

    public void displaySignal() {
        System.out.println(location + ": " + currentColor);
    }

    public boolean isReadyForChange(long minDuration) {
        return (System.currentTimeMillis() - lastChanged) >= minDuration;
    }

    public void logEvent(String event) {
        try {
            String logEntry = new Date() + ": " + event + "\n";
            Files.write(Paths.get("traffic_log.txt"), logEntry.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.err.println("Logging failed: " + e.getMessage());
        }
    }
}

public class InteractiveTrafficSystem {
    private static final long MIN_SIGNAL_DURATION = 5000; 
    private static String lastAction = "Welcome to Intelligent Traffic Control System!";
    private static Scanner scanner = new Scanner(System.in);
    private static Random random = new Random();
    private static Map<String, TrafficSignal> signals = new LinkedHashMap<>();
    private static SmartTrafficControl controlSystem = new SmartTrafficControl();

    public static void main(String[] args) {
        String[] locations = {"Mirpur 10", "Dhanmondi 27", "Green road", "Gulshan 1", "Aftabnagar"};
        for (String loc : locations) {
            signals.put(loc.toLowerCase(), new TrafficSignal(loc));
        }

        while (true) {
            clearScreen();
            System.out.println(lastAction + "\n");
            printMenu();
            int choice = readInt("Enter choice (1-6): ");

            switch (choice) {
                case 1:
                    lastAction = "Current Signal States:";
                    clearScreen();
                    System.out.println(lastAction + "\n");
                    signals.values().forEach(TrafficSignal::displaySignal);
                    pause();
                    break;

                case 2:
                    manualTrafficInput();
                    pause();
                    break;

                case 3:
                    manualSignalChange();
                    pause();
                    break;

                case 4:
                    simulateEmergency();
                    pause();
                    break;

                case 5:
                    sensorSimulation();
                    pause();
                    break;

                case 6:
                    System.out.println("Exiting system.");
                    return;

                default:
                    lastAction = "Invalid choice. Please select 1-6.";
            }
        }
    }

    private static void printMenu() {
        System.out.println("--- Intelligent Traffic Control System ---");
        System.out.println("1. Display Signal States");
        System.out.println("2. Simulate Traffic Input");
        System.out.println("3. Manual Signal Change");
        System.out.println("4. Simulate Emergency Vehicle");
        System.out.println("5. Simulate Sensor Input (Random)");
        System.out.println("6. Exit");
    }

    private static void manualTrafficInput() {
        System.out.print("Available signals: ");
        signals.values().forEach(s -> System.out.print(s.getLocation() + ", "));
        System.out.println();
        String locKey = readString("Enter signal location: ").toLowerCase();
        TrafficSignal signal = signals.get(locKey);
        if (signal == null) {
            lastAction = "Invalid location. Please try again.";
            return;
        }
        if (!signal.isReadyForChange(MIN_SIGNAL_DURATION)) {
            lastAction = "Signal at " + signal.getLocation() + " not ready to change yet.";
            return;
        }
        int count = readInt("Enter number of vehicles: ");
        String type = readString("Enter vehicle type (car/bike/bus/truck): ");
        controlSystem.controlTraffic(signal, count, type, false);
        lastAction = "Manual input at " + signal.getLocation() + ": count=" + count + ", type=" + type + ". Now " + signal.getCurrentColor();
    }

    private static void manualSignalChange() {
        System.out.print("Available signals: ");
        signals.values().forEach(s -> System.out.print(s.getLocation() + ", "));
        System.out.println();
        String locKey = readString("Enter signal location: ").toLowerCase();
        TrafficSignal signal = signals.get(locKey);
        if (signal == null) {
            lastAction = "Invalid location. Please try again.";
            return;
        }
        String color = readString("Enter new signal color (Red/Yellow/Green): ");
        signal.changeSignal(color);
        lastAction = "Manually changed " + signal.getLocation() + " to " + color;
    }

    private static void simulateEmergency() {
        System.out.print("Available signals: ");
        signals.values().forEach(s -> System.out.print(s.getLocation() + ", "));
        System.out.println();
        String locKey = readString("Enter location of emergency vehicle: ").toLowerCase();
        TrafficSignal signal = signals.get(locKey);
        if (signal == null) {
            lastAction = "Invalid location. Please try again.";
            return;
        }
        controlSystem.controlTraffic(signal, 0, "emergency", true);
        lastAction = "Emergency at " + signal.getLocation() + ". Signal turned Green.";
    }

    private static void sensorSimulation() {
        StringBuilder sb = new StringBuilder("Sensor Simulation: ");
        for (TrafficSignal signal : signals.values()) {
            int count = random.nextInt(400); // realistic range
            controlSystem.controlTraffic(signal, count, "car", false);
            sb.append(signal.getLocation()).append("=").append(count).append(" ");
        }
        lastAction = sb.toString();
    }

    private static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private static void pause() {
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    private static int readInt(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            scanner.next();
            System.out.print("Invalid. " + prompt);
        }
        int val = scanner.nextInt();
        scanner.nextLine();
        return val;
    }

    private static String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
}

abstract class TrafficControl {
    public abstract void controlTraffic(TrafficSignal signal, int vehicleCount, String vehicleType, boolean isEmergency);
}

class SmartTrafficControl extends TrafficControl {
    @Override
    public void controlTraffic(TrafficSignal signal, int vehicleCount, String vehicleType, boolean isEmergency) {
        if (isEmergency) {
            signal.changeSignal("Green");
        } else if (vehicleType.equalsIgnoreCase("bus")) {
            signal.changeSignal(vehicleCount > 60 ? "Green" : "Red");
        } else if (vehicleType.equalsIgnoreCase("truck")) {
            signal.changeSignal(vehicleCount > 80 ? "Green" : "Red");
        } else if (vehicleType.equalsIgnoreCase("car")) {
            signal.changeSignal(vehicleCount > 150 ? "Green" : "Red");
        } else if (vehicleType.equalsIgnoreCase("bike")) {
            signal.changeSignal(vehicleCount > 300 ? "Green" : "Red");
        } else {
            // default to car threshold
            signal.changeSignal(vehicleCount > 150 ? "Green" : "Red");
        }
    }
}
