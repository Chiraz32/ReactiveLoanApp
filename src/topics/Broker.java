package topics;

import java.util.HashMap;
import java.util.Map;

public class Broker {
    private Map<String, Topic> topics;

    public Broker() {
        this.topics = new HashMap<>();
    }


    public Topic getTopic(String topicName) {
        return topics.get(topicName);
    }

    public void removeTopic(String topicName) {
        topics.remove(topicName);
    }
    public void addTopic(String topicName,Topic topic) {
        topics.put(topicName, topic);
    }
}