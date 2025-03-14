# CarRentalSystem

CarRentalSystem is a Java-based application designed to manage car rental services, including functionalities for administrators and customers.

## Features

- **Administrator Functions**:
  - Manage car inventory
  - View rental history
  - Update pricing attributes

- **Customer Functions**:
  - Browse available cars
  - Make reservations
  - View rental history

## Requirements

- Java Development Kit (JDK) 8 or higher

## Installation

1. **Clone the repository**:

   ```bash
   git clone https://github.com/mMelnic/CarRentalSystem.git
   ```

2. **Navigate to the project directory**:

   ```bash
   cd CarRentalSystem
   ```

3. **Compile the source code**:

   ```bash
   javac -d bin src/*.java
   ```

## Usage

1. **Run the application**:

   ```bash
   java -cp bin Main
   ```

2. **Follow the on-screen instructions** to navigate through the application.

## File Structure

- `src/`: Contains the Java source code files.
- `lib/`: Directory for external libraries (currently empty).
- `admin_database.ser`, `carInventory.ser`, `customer_database.ser`, `pricingAttributes.ser`, `rental_history.ser`: Serialized files storing persistent data.
