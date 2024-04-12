import taskmanager.*;
import taskpackage.*;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();

        Task taskOne = new Task("First", "...", Status.NEW);
        Task taskTwo = new Task("Second", "...", Status.IN_PROGRESS);

        Epic epic = new Epic("Epic");
        Epic epicEmpty = new Epic("EpicEmpty");

        Subtask subtaskOne = new Subtask("SubOne", "...",Status.NEW,2);
        Subtask subtaskTwo = new Subtask("SubTwo", "...",Status.IN_PROGRESS,2);
        Subtask subtaskThree = new Subtask("SubThree", "...",Status.DONE,2);

        taskManager.addTask(taskOne);
        taskManager.addTask(taskTwo);

        taskManager.addEpic(epic);
        taskManager.addEpic(epicEmpty);

        taskManager.addSubtask(subtaskOne);
        taskManager.addSubtask(subtaskTwo);
        taskManager.addSubtask(subtaskThree);

        //Тесты получения истории
        taskManager.getTask(taskOne.getId());
        taskManager.getTask(taskTwo.getId());
        System.out.println(taskManager.getHistory());

        taskManager.getTask(taskOne.getId());
        System.out.println(taskManager.getHistory());

        taskManager.getEpic(epic.getId());
        taskManager.getTask(taskTwo.getId());
        System.out.println(taskManager.getHistory());

        taskManager.getSubtask(subtaskOne.getId());
        taskManager.getSubtask(subtaskThree.getId());
        System.out.println(taskManager.getHistory());

        taskManager.removeTask(taskOne.getId());
        System.out.println(taskManager.getHistory());

        taskManager.removeSubtask(subtaskThree);
        System.out.println(taskManager.getHistory());

        taskManager.removeEpic(epic.getId());
        System.out.println(taskManager.getHistory());
    }
}
