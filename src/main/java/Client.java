import java.util.HashMap;

public class Client {

    private static HashMap<Integer, Command> commands = new HashMap<>();

    static {
        commands.put(CommandType.CREATE.ordinal(), new CreateCommand());
        commands.put(CommandType.READ.ordinal(), new ReadCommand());
        commands.put(CommandType.UPDATE.ordinal(), new UpdateCommand());
        commands.put(CommandType.DELETE.ordinal(), new DeleteCommand());
    }

    public static void main(String[] args) {
        Settings s = getSettings();
        ConsoleHelper.writeMessage("Вас приветствует CRUD-приложение");
        while(true) {
            ConsoleHelper.writeMessage("Пожалуйста, выберите действие (укажите его порядковый номер):");
            ConsoleHelper.writeMessage(String.format("%d Создать новую запись", CommandType.CREATE.ordinal()));
            ConsoleHelper.writeMessage(String.format("%d Найти запись", CommandType.READ.ordinal()));
            ConsoleHelper.writeMessage(String.format("%d Обновить существующую запись", CommandType.UPDATE.ordinal()));
            ConsoleHelper.writeMessage(String.format("%d Удалить запись", CommandType.DELETE.ordinal()));
            ConsoleHelper.writeMessage(String.format("%s Выйти", CommandType.NONE.ordinal()));
            int answer = ConsoleHelper.readInt();
            if(answer == CommandType.NONE.ordinal()) {
                ConsoleHelper.sayGoodBuy();
                break;
            }
            if (commands.containsKey(answer)) {
                commands.get(answer).execute(s);
            }
        }
    }

    private static Settings getSettings() {
        Settings s = null;
        try {
            s = new Settings();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }
}
