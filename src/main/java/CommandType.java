import java.io.Serializable;

public enum CommandType implements Serializable {
    NONE,
    CREATE,
    READ,
    UPDATE,
    DELETE,
    SINGLE_READ,
    MULTIPLE_DELETE
}
