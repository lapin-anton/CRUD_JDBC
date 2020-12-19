import java.io.Serializable;

public enum CommandType implements Serializable {
    SINGLE_READ,
    CREATE,
    READ,
    UPDATE,
    DELETE
}
