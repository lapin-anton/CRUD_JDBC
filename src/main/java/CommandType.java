import java.io.Serializable;

public enum CommandType implements Serializable {
    NONE,
    CREATE,
    READ,
    UPDATE,
    DELETE,
    SINGLE_READ,
    MULTIPLE_DELETE,
    MULTIPLE_UPDATE,
    AUTHORIZATION,
    ADD_USER,
    READ_USERS,
    UPDATE_USERS, DELETE_USERS
}
