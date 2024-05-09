package topics;

public interface Itopic {
    String[] getMessages();
    void produce(String message);
    String consume();
}
