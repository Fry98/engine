package com.teqtrue.engine.utils;

import java.io.File;
import java.security.SecureRandom;

import com.teqtrue.engine.model.GameMap;
import com.teqtrue.engine.model.KeyMap;
import com.teqtrue.engine.screen.EditorScreen;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class SaveScreen {
    public static void showDialog(EditorScreen parent) {

        KeyMap.clear();

        // GRID PANE SETUP
        GridPane grid = new GridPane();
        grid.setHgap(5);
        grid.setAlignment(Pos.CENTER);
        Scene saveScene = new Scene(grid, 380, 60);
    
        // TEXTBOX
        TextField name = new TextField();
        name.setPromptText("Enter name of the level...");
        name.setPrefColumnCount(20);
        GridPane.setConstraints(name, 0, 0);
        grid.getChildren().add(name);

        // SAVE BUTTON
        Button save = new Button("Save");
        GridPane.setConstraints(save, 1, 0);
        grid.getChildren().add(save);

        // CANCEL BUTTON
        Button cancel = new Button("Cancel");
        GridPane.setConstraints(cancel, 2, 0);
        grid.getChildren().add(cancel);

        // WINDOW INIT
        Stage newWindow = new Stage();
        newWindow.setTitle("Save your level...");
        newWindow.setScene(saveScene);
        newWindow.show();

        // LISTENERS
        newWindow.setOnHiding(e -> {
            parent.restore();
        });

        cancel.setOnMouseClicked(e -> {
            newWindow.hide();
            parent.restore();
        });

        save.setOnMouseClicked(e -> {
            saveMap(name.getText(), parent);
            newWindow.hide();
            parent.restore();
        });
    }

    private static void saveMap(String lvlName, EditorScreen parent) {
        if (lvlName == "") {
            return;
        }
        String filename;
        File folder = new File("src/main/levels");
        File[] files = folder.listFiles();
        while (true) {
            filename = generateFilename() + ".map";
            for (File file : files) {
                if (file.getName() == filename) {
                    continue;
                }
            }
            break;
        }
        GameMap map = parent.getMap();
        map.setName(lvlName);
        FileUtil.saveObject(map, filename);
    }

    private static String generateFilename() {
        String source = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        SecureRandom rnd = new SecureRandom();
        StringBuilder sb = new StringBuilder(10);
        for (int i = 0; i < 10; i++) {
            sb.append(source.charAt(rnd.nextInt(source.length())));
        }
        return sb.toString();
    }
}
