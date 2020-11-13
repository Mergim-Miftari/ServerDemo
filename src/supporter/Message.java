package supporter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * An Object with an uniq ID.
 */
public class Message implements Serializable {
    /** id of the message. This ID is unique for the sender. */
    private int id;

    /** static counter to generate unique ids within the same process. */
    private static int idcounter;

    /** logger */
    private static final transient Logger logger = LoggerFactory.getLogger(Message.class.getName());

    /**
     * Constructor assigning unique message number to id.
     */
    public Message() {
        super();
        id = ++idcounter;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        String value = "[supporter.Message] ";
        value += "id = " + id;
        return value;
    }
}

