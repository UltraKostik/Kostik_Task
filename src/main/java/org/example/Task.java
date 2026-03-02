package org.example;

import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;
import org.json.JSONObject;

public class Task {

    public void addTask(Scanner scanner) {
        try {
            System.out.println("Введите название заметки:");
            String name_file = scanner.nextLine();
            System.out.println("Введите текст заметки:");
            String text_file = scanner.nextLine();

            JSONObject json = new JSONObject();
            json.put("fileName", name_file);
            json.put("content", text_file);

            File folder = new File("notes");
            folder.mkdirs();

            File targetFile = new File(folder, name_file + ".json");
            int counter = 1;

            while (targetFile.exists()) {
                String newName = name_file + " (" + counter + ")";
                targetFile = new File(folder, newName + ".json");
                counter++;
            }

            if (counter > 1) {
                String actualName = targetFile.getName();
                json.put("fileName", actualName);
            }

            try (FileWriter file = new FileWriter(targetFile)) {
                file.write(json.toString());
                System.out.println("Заметка успешно сохранена в файл: notes/" + targetFile.getName());
            }

        } catch (Exception e) {
            System.out.println("Ошибка при добавлении заметки: " + e.getMessage());
        }
    }

    public void viewTask(Scanner scanner) {
        try {
            File[] files = getNotesList();
            if (files == null) return;

            int choice = getUserChoice(scanner, files.length);
            if (choice == 0) return;

            String content = new String(java.nio.file.Files.readAllBytes(files[choice - 1].toPath()));
            JSONObject json = new JSONObject(content);

            System.out.println("Название файла: " + json.getString("fileName"));
            System.out.println("Контент файла:\n" + json.getString("content"));

        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    public void deleteTask(Scanner scanner) {
        try {
            File[] files = getNotesList();
            if (files == null) return;

            int choice = getUserChoice(scanner, files.length);
            if (choice == 0) return;

            File file = files[choice - 1];
            String name_file = file.getName();

            if (file.delete()) {
                System.out.println("Файл " + name_file + " успешно удален.");
            } else {
                System.out.println("Файл " + name_file + " не был удален.");
            }

        } catch (Exception e) {
            System.out.println("Ошибка при удалении заметки: " + e.getMessage());
        }
    }

    public void editTask(Scanner scanner) {
        try {
            File[] files = getNotesList();
            if (files == null) return;

            int choice = getUserChoice(scanner, files.length);
            if (choice == 0) return;

            File file = files[choice - 1];
            String content = new String(java.nio.file.Files.readAllBytes(file.toPath()));
            JSONObject json = new JSONObject(content);

            System.out.println("Текущий текст: " + json.getString("content"));
            System.out.println("Введите новый текст:");
            String newText = scanner.nextLine();

            if (!newText.isEmpty()) {
                json.put("content", newText);
                try (FileWriter fw = new FileWriter(file)) {
                    fw.write(json.toString());
                    System.out.println("Заметка отредактирована!");
                }
            } else {
                System.out.println("Редактирование отменено");
            }

        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    ///Вспомогательный метод (Я попытался в час ночи поэкперментировать над кодом и максимально его сжать)///

    private File[] getNotesList() {
        File[] files = new File("notes").listFiles((dir, name) -> name.endsWith(".json"));

        if (files == null || files.length == 0) {
            System.out.println("Нет заметок");
            return null;
        }

        System.out.println("Список заметок:");
        for (int i = 0; i < files.length; i++)
            System.out.println((i + 1) + ". " + files[i].getName());
        return files;
    }

    private int getUserChoice(Scanner scanner, int max) {
        System.out.print("Номер (0-выход): ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        if (choice == 0) return 0;
        if (choice < 1 || choice > max) {
            System.out.println("Неверный номер");
            return 0;
        }
        return choice;
    }
}