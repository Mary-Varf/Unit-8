package src.UI;

import javafx.scene.control.Button;

public class StyledButton extends Button {

    public StyledButton(String text) {
        super(text);
        setDefaultStyle();
        setHoverEffect();
        setClickEffect();
    }

    private void setDefaultStyle() {
        setStyle(
            "-fx-background-color: #6a0dad; " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 14px; " +
            "-fx-font-weight: bold; " +
            "-fx-padding: 8px 16px; " +
            "-fx-border-radius: 5px; " +
            "-fx-background-radius: 5px; " +
            "-fx-cursor: hand;"
        );
    }

    private void setHoverEffect() {
        setOnMouseEntered(e -> setStyle(
            "-fx-background-color: #8000ff; " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 14px; " +
            "-fx-font-weight: bold; " +
            "-fx-padding: 8px 16px; " +
            "-fx-border-radius: 5px; " +
            "-fx-background-radius: 5px; " +
            "-fx-cursor: hand;"
        ));

        setOnMouseExited(e -> setDefaultStyle());
    }

    private void setClickEffect() {
        setOnMousePressed(e -> setStyle(
            "-fx-background-color: #5a009a; " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 14px; " +
            "-fx-font-weight: bold; " +
            "-fx-padding: 8px 16px; " +
            "-fx-border-radius: 5px; " +
            "-fx-background-radius: 5px; " +
            "-fx-cursor: hand; " +
            "-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.4), 10, 0, 0, 2);"
        ));

        setOnMouseReleased(e -> setDefaultStyle());
    }
}
