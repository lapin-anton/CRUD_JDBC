public interface Command {
    public void execute();
    public boolean isDone();
    public Result getResult();
}
