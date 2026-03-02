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
            File[] files = new File("notes").listFiles((dir,name) -> name.endsWith(".json"));

            if (files == null || files.length == 0) {
                System.out.println("Нет заметок");
                return;
            }

            for (int i = 0; i < files.length; i++)
                System.out.println((i + 1) + ". " + files[i].getName());

            System.out.print("Номер (0-выход): ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 0) return;
            if (choice < 1 || choice > files.length) {
                System.out.println("Неверный номер");
                return;
            }

            String content = new String(java.nio.file.Files.readAllBytes(files[choice - 1].toPath()));
            JSONObject json = new JSONObject(content);

            System.out.println("Название файла: " + json.getString("fileName"));
            System.out.println("Контент файла:\n" +json.getString("content"));

        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    public void deleteTask(Scanner scanner) {
        try {
            System.out.println("Введите название заметки:");
            String name_file = scanner.nextLine();
            File file = new File("notes", name_file + ".json");

            if (file.exists()) {
                if (file.delete()) {
                    System.out.println("Файл " + name_file + ".json успешно удален.");
                } else {
                    System.out.println("Файл " + name_file + ".json не был удален.");
                }
            } else {
                System.out.println("Файл " + name_file + ".json не найден в папке notes.");
            }

        } catch (Exception e) {
            System.out.println("Ошибка при удалении заметки: " + e.getMessage());
        }
    }
}