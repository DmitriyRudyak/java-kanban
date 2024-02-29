import TaskManager.*;
import TaskPackage.*;

public class Main {
    public static void main(String[] args) {
        Managers manager = new Managers();
        TaskManager tsk = manager.getDefault();
    }
}
