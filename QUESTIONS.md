### Additional Assumptions and Questions

1. **Application Type and Usage:**
    - Considering the assignment is about a congestion tax calculator, will this application operate as a job or a real-time service?
        - Could it function both ways, with a UI for users to input VIN (Vehicle Identification Number) and date range, and also run scheduled jobs on a monthly basis to charge all vehicles?
    - What specific requirements are needed for the UI and the scheduled job?
    - How will real-time data processing and scheduled batch processing be managed within the same application?

2. **Parameter Storage:**
    - Currently, parameters are implemented in a JSON file. When the application goes live, will these parameters be stored in a database?
        - If so, what type of database will be used (SQL, NoSQL)?
        - How will the data be structured in the database?
        - What strategies will be used to migrate the current JSON data to the database?
    - How will updates to parameters (e.g., tax rates, exempted vehicles, holidays) be handled in the production environment?

3. **Data Handling:**
    - How will the provided list of dates be used? Are these dates for testing purposes or actual input data for the application?
    - What methods will be used for data input and output in the application?

4. **Scalability and Performance:**
    - How will the application handle a large volume of data, particularly if it functions as both a real-time service and a scheduled job?

5. **Security:**
    - How will the application be secured, especially if it is exposed as a real-time service over HTTP?
    - What measures will be taken to protect sensitive data (e.g., VIN)?

6. **Error Handling and Logging:**
    - What strategies will be employed to handle errors in both the real-time service and scheduled jobs?
    - How will application errors and performance issues be monitored and alerted?

7. **Testing and Validation:**
    - How will the accuracy of the congestion tax calculations be ensured?
    - What types of tests will be implemented (unit tests, integration tests, end-to-end tests)?
