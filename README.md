# Gaming Console Layout Engine

Turn your Android smartphone into a low-latency, responsive wireless game controller and custom touchpad interface for your PC over Wi-Fi. This ecosystem combines a native Android HUD controller client with a lightweight Java desktop automation server.

---

##  Key Features

* **Smart Full-Screen Touchpad:** Emulates fluid desktop look/camera actions using relative pointer displacements (`MOUSE_MOVE:dx:dy`), dynamically masking out structural UI button rects to prevent input interception.
* **Low-Latency Combat HUD Bindings:** Hardcoded manual intercept lines provide zero-delay routing for critical actions (`MOUSE_FIRE` and `MOUSE_SCOPE`).
* **Fluid Joystick Mapping Engine:** Dynamic touch tracking calculations translate mobile screen drag deltas into immediate keyboard movement keys (`W`, `A`, `S`, `D`).
* **Clean Logging Pipeline:** Clear terminal diagnostics (`[KEYBOARD]`, `[MOUSE]`, `[TOUCHPAD]`) let you easily monitor your incoming UDP data payloads in real time.

---

## Architecture Layout

* **`/app` (Android Client):** Native mobile app built using high-performance overlay listener states to avoid thread blockages.
* **`/src` (Java Desktop Server):** Multi-threaded desktop listener leveraging `java.awt.Robot` hardware driver level emulation to route input packets straight to active window contexts.

---

## Deployment Steps & Usage Guide

You can access the source code here or navigate directly to the **Releases** tab to download the pre-compiled builds.

### 1. Setup the Android Controller App
1. Navigate to the latest release block and download **`Gaming.console.mobile.apk`**.
2. Transfer the package to your Android device and install it (enable *"Install from Unknown Sources"* if prompted by your system installer).
3. Ensure your phone is connected to the exact same local Wi-Fi network subnet as your target PC.

###  2. Launch the Desktop Server Dashboard
1. Download **`Gaming.console.windows.exe`** from the project's release page.
2. Launch the executable. A dedicated console debug terminal window will pop up showing:
  
Note on Windows Security: Because the .exe file is newly compiled, click More Info -> Run Anyway if Windows Defender SmartScreen blocks it on your initial setup attempt.

 3. Establish the Wireless Network Handshake
Open the app on your smartphone device.

Tap the Settings Gear Icon located on the top left corner of the layout menu bar.

This fires a targeted UDP network broadcast across port 5556. The desktop server will automatically acknowledge it with a SERVER_HERE validation token and bind a high-speed highway connection over port 5555.

Your server terminal window will immediately print incoming button data lines:

Plaintext
[SERVER] Packet Received : MOUSE_FIRE:DOWN
[MOUSE] ▶ PRESS   -> LEFT CLICK (FIRE)
Open your PC game emulator environment, map your key binds, and start playing!




Development Tools Implemented
Mobile Core: Java, Android SDK Framework (ConstraintLayout, View Interceptors, GlobalVisibleRect)

Desktop Core: Java Core 17+, java.awt.Robot Native Driver Hooks

Packaging: Launch4j Wrappers (windres.exe multi-res resource compilation compilers)


---


