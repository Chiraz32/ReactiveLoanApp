package microservices;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import java.io.File;

public class DataUploadService {
    private KafkaProducer<String, String> producer;
    private AmazonS3 s3Client;
    private final String bucketName = "doc-bucket";

    public DataUploadService(String kafkaProps) {
        this.producer = new KafkaProducer<>(kafkaProps);
        this.s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new DefaultAWSCredentialsProviderChain())
                .withRegion(Regions.US_EAST_1)
                .build();
    }

    public void uploadDocument(String documentPath) {
        try {
            // Upload document to S3 bucket
            File file = new File(documentPath);
            String key = file.getName();
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.length());

            PutObjectRequest request = new PutObjectRequest(bucketName, key, file)
                    .withMetadata(metadata);
            s3Client.putObject(request);

            System.out.println("Document uploaded to S3. Key: " + key);

            // Send document ID to Kafka topic
            String documentId = key; // Assuming document ID is the file name
            producer.send(new ProducerRecord<>("data_upload_topic", documentId));
            System.out.println("Document ID sent to data upload topic.");
        } catch (AmazonServiceException e) {
            e.printStackTrace();
        } catch (SdkClientException e) {
            e.printStackTrace();
        }
    }
}
