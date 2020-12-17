public class ExceptionHandler {
    public static void log(Throwable e) {
        System.out.println(String.format("%s: %s", e.getClass().getSimpleName(), e.getMessage()));
    }
}
