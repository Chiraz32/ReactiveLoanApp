import microservices.CommercialService;
import microservices.DataUploadService;
import microservices.OcrMicroservice;

import java.sql.SQLException;
import java.util.Properties;

public class LoanApplication {
    public static void main(String[] args) throws SQLException {
        // Initialize Kafka properties
        Properties kafkaProps = new Properties();
        kafkaProps.put("bootstrap.servers", "localhost:9092");
        kafkaProps.put("group.id", "loan-application-group");
        kafkaProps.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        kafkaProps.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        // Initialize microservices
        DataUploadService dataUploadService = new DataUploadService("doc-bucket");
        CommercialService commercialService = new CommercialService(kafkaProps);
        OcrMicroservice ocrMicroservice = new OcrMicroservice(kafkaProps);

        // Upload document
        dataUploadService.uploadDocument("path/to/document");

        commercialService.startProcess();
        ocrMicroservice.performOcr();
    }
}

