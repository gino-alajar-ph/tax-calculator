# Congestion Tax Calculator

This project is a Spring Boot application designed to calculate congestion taxes for vehicles based on their entry and exit timestamps in various cities. The tax rules and exemptions can vary between cities, and the application is designed to be flexible and easily extendable to handle different tax policies for different cities.

## Features

- Calculate congestion tax for vehicles based on timestamps.
- Support for different tax rules for different cities.
- Polymorphic deserialization for different vehicle types.
- Flexible and maintainable design using design patterns like Strategy and Dependency Injection.

## Getting Started

### Prerequisites

- Java 8 or higher
- Maven 3.6.3 or higher

### Installation

1. **Clone the repository**:
   ```sh
   git clone https://github.com/yourusername/congestion-tax-calculator.git
   cd congestion-tax-calculator
   ```

2. **Build the project**:
   ```sh
   mvn clean install
   ```

3. **Run the application**:
   ```sh
   mvn spring-boot:run
   ```

### Usage

To calculate the congestion tax, send a POST request to the `/api/calculate` endpoint with the following JSON payload:

```json
[
  {
    "city": "Gothenburg",
    "vehicle": {
      "type": "Car",
      "vin": "1HGCM82633A654321"
    },
    "timestamps": [
      "2013-01-14 21:00:00",
      "2013-01-15 21:00:00",
      "2013-02-07 06:23:27",
      "2013-02-07 15:27:00",
      "2013-02-08 06:27:00",
      "2013-02-08 06:20:27",
      "2013-02-08 14:35:00",
      "2013-02-08 15:29:00",
      "2013-02-08 15:47:00",
      "2013-02-08 16:01:00",
      "2013-02-08 16:48:00",
      "2013-02-08 17:49:00",
      "2013-02-08 18:29:00",
      "2013-02-08 18:35:00",
      "2013-03-26 14:25:00",
      "2013-03-28 14:07:27"
    ]
  }
]
```

### Example Curl Command

```sh
curl -X POST http://localhost:8080/api/calculate \
-H "Content-Type: application/json" \
-d '[
    {
        "city": "Gothenburg",
        "vehicle": {
            "type": "Car",
            "vin": "1HGCM82633A654321"
        },
        "timestamps": [
            "2013-01-14 21:00:00", 
            "2013-01-15 21:00:00", 
            "2013-02-07 06:23:27", 
            "2013-02-07 15:27:00", 
            "2013-02-08 06:27:00", 
            "2013-02-08 06:20:27", 
            "2013-02-08 14:35:00", 
            "2013-02-08 15:29:00", 
            "2013-02-08 15:47:00", 
            "2013-02-08 16:01:00", 
            "2013-02-08 16:48:00", 
            "2013-02-08 17:49:00", 
            "2013-02-08 18:29:00", 
            "2013-02-08 18:35:00", 
            "2013-03-26 14:25:00", 
            "2013-03-28 14:07:27"
        ]
    }
]'
```

### Expected Response

The response will be a JSON array containing the calculated tax results for each request. Here is an example response:

```json
[
  {
    "vin": "1HGCM82633A654321",
    "exempted": false,
    "taxResults": {
      "2013-03-28": 8,
      "2013-01-15": 0,
      "2013-03-26": 8,
      "2013-01-14": 0,
      "2013-02-08": 39,
      "2013-02-07": 21
    }
  }
]
```

## Project Structure

```
src/main/java/com/example/taxcalculator/
├── TaxCalculatorApplication.java
├── controller/
│   └── CongestionTaxController.java
├── exception/
│   ├── GlobalExceptionHandler.java
│   ├── UnsupportedCityException.java
│   └── UnsupportedVehicleTypeException.java
├── model/
│   ├── Bus.java
│   ├── Car.java
│   ├── Diplomat.java
│   ├── Emergency.java
│   ├── Military.java
│   ├── Motorbike.java
│   ├── TaxCalculationRequest.java
│   ├── TaxPeriod.java
│   └── Vehicle.java
├── service/
│   ├── CongestionTaxService.java
│   ├── JsonDataLoader.java
│   └── calculator/
│       ├── CongestionTaxCalculator.java
│       └── GothenburgCongestionTaxCalculator.java
src/main/resources/
├── application.properties
├── exempted-vehicles.json
├── holidays.json
└──  tax-rates.json

```
## Detailed Explanation

### Model Classes

- **Vehicle Interface**: Represents a vehicle. Using an interface allows for flexibility in adding more vehicle types in the future.

- **Car and Motorbike Implementations**: Implement the `Vehicle` interface.

- **TaxCalculationRequest**: Represents the request payload for tax calculation.

- **TaxPeriod**: Represent configuration data for tax periods.

### Service Layer

- **CongestionTaxService**: Core business logic for calculating the congestion tax.

- **JsonDataLoader**: Service responsible for loading configuration data. It reads data from external JSON files or databases.

### Controller

- **CongestionTaxController**: Handles HTTP requests and delegates to the service layer.

### Polymorphic Deserialization

- **Jackson Annotations and Configuration**: Ensures that Jackson can deserialize the `Vehicle` interface and its implementations.
    - `@JsonTypeInfo` and `@JsonSubTypes` annotations on the `Vehicle` interface handle polymorphic deserialization.

### Design Patterns

- **Strategy Pattern**: Used in the `CongestionTaxService` where different cities can have different tax calculation strategies (Gothenburg vs. other cities). The `CongestionTaxCalculator` interface and its implementations represent different strategies.

- **Dependency Injection**: Used throughout the application to inject dependencies, managed by Spring. This promotes loose coupling and easier testing.

## Running Tests

To run tests, use the following command:

```sh
mvn test
```

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/fooBar`)
3. Commit your changes (`git commit -m 'Add some fooBar'`)
4. Push to the branch (`git push origin feature/fooBar`)
5. Create a new Pull Request

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
