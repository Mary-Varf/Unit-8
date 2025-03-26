package src.UI;

import javafx.scene.control.TextField;

public class StyledTextField extends TextField {

    public StyledTextField(String promptText) {
        super();
        setPromptText(promptText);
        setDefaultStyle();
        setHoverEffect();
        setFocusEffect();
    }

    private void setDefaultStyle() {
        setStyle(
            "-fx-border-color: #6a0dad; " +
            "-fx-border-width: 2px; " +
            "-fx-border-radius: 5px; " +
            "-fx-background-radius: 5px; " +
            "-fx-padding: 6px; " +
            "-fx-font-size: 14px; " +
            "-fx-text-fill: black;"
        );
    }

    private void setHoverEffect() {
        setOnMouseEntered(e -> setStyle(
            "-fx-border-color: #8000ff; " +
            "-fx-border-width: 2px; " +
            "-fx-border-radius: 5px; " +
            "-fx-background-radius: 5px; " +
            "-fx-padding: 6px; " +
            "-fx-font-size: 14px; " +
            "-fx-text-fill: black; " +
            "-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.2), 5, 0, 0, 2);"
        ));

        setOnMouseExited(e -> setDefaultStyle());
    }

    private void setFocusEffect() {
        setOnMouseClicked(e -> setStyle(
            "-fx-border-color: #5a009a; " +
            "-fx-border-width: 2px; " +
            "-fx-border-radius: 5px; " +
            "-fx-background-radius: 5px; " +
            "-fx-padding: 6px; " +
            "-fx-font-size: 14px; " +
            "-fx-text-fill: black; " +
            "-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.4), 10, 0, 0, 2);"
        ));

        setOnKeyTyped(e -> setStyle(
            "-fx-border-color: #5a009a; " +
            "-fx-border-width: 2px; " +
            "-fx-border-radius: 5px; " +
            "-fx-background-radius: 5px; " +
            "-fx-padding: 6px; " +
            "-fx-font-size: 14px; " +
            "-fx-text-fill: black; " +
            "-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.4), 10, 0, 0, 2);"
        ));

        setOnMouseExited(e -> setDefaultStyle());
    }
}
