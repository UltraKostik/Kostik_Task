import org.example.Task;
import org.junit.Assert;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.util.Scanner;

public class MainTest {

    @Test
    public void testFolderCreated() {
        new File("notes").mkdirs();
        Assert.assertTrue(new File("notes").exists());
    }

    @Test
    public void fileExistsAndDelete() {
        Task task = new Task();

        System.setIn(new ByteArrayInputStream("TestTitle\nTestContent\n".getBytes()));
        task.addTask(new Scanner(System.in));
        Assert.assertTrue("Файл TestTitle должен быть создан", new File("notes/TestTitle.json").exists());

        System.setIn(new ByteArrayInputStream("1\n".getBytes()));
        task.deleteTask(new Scanner(System.in));
        Assert.assertFalse("Файл TestTitle должен быть удален", new File("notes/TestTitle.json").exists());

    }

    @Test
    public void testAddDuplicateTitle() {
        Task task = new Task();
        System.setIn(new ByteArrayInputStream("TestTitle\nTestContent\n".getBytes()));
        task.addTask(new Scanner(System.in));
        System.setIn(new ByteArrayInputStream("TestTitle\nTestContent\n".getBytes()));
        task.addTask(new Scanner(System.in));

        File file1 = new File("notes/TestTitle.json");
        File file2 = new File("notes/TestTitle (1).json");
        Assert.assertTrue("Файл TestTitle должен быть создан", file1.exists());
        Assert.assertTrue("Файл TestTitle (1) должен быть создан", file2.exists());
        file1.delete();
        file2.delete();
    }

    @Test
    public void testAddTaskWithEmptyTitleAndContent(){
        Task task = new Task();
        System.setIn(new ByteArrayInputStream("\n\n".getBytes()));
        task.addTask(new Scanner(System.in));

        File file = new File("notes/.json");
        Assert.assertTrue("Пустой файл .json должен быть создан", file.exists());
        file.delete();
    }

    @Test
    public void testCancelDelete() {
        Task task = new Task();
        System.setIn(new ByteArrayInputStream("TestTitle\nTestContent\n".getBytes()));
        task.addTask(new Scanner(System.in));

        File file = new File("notes/TestTitle.json");
        Assert.assertTrue("Файл TestTitle должен создаться", file.exists());
        System.setIn(new ByteArrayInputStream("0\n".getBytes()));
        task.deleteTask(new Scanner(System.in));
        
        Assert.assertTrue("Файл TestTitle должен остаться после отмены", file.exists());
        file.delete();
    }
    
    @Test
    public void testViewTask() {
        Task task = new Task();
        System.setIn(new ByteArrayInputStream("TestTitle\nTestContent\n".getBytes()));
        task.addTask(new Scanner(System.in));

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        System.setIn(new ByteArrayInputStream("1\n".getBytes()));
        task.viewTask(new Scanner(System.in));

        String output = out.toString();
        Assert.assertTrue("Должен показать название файла", output.contains("TestTitle"));
        Assert.assertTrue("Должен показать контент файла", output.contains("TestContent"));

        new File("notes/TestTitle.json").delete();
    }

    @Test
    public void testWrongChoice(){
        Task task = new Task();
        System.setIn(new ByteArrayInputStream("TestTitle\nTestContent\n".getBytes()));
        task.addTask(new Scanner(System.in));

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        System.setIn(new ByteArrayInputStream("999\n".getBytes()));
        task.deleteTask(new Scanner(System.in));
        Assert.assertTrue(out.toString().contains("Неверный номер"));
        new File("notes/TestTitle.json").delete();
    }

    @Test
    public void testNegativeNumber(){
        Task task = new Task();
        System.setIn(new ByteArrayInputStream("TestTitle\nTestContent\n".getBytes()));
        task.addTask(new Scanner(System.in));

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        System.setIn(new ByteArrayInputStream("-5\n".getBytes()));
        task.deleteTask(new Scanner(System.in));
        Assert.assertTrue(out.toString().contains("Неверный номер"));
        new File("notes/TestTitle.json").delete();
    }
}