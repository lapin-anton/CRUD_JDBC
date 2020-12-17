import java.util.ArrayList;
import java.util.HashMap;

public class Client {

    private static HashMap<Integer, Command> commands = new HashMap<>();

    static {
        commands.put(CommandType.CREATE.ordinal(), new CreateManager());
        commands.put(CommandType.READ.ordinal(), new ReadManager());
        commands.put(CommandType.UPDATE.ordinal(), new UpdateManager());
        commands.put(CommandType.DELETE.ordinal(), new DeleteManager());
    }

    public static void main(String[] args) {
        System.out.println("Вас приветствует CRUD-приложение");
        System.out.println("Пожалуйста, выберите действие (укажите его порядковый номер):");
        System.out.println(String.format("%d Создать новую запись", CommandType.CREATE.ordinal()));
        System.out.println(String.format("%d Найти запись", CommandType.READ.ordinal()));
        System.out.println(String.format("%d Обновить существующую запись", CommandType.UPDATE.ordinal()));
        System.out.println(String.format("%d Удалить запись", CommandType.DELETE.ordinal()));
        System.out.println("5. Выйти");
        int answer = ConsoleHelper.readInt();
        if(commands.containsKey(answer)) {
            commands.get(answer).execute();
        } else {
            ConsoleHelper.sayGoodBuy();
        }
    }


}
