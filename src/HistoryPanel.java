package src;

import javafx.scene.control.TextArea;
import java.util.ArrayList;
import java.util.List;

public class HistoryPanel {
    private TextArea historyArea;
    private List<String> searchHistory;

    public HistoryPanel() {
        historyArea = new TextArea();
        historyArea.setEditable(false);
        historyArea.setPrefHeight(100);
        searchHistory = new ArrayList<>();
        
        historyArea.setStyle(
            "-fx-background-color: white; " +
            "-fx-border-radius: 5px; " +
            "-fx-border-color: white; "
        );
    }

    public TextArea getHistoryArea() {
        return historyArea;
    }

    public void addEntry(String entry) {
        searchHistory.add(entry);
        historyArea.setText("History:\n" + String.join("\n", searchHistory));
    }
}
