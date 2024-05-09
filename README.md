### Loan Application Management System

This project implements a reactive architecture for managing loan applications, focusing on the highlighted part:

![](/architecture.png)

- **Data Upload Service**: Responsible for uploading documents to an S3 bucket and sending document IDs to a Kafka topic (`data_upload_topic`).
- **Commercial Service**: Consumes document IDs from the Kafka topic, forwards them to the OCR microservice, and calculates an initial score for the application based on data from external databases and OCR results.
- **OCR Microservice**: Reads document IDs from a Kafka topic (`doc_topic`), performs OCR on the corresponding documents, and stores the OCR results in a MongoDB database (`ocr_results_db`).

**Technologies Used:**
- Apache Kafka: For asynchronous communication between microservices.
- Amazon S3: For storing uploaded documents.
- MongoDB: For storing OCR results.
- Java: For implementing microservices and interacting with databases.

Note: The actual implementation involves real database connections, AWS S3 usage, and Kafka configurations, while the provided code snippets contain mock logic for demonstration purposes.