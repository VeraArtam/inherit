import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TaskTest {

    @Test
    public void testSimpleTaskMatchesWhenTitleContainsQuery() {
        SimpleTask task = new SimpleTask(1, "Купить молоко и хлеб");

        Assertions.assertTrue(task.matches("молоко"));
        Assertions.assertTrue(task.matches("хлеб"));
        Assertions.assertTrue(task.matches("Купить"));
    }

    @Test
    public void testSimpleTaskMatchesWhenTitleDoesNotContainQuery() {
        SimpleTask task = new SimpleTask(2, "Сделать домашнее задание");

        Assertions.assertFalse(task.matches("молоко"));
        Assertions.assertFalse(task.matches("работа"));
        Assertions.assertFalse(task.matches("погулять"));
    }

    @Test
    public void testSimpleTaskMatchesWithEmptyQuery() {
        SimpleTask task = new SimpleTask(3, "Позвонить маме");

        Assertions.assertTrue(task.matches(""));
    }

    @Test
    public void testSimpleTaskMatchesWithCaseSensitiveQuery() {
        SimpleTask task = new SimpleTask(4, "Java программирование");

        Assertions.assertTrue(task.matches("Java"));
        Assertions.assertFalse(task.matches("java")); // содержит ли "Java" подстроку "java"? Нет
    }

    @Test
    public void testEpicMatchesWhenSubtaskContainsQuery() {
        String[] subtasks = {"Написать код", "Протестировать", "Задеплоить"};
        Epic epic = new Epic(1, subtasks);

        Assertions.assertTrue(epic.matches("Написать"));
        Assertions.assertTrue(epic.matches("Протестировать"));
        Assertions.assertTrue(epic.matches("Задеплоить"));
        Assertions.assertTrue(epic.matches("код"));
    }

    @Test
    public void testEpicMatchesWhenNoSubtaskContainsQuery() {
        String[] subtasks = {"Покушать", "Поспать", "Погулять"};
        Epic epic = new Epic(2, subtasks);

        Assertions.assertFalse(epic.matches("работа"));
        Assertions.assertFalse(epic.matches("программирование"));
        Assertions.assertFalse(epic.matches("учеба"));
    }

    @Test
    public void testEpicMatchesWithEmptySubtasks() {
        String[] subtasks = {};
        Epic epic = new Epic(3, subtasks);

        Assertions.assertFalse(epic.matches("что-то"));
    }

    @Test
    public void testEpicMatchesWithEmptyQuery() {
        String[] subtasks = {"Задача 1", "Задача 2"};
        Epic epic = new Epic(4, subtasks);

        Assertions.assertTrue(epic.matches(""));
    }

    @Test
    public void testMeetingMatchesWhenTopicContainsQuery() {
        Meeting meeting = new Meeting(1, "Еженедельное совещание", "Проект А", "10:00");

        Assertions.assertTrue(meeting.matches("Еженедельное"));
        Assertions.assertTrue(meeting.matches("совещание"));
        Assertions.assertTrue(meeting.matches("Еженедельное совещание"));
    }

    @Test
    public void testMeetingMatchesWhenProjectContainsQuery() {
        Meeting meeting = new Meeting(2, "Планирование", "Проект Бета", "14:30");

        Assertions.assertTrue(meeting.matches("Проект"));
        Assertions.assertTrue(meeting.matches("Бета"));
        Assertions.assertTrue(meeting.matches("Проект Бета"));
    }

    @Test
    public void testMeetingMatchesWhenTopicAndProjectContainQuery() {
        Meeting meeting = new Meeting(3, "Ретроспектива", "Проект Гамма", "16:00");

        Assertions.assertTrue(meeting.matches("Ретроспектива"));
        Assertions.assertTrue(meeting.matches("Гамма"));
    }

    @Test
    public void testMeetingMatchesWhenNeitherTopicNorProjectContainsQuery() {
        Meeting meeting = new Meeting(4, "Демо", "Проект Дельта", "12:00");

        Assertions.assertFalse(meeting.matches("планирование"));
        Assertions.assertFalse(meeting.matches("еженедельное"));
        Assertions.assertFalse(meeting.matches("несуществующий запрос"));
    }

    @Test
    public void testMeetingMatchesWithEmptyQuery() {
        Meeting meeting = new Meeting(5, "Встреча", "Проект", "09:00");

        Assertions.assertTrue(meeting.matches("")); // пустая строка содержится в любой строке
    }

    @Test
    public void testMeetingMatchesStartFieldIsNotUsed() {
        Meeting meeting = new Meeting(6, "Обсуждение", "Проект Икс", "15:00");

        Assertions.assertFalse(meeting.matches("15:00"));
        Assertions.assertFalse(meeting.matches("15"));
    }

    @Test
    public void testMeetingMatchesWithCaseSensitiveQuery() {
        Meeting meeting = new Meeting(7, "Важная встреча", "Проект Альфа", "11:00");

        Assertions.assertTrue(meeting.matches("Важная"));
        Assertions.assertFalse(meeting.matches("важная")); // чувствительность к регистру
    }

    @Test
    public void testPolymorphicMatches() {
        Task[] tasks = {
                new SimpleTask(1, "Купить продукты"),
                new Epic(2, new String[]{"Задача 1", "Задача 2"}),
                new Meeting(3, "Совещание", "Проект", "10:00")
        };

        Assertions.assertTrue(tasks[0].matches("продукты"));
        Assertions.assertFalse(tasks[0].matches("работа"));

        Assertions.assertTrue(tasks[1].matches("Задача"));
        Assertions.assertFalse(tasks[1].matches("что-то"));

        Assertions.assertTrue(tasks[2].matches("Совещание"));
        Assertions.assertTrue(tasks[2].matches("Проект"));
        Assertions.assertFalse(tasks[2].matches("10:00"));
    }

    @Test
    public void testEpicGetSubtasks() {
        String[] subtasks = {"Задача 1", "Задача 2"};
        Epic epic = new Epic(4, subtasks);
        String[] actual = epic.getSubtasks();

        Assertions.assertArrayEquals(subtasks, actual);
    }

    @Test
    void getSubtasks_shouldReturnSameArrayReference() {
        // Arrange
        String[] subtasks = {"Task1", "Task2"};
        Epic epic = new Epic(1, subtasks);

        // Act
        String[] returnedSubtasks = epic.getSubtasks();

        // Assert - проверяем, что возвращается та же ссылка на массив
        Assertions.assertSame(subtasks, returnedSubtasks);
    }


    @Test
    void getSubtasks_shouldReturnSubtasksArray() {
        // Arrange
        String[] subtasks = {"Test1", "Test2"};
        Epic epic = new Epic(1, subtasks);

        // Act
        String[] result = epic.getSubtasks();

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.length);
        Assertions.assertArrayEquals(subtasks, result);
    }

    @Test
    void getSubtasks_shouldReturnSubtasks() {
        // Arrange
        String[] expected = {"Task1", "Task2"};
        Epic epic = new Epic(1, expected);

        String[] actual = epic.getSubtasks();

        Assertions.assertArrayEquals(expected, actual);
    }

    @Test
        void getTitle_shouldReturnTitle() {
            // Arrange
            String expectedTitle = "Купить молоко";
            SimpleTask task = new SimpleTask(1, expectedTitle);

            // Act
            String actualTitle = task.getTitle();

            // Assert
            Assertions.assertEquals(expectedTitle, actualTitle);
        }

    @Test
    void getters_shouldReturnCorrectValues() {
        // Arrange
        String topic = "Планирование";
        String project = "Новый проект";
        String start = "15:30";
        Meeting meeting = new Meeting(1, topic, project, start);

        // Act & Assert
        Assertions.assertEquals(topic, meeting.getTopic());
        Assertions.assertEquals(project, meeting.getProject());
        Assertions.assertEquals(start, meeting.getStart());
    }

    @Test
    void getId_shouldReturnId() {

        Task task = new Task(5);

        int id = task.getId();

        Assertions.assertEquals(5, id);
    }

    @Test
    void equals_shouldReturnTrue_whenSameObject() {
        Task task = new Task(1);

        boolean result = task.equals(task);

        Assertions.assertTrue(result);
    }

    @Test
    void equals_shouldReturnFalse_whenNull() {
        Task task = new Task(1);

        boolean result = task.equals(null);

        Assertions.assertFalse(result);
    }

    @Test
    void equals_shouldReturnFalse_whenDifferentClass() {
        Task task = new Task(1);
        String notTask = "string";

        boolean result = task.equals(notTask);

        Assertions.assertFalse(result);
    }

    @Test
    void equals_shouldReturnTrue_whenSameId() {
        Task task1 = new Task(1);
        Task task2 = new Task(1);

        boolean result = task1.equals(task2);

        Assertions.assertTrue(result);
    }

    @Test
    void equals_shouldReturnFalse_whenDifferentId() {
        Task task1 = new Task(1);
        Task task2 = new Task(2);

        boolean result = task1.equals(task2);

        Assertions.assertFalse(result);
    }

    @Test
    void hashCode_shouldReturnSameHashForSameId() {
        Task task1 = new Task(10);
        Task task2 = new Task(10);

        int hash1 = task1.hashCode();
        int hash2 = task2.hashCode();

        Assertions.assertEquals(hash1, hash2);
    }

    @Test
    void hashCode_shouldReturnDifferentHashForDifferentId() {
        Task task1 = new Task(10);
        Task task2 = new Task(20);

        int hash1 = task1.hashCode();
        int hash2 = task2.hashCode();

        Assertions.assertNotEquals(hash1, hash2);
    }

    @Test
    void matches_shouldReturnFalse() {
        Task task = new Task(1);

        boolean result = task.matches("anything");

        Assertions.assertFalse(result);
    }
    }

