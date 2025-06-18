/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kci.simulator;

/**
 *
 * @author abdur
 */
import java.io.*;

public class GameSaver {
    private static final String SAVE_FILE = "progress.txt";

    public static void save(int gameStage) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(SAVE_FILE))) {
            writer.write(Integer.toString(gameStage));
            System.out.println("Game progress saved: gameStage = " + gameStage);
        } catch (IOException e) {
            System.out.println("Error saving progress: " + e.getMessage());
        }
    }

    public static int load() {
        try (BufferedReader reader = new BufferedReader(new FileReader(SAVE_FILE))) {
            String line = reader.readLine();
            int stage = Integer.parseInt(line);
            System.out.println("Loaded progress: gameStage = " + stage);
            return stage;
        } catch (IOException | NumberFormatException e) {
            System.out.println("No save found. Starting at gameStage = 0");
            return 0;
        }
    }
}
