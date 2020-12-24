package exceptions;

public class IncorrectValueRangeException extends Exception {

    @Override
    public String getMessage() {
        return "Выбран неправильный диапазон. Пожалуйста, проверьте введеные данные.";
    }
}
