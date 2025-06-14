
# 📦 Project Design: Smart Wallet

A multidisciplinary project focused on designing a Smart Wallet with enhanced security, tracking, and real-time data access, featuring custom PCB, mobile application, web integration, and mechanical design.

## 🧠 Features Overview
### 🔐 Security

- Unauthorized access protection
- Anti-loss alarm (Bluetooth disconnection)
- Fall detection
- Precise location recovery

### 📱 Functionality
- Bluetooth communication with Android app (BLE)

- Real-time statistics:
- Date and time of wallet openings
- Detection of abnormal movements
- Unauthorized access logs
- Last known location

### 🔧 Ergonomics and Durability

- Compact PCB design to fit in standard wallets
- BLE for low power consumption and long battery life

# 📁 Repository Structure

SmartWallet/
│
├── README.md                  <- General overview (this file)
│
├── hardware/
│   ├── README.md              <- Schematic, PCB layout (2D & 3D), test results
│   ├── schematic/             <- Altium files
│   ├── pcb/                   <- GERBER files, 2D/3D renders
│   └── testing/               <- Images/videos of pre-solder and post-solder testing
│
├── firmware/
│   ├── README.md              <- Microcontroller code overview
│   └── src/                   <- C/C++ source code
│
├── android_app/
│   ├── README.md              <- App features and Bluetooth integration
│   └── SmartWalletApp/        <- Full Android Studio project
│
├── web/
│   ├── README.md              <- Website functionality and usage
│   └── site/                  <- HTML/CSS/JS or framework files
│
├── database/
│   ├── README.md              <- MongoDB Atlas structure and integration
│   └── schema/                <- JSON or schema descriptions
│
└── casing_design/
    ├── README.md              <- Design rationale and print instructions
    └── 3d_models/             <- STL, OBJ, Fusion/Inventor files

## 🛠 Technologies Used

- **PCB Design:** Altium
- **Microcontroller:** nRF51822
- **Bluetooth:** BLE (Low Energy)
- **Mobile App:** Android (Java/Kotlin)
- **Database:** MongoDB Atlas
- **Web:** HTML/CSS/JS or framework (React/Vue/etc.)
- **3D Design:** Fusion 360

## 🚀 Getting Started

To explore each part of the project, navigate into the corresponding folder and refer to its README.md.


# 👥 Credits

Josh Sebastián López Murcia
