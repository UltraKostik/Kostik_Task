package org.example;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Task myTask = new Task();

        while (true){
            System.out.print("1. Показать задачи\n2. Добавить задачу\n3. Удалить задачу\n4. Редактировать задачу\n5. Выход: ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    System.out.println("Список задач:");
                    myTask.viewTask(scanner);
                    break;
                case 2:
                    System.out.println("Добавление...");
                    myTask.addTask(scanner);
                    break;
                case 3:
                    System.out.println("Удаление...");
                    myTask.deleteTask(scanner);
                    break;
                case 4:
                    System.out.println("Редактирование...");
                    myTask.editTask(scanner);
                    break;
                case 5:
                    System.out.println("Пока-пока!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Неверные данные!");
            }
        }

    }
}