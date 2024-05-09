import topics.Broker;
import topics.Itopic;
import topics.Topic;

public class main {
    Broker broker_1 = new Broker();
    Topic docTopic = new Topic();
    Topic dataUploadTopic = new Topic();
    Topic initialScoreTopic = new Topic();
    Topic decisionTopic = new Topic();

    // Adding topics to the broker
    broker_1.addTopic("docTopic", docTopic);
    broker.addTopic("dataUploadTopic", dataUploadTopic);
    broker.addTopic("initialScoreTopic", initialScoreTopic);
    broker.addTopic("decisionTopic", decisionTopic);
    broker.getTopic()

}
