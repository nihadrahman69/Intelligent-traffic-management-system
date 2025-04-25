# Interactive Traffic Control System

A Java-based console application that simulates an intelligent traffic light management system for urban intersections. It allows real-time signal monitoring, manual control, emergency vehicle prioritization, and sensor-based traffic simulation.



##  Features

-  Real-time display of signal states
-  Manual signal color change
-  Manual input of traffic conditions (vehicle type and count)
-  Emergency vehicle handling with signal priority
-  Random sensor-based simulation for realistic traffic behavior
-  Event logging to `traffic_log.txt`



##  System Overview

The system manages traffic lights at multiple intersections:
- **Mirpur 10**
- **Dhanmondi 27**
- **Green Road**
- **Gulshan 1**
- **Aftabnagar**

The logic is backed by a smart decision system that changes signals based on:
- Number of vehicles
- Vehicle types (car, bike, bus, truck)
- Emergency situations


##  File Structure

- `InteractiveTrafficSystem.java` – Main class with the console menu and UI interaction.
- `TrafficSignal` – Represents individual traffic signals and handles state changes and logging.
- `TrafficControl` – Abstract class defining the control interface.
- `SmartTrafficControl` – Implements intelligent control logic for signal changes.
- `traffic_log.txt` – Automatically created log file storing signal change events with timestamps.



##  How to Run

1. **Compile the project**:
   ```bash
   javac InteractiveTrafficSystem.java
   ```

2. **Run the application**:
   ```bash
   java InteractiveTrafficSystem
   ```



##  Menu Options

1. **Display Signal States** – View current color status of all intersections.
2. **Simulate Traffic Input** – Input vehicle type and count to simulate traffic manually.
3. **Manual Signal Change** – Directly change a signal's color.
4. **Simulate Emergency Vehicle** – Prioritize signal green for emergency passage.
5. **Simulate Sensor Input (Random)** – Auto-generate traffic data and update signals.
6. **Exit** – Close the system.



##  Signal Change Rules (Thresholds)

| Vehicle Type | Vehicles Required for Green Signal |
|--------------|------------------------------------|
| Car          | > 150                              |
| Bike         | > 300                              |
| Bus          | > 60                               |
| Truck        | > 80                               |
| Emergency    | Always prioritized to Green        |



##  Dependencies

- Java 8 or above
- No external libraries required



##  Logging

Every signal change is recorded in a `traffic_log.txt` file with timestamps, allowing post-analysis and debugging.

Example log entry:
```
Mon Apr 22 17:25:36 GMT 2025: Signal at Dhanmondi 27 changed to Green
```



##  Notes

- The system enforces a minimum signal duration of **5 seconds** before it can be changed again.
- Console screen clears between actions for better visibility (may not work on all terminal emulators).



##  Future Improvements

- GUI interface for easier interaction
- Real-time traffic camera feed integration
- AI-based adaptive control using live data



##  Author

Developed by Nihad Rahman Rawdra, Nafisa Hasan, Hla Hla Nu.  
Feel free to modify, extend, and improve this project.
