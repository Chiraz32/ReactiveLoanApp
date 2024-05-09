package models;

public class BankTransactionData {
    private String documentId;
    private double amount;

    public BankTransactionData(String documentId, double amount) {
        this.documentId = documentId;
        this.amount = amount;
    }

    // Getters and setters

    @Override
    public String toString() {
        return "BankTransactionData{" +
                "documentId='" + documentId + '\'' +
                ", amount=" + amount +
                '}';
    }
}