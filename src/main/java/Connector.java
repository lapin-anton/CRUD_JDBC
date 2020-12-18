import java.io.Closeable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Connector implements Closeable {

    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    public Connector(Socket socket) throws IOException {
        this.socket = socket;
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());
    }

    // то, что отправляется со стороны клиента (запрос)
    public void clientSend(Order order) throws IOException {
        synchronized (out) {
            out.writeObject(order);
        }
    }
    // то, что получает клиент обратно (результат запроса)
    public Result clientReceive() throws IOException, ClassNotFoundException {
        Result result;
        synchronized (in) {
            result = (Result) in.readObject();
        }
        return result;
    }

    // то, что отправляется со стороны сервера (результат запроса)
    public void serverSend(Result result) throws IOException {
        synchronized (out) {
            out.writeObject(result);
        }
    }
    // то, что получает сервер (запрос)
    public Order serverReceive() throws IOException, ClassNotFoundException {
        Order order;
        synchronized (in) {
            order = (Order) in.readObject();
        }
        return order;
    }

    @Override
    public void close() throws IOException {
        this.socket.close();
        this.in.close();
        this.out.close();
    }
}
