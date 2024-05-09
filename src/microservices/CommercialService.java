package microservices;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

import models.BankTransactionData;
import models.LoanData;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;

public class CommercialService {
    private KafkaConsumer<String, String> consumer;
    private KafkaProducer<String, String> producer;
    private Connection loanDatabaseConnection;
    private Connection bankTransactionDatabaseConnection;
    private MongoClient mongoClient;
    private MongoCollection<Document> ocrResultCollection;

    public CommercialService(Properties consumerProps) throws SQLException {
        // Initialize Kafka consumer and producer
        this.consumer = new KafkaConsumer<>(consumerProps);
        this.producer = new KafkaProducer<>(producerProps);
        consumer.subscribe(Collections.singletonList("data_upload_topic"));

        // Initialize SQL connections
        this.loanDatabaseConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/loan_db", "admin", "admin");
        this.bankTransactionDatabaseConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank_transactions_db", "admin", "admin");

        // Initialize MongoDB connection
        this.mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase database = mongoClient.getDatabase("ocr_results_db");
        this.ocrResultCollection = database.getCollection("ocr_results");

    }

    public void startProcess() {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
            for (ConsumerRecord<String, String> record : records) {
                String documentId = record.value();
                // Forward document ID to OCR service
                producer.send(new ProducerRecord<>("doc_topic", documentId));
                System.out.println("Document ID forwarded to OCR topic: " + documentId);
            }

            // Watch MongoDB for OCR results and process them
            ocrResultCollection.watch().forEach((ChangeStreamDocument<Document> change) -> {
                Document document = change.getFullDocument();
                String documentId = document.getString("document_id");
                try {
                    if (documentId != null) {
                        LoanData loanData = getLoanData(documentId);
                        BankTransactionData bankTransactionData = getBankTransactionData(documentId);
                        String ocrResult = getOcrResult(documentId);

                        if (loanData != null && bankTransactionData != null && ocrResult != null) {
                            int initialScore = calculateInitialScore(loanData, bankTransactionData, ocrResult);
                            System.out.println("Initial score calculated for Document ID " + documentId + ": " + initialScore);
                        } else {
                            System.out.println("Data missing for Document ID " + documentId);
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
    }

    private LoanData getLoanData(String documentId) throws SQLException {
        PreparedStatement statement = loanDatabaseConnection.prepareStatement("SELECT * FROM loans WHERE document_id = ?");
        statement.setString(1, documentId);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            return new LoanData(resultSet.getString("document_id"), resultSet.getDouble("amount"), resultSet.getInt("term"));
        }
        return null;
    }

    private BankTransactionData getBankTransactionData(String documentId) throws SQLException {
        PreparedStatement statement = bankTransactionDatabaseConnection.prepareStatement("SELECT * FROM transactions WHERE document_id = ?");
        statement.setString(1, documentId);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            return new BankTransactionData(resultSet.getString("document_id"), resultSet.getDouble("amount"));
        }
        return null;
    }

    private String getOcrResult(String documentId) {
        Document query = new Document("document_id", documentId);
        Document result = ocrResultCollection.find(query).first();
        if (result != null) {
            return result.getString("ocr_result");
        }
        return null;
    }

    private int calculateInitialScore(LoanData loanData, BankTransactionData bankTransactionData, String ocrResult) {
        // Simplified scoring logic based on available data
        // This is just an example; the actual calculation would likely be more complex
        return 100; // Placeholder score
    }
}
