package models;

public class LoanData {
    private String documentId;
    private double amount;
    private int term;

    public LoanData(String documentId, double amount, int term) {
        this.documentId = documentId;
        this.amount = amount;
        this.term = term;
    }

    // Getters and setters
    // You can generate these using your IDE or write them manually

    @Override
    public String toString() {
        return "LoanData{" +
                "documentId='" + documentId + '\'' +
                ", amount=" + amount +
                ", term=" + term +
                '}';
    }
}