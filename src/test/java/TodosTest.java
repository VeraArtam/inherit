import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TodosTest {

    @Test
    public void shouldAddThreeTasksOfDifferentType() {
        SimpleTask simpleTask = new SimpleTask(5, "Позвонить родителям");

        String[] subtasks = {"Молоко", "Яйца", "Хлеб"};
        Epic epic = new Epic(55, subtasks);

        Meeting meeting = new Meeting(
                555,
                "Выкатка 3й версии приложения",
                "Приложение НетоБанка",
                "Во вторник после обеда"
        );

        Todos todos = new Todos();

        todos.add(simpleTask);
        todos.add(epic);
        todos.add(meeting);

        Task[] expected = {simpleTask, epic, meeting};
        Task[] actual = todos.findAll();
        Assertions.assertArrayEquals(expected, actual);


    }

    private Todos todos;

    @BeforeEach
    public void setUp() {
        todos = new Todos();
    }

    @Test
    public void testSearchShouldFindSimpleTaskByTitle() {
        SimpleTask simpleTask = new SimpleTask(1, "Купить молоко");
        todos.add(simpleTask);

        Task[] result = todos.search("молоко");

        Assertions.assertEquals(1, result.length);
        Assertions.assertEquals(simpleTask, result[0]);
    }

    @Test
    public void testSearchShouldFindEpicBySubtask() {
        String[] subtasks = {"Написать код", "Протестировать", "Задеплоить"};
        Epic epic = new Epic(1, subtasks);
        todos.add(epic);

        Task[] result = todos.search("Протестировать");

        Assertions.assertEquals(1, result.length);
        Assertions.assertEquals(epic, result[0]);
    }

    @Test
    public void testSearchShouldFindMeetingByTopic() {
        Meeting meeting = new Meeting(1, "Еженедельное совещание", "Проект А", "10:00");
        todos.add(meeting);

        Task[] result = todos.search("совещание");

        Assertions.assertEquals(1, result.length);
        Assertions.assertEquals(meeting, result[0]);
    }

    @Test
    public void testSearchShouldFindMeetingByProject() {
        Meeting meeting = new Meeting(1, "Планирование", "Проект Бета", "14:30");
        todos.add(meeting);

        Task[] result = todos.search("Проект Бета");

        Assertions.assertEquals(1, result.length);
        Assertions.assertEquals(meeting, result[0]);
    }


    @Test
    public void testSearchShouldFindMultipleTasks() {
        SimpleTask simpleTask = new SimpleTask(1, "Купить продукты");
        Epic epic = new Epic(2, new String[]{"Купить продукты", "Приготовить ужин"});
        Meeting meeting = new Meeting(3, "Обсуждение продуктов", "Проект", "12:00");

        todos.add(simpleTask);
        todos.add(epic);
        todos.add(meeting);

        Task[] result = todos.search("продукты");

        Assertions.assertEquals(2, result.length);
        Assertions.assertArrayEquals(new Task[]{simpleTask, epic}, result);
    }

    @Test
    public void testSearchShouldFindTasksWithSameQueryInDifferentFields() {
        SimpleTask simpleTask = new SimpleTask(1, "Подготовить презентацию");
        Epic epic = new Epic(2, new String[]{"Подготовить материалы", "Сделать презентацию"});
        Meeting meeting = new Meeting(3, "Презентация проекта", "Презентация", "15:00");

        todos.add(simpleTask);
        todos.add(epic);
        todos.add(meeting);

        Task[] result = todos.search("презентация");

        Assertions.assertEquals(0, result.length);
    }


    @Test
    public void testSearchShouldReturnEmptyArrayWhenNoMatches() {
        SimpleTask simpleTask = new SimpleTask(1, "Купить молоко");
        Epic epic = new Epic(2, new String[]{"Написать код"});
        Meeting meeting = new Meeting(3, "Совещание", "Проект", "10:00");

        todos.add(simpleTask);
        todos.add(epic);
        todos.add(meeting);

        Task[] result = todos.search("несуществующий запрос");

        Assertions.assertEquals(0, result.length);
    }

    @Test
    public void testSearchShouldReturnEmptyArrayWhenTodosEmpty() {
        Task[] result = todos.search("что-то");

        Assertions.assertEquals(0, result.length);
    }


    @Test
    public void testSearchIsCaseSensitive() {
        SimpleTask task = new SimpleTask(1, "Java программирование");
        todos.add(task);

        Task[] resultLowerCase = todos.search("java");
        Task[] resultUpperCase = todos.search("Java");

        Assertions.assertEquals(0, resultLowerCase.length);
        Assertions.assertEquals(1, resultUpperCase.length);
    }


    @Test
    public void testSearchWithEmptyQueryShouldReturnAllTasks() {
        SimpleTask simpleTask = new SimpleTask(1, "Задача 1");
        Epic epic = new Epic(2, new String[]{"Подзадача 1"});
        Meeting meeting = new Meeting(3, "Тема", "Проект", "12:00");

        todos.add(simpleTask);
        todos.add(epic);
        todos.add(meeting);

        Task[] result = todos.search("");

        Assertions.assertEquals(3, result.length);
        Assertions.assertArrayEquals(new Task[]{simpleTask, epic, meeting}, result);
    }


    @Test
    public void testSearchWithNullQueryShouldThrowException() {
        SimpleTask task = new SimpleTask(1, "Задача");
        todos.add(task);

        Assertions.assertThrows(NullPointerException.class, () -> todos.search(null));
    }


    @Test
    public void testSearchShouldPreserveAdditionOrder() {
        SimpleTask task1 = new SimpleTask(1, "А");
        SimpleTask task2 = new SimpleTask(2, "А");
        SimpleTask task3 = new SimpleTask(3, "А");

        todos.add(task1);
        todos.add(task2);
        todos.add(task3);

        Task[] result = todos.search("А");

        Assertions.assertEquals(3, result.length);
        Assertions.assertArrayEquals(new Task[]{task1, task2, task3}, result);
    }

}
