package topics;

public class Topic implements Itopic {
    private String[] msgs;

    public Topic() {
        this.msgs = new String[0];
    }

    @Override
    public String[] getMessages() {
        return msgs;
    }

    @Override
    public void produce(String msg) {
        String[] newMsgs = new String[msgs.length + 1];
        System.arraycopy(msgs, 0, newMsgs, 0, msgs.length);
        newMsgs[msgs.length] = msg;
        msgs = newMsgs;
    }

    @Override
    public String consume() {
        if (msgs.length == 0) {
            return null;
        }
        String msg = msgs[0];
        String[] newMsgs = new String[msgs.length - 1];
        System.arraycopy(msgs, 1,newMsgs , 0, msgs.length - 1);
        msgs = newMsgs;
        return msg;
    }
}