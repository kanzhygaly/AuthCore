/**
 * Data Transfer Object for "message" information
 */
package kz.ya.authcore.dto;

/**
 *
 * @author YERLAN
 */
public class Message {
    
    private String type;
    private String sequenceId;
    private Data data;

    public Message(String sequenceId) {
        this.sequenceId = sequenceId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSequenceId() {
        return sequenceId;
    }

    public void setSequenceId(String sequenceId) {
        this.sequenceId = sequenceId;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}
