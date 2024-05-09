package microservices;

import java.util.Properties;

public class OcrMicroservice {
    private KafkaConsumer<String, String> consumer;
    private MongoClient mongoClient;
    private MongoCollection<Document> ocrResultCollection;

    public OcrMicroservice(Properties kafkaProps) {
        // Initialize Kafka consumer
        this.consumer = new KafkaConsumer<>(kafkaProps);
        consumer.subscribe(Collections.singletonList("doc_topic"));

        // Initialize MongoDB connection
        this.mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase database = mongoClient.getDatabase("ocr_results_db");
        this.ocrResultCollection = database.getCollection("ocr_results");
    }

    public void performOcr() {
        ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
        for (ConsumerRecord<String, String> record : records) {
            String documentId = record.value();

            // Perform OCR logic
            String ocrResult = "OCR_RESULT"; // Mocked OCR result

            // Store OCR result in MongoDB
            Document document = new Document("document_id", documentId).append("ocr_result", ocrResult);
            ocrResultCollection.insertOne(document);
        }
    }
}
