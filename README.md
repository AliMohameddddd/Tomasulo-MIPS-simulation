# Tomasulo Algorithm Simulator

## Introduction
This project is designed to simulate the Tomasulo algorithm, which is used to dynamically schedule instructions to improve the concurrency of processors. The simulation will demonstrate how MIPS instructions are processed using Tomasulo's algorithm, showing the interaction between different components such as reservation stations, register files, caches, and queues.

## Project Overview
The simulator is built to handle MIPS assembly instructions and illustrate the cycle-by-cycle execution of these instructions. It will identify and manage various types of hazards, including RAW (Read After Write), WAR (Write After Read), and WAW (Write After Write), ensuring efficient instruction flow within the simulated microprocessor environment.

## Motivation
This simulator serves as an educational tool to deepen understanding of:
- **Dynamic Scheduling**: Exploring Tomasulo's algorithm for handling instruction dependencies.
- **Concurrency Optimization**: Enhancing instruction throughput in pipelined architectures.
- **Complex Hazard Handling**: Addressing and resolving instruction execution hazards dynamically.

## Features

### Core Functionalities
- **Detailed Simulation**: Step-by-step execution of MIPS instructions with visualization of internal states.
- **Hazard Management**: Automated detection and management of instruction-level hazards.
- **Customizable Environment**: Ability to set instruction latencies and configure sizes of stations and buffers.

### Technical Specifications
- **Supported Operations**: Includes floating point and integer operations, load/store instructions, and branch management.
- **User Interactions**: Command-line interface for inputting instructions and configuration settings, with optional GUI enhancement as a bonus.

## Installation and Setup
To set up and run the Tomasulo algorithm simulator, follow these steps:

1. **Clone the repository**:
   ```bash
   git clone https://github.com/yourusername/tomasulo-simulator.git
   ```
2. **Navigate to the project directory**:
   ```bash
   cd tomasulo-simulator
   ```
3. **Compile the application** (make sure your development environment is set up for the chosen programming language):
   ```bash
   make build  # This is an example command
   ```
4. **Run the simulator**:
   ```bash
   make run  # This launches the simulator
   ```

## How to Use
- **Load Instructions**: Input MIPS assembly instructions one by one or load them from a text file.
- **Configure Simulation**: Set the latencies for each instruction type and adjust the sizes of the buffers and stations as needed.
- **Run Simulation**: Execute the simulation and observe the output for each cycle, including the state of reservation stations, registers, and the cache.

## Contributing
Contributions to enhance the functionality, improve user experience, or extend the simulator's capabilities are welcome. For substantial changes, please open an issue first to discuss what you would like to change.

## Credits
This project was developed by students of the CSEN 702 course at [Your University Name], under the guidance of their course instructors.

## License
[![MIT License](https://img.shields.io/badge/license-MIT-blue.svg)](https://opensource.org/licenses/MIT)

This software is licensed under the MIT License - see the LICENSE.md file for details.
```

This README is designed to provide a thorough overview of your Tomasulo Algorithm Simulator project, its purpose, how to set it up, and how to use it effectively. Adjust any specific details or links to fit your actual project implementation and repository location.
