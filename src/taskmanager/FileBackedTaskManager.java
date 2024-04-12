package taskmanager;
import taskpackage.*;

import java.util.ArrayList;
import java.util.List;

public class FileBackedTaskManager implements TaskManager{
	@Override
	public Task addTask(Task task) {
		return null;
	}

	@Override
	public ArrayList<Task> taskList() {
		return null;
	}

	@Override
	public void removeTask(int id) {

	}

	@Override
	public Task getTask(int id) {
		return null;
	}

	@Override
	public Task updateTask(Task newTask) {
		return null;
	}

	@Override
	public void deleteTaskList() {

	}

	@Override
	public Epic addEpic(Epic epic) {
		return null;
	}

	@Override
	public ArrayList<Epic> epicList() {
		return null;
	}

	@Override
	public void removeEpic(int id) {

	}

	@Override
	public Task getEpic(int id) {
		return null;
	}

	@Override
	public Task updateEpic(Epic newEpic) {
		return null;
	}

	@Override
	public void deleteEpicList() {

	}

	@Override
	public Subtask addSubtask(Subtask subtask) {
		return null;
	}

	@Override
	public ArrayList<Subtask> subtaskList(Epic epic) {
		return null;
	}

	@Override
	public ArrayList<Subtask> subtaskList() {
		return null;
	}

	@Override
	public void removeSubtask(Subtask subtask) {

	}

	@Override
	public Subtask getSubtask(int id) {
		return null;
	}

	@Override
	public Subtask updateSubtask(Subtask newSubtask) {
		return null;
	}

	@Override
	public void deleteSubtaskList() {

	}

	@Override
	public List<Task> getHistory() {
		return List.of();
	}

	@Override
	public void setEpicStatus(Epic epic) {

	}
}
