### Additional Assumptions and Questions

1. **Application Type and Usage:**
    - Since the assignment is about a congestion tax calculator, will this application run as a job or a service (real-time)?
        - I assume this will be both since we can have a UI where we can just use the VIN (Vehicle Identification Number) and Date Range.
        - At the same time, we can have a job that will run on a monthly basis to charge all vehicles.
    - What are the specific requirements for the UI and the scheduled job?
    - How will you handle real-time data processing and scheduled batch processing within the same application?

2. **Parameter Storage:**
    - Right now, all the parameters I implemented are in a JSON file. When it becomes an actual application, will these be saved in a database?
        - If so, what kind of database will be used (SQL, NoSQL)?
        - How will the data be structured in the database?
        - What strategies will you use to migrate the current JSON data to the database?
    - How will you handle updates to the parameters (e.g., tax rates, exempted vehicles, holidays) in the production environment?

3. **Data Handling:**
    - How will the list of dates provided be used? Are these for testing purposes or actual input data for the application?
    - How will you handle data input and output for the application?

4. **Scalability and Performance:**
    - How will the application handle a large volume of data, especially if it runs as a real-time service and a scheduled job?
 
5. **Security:**
    - How will you secure the application, especially if it is exposed as a real-time service over HTTP?
    - How will you protect sensitive data (e.g., VIN)?

6. **Error Handling and Logging:**
    - How will you handle errors in both real-time service and scheduled jobs?
    - How will you monitor and alert on application errors and performance issues?

7. **Testing and Validation:**
    - How will you ensure the accuracy of the congestion tax calculations?
    - What types of tests will you implement (unit tests, integration tests, end-to-end tests)?