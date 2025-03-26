package src.UI;

import javafx.scene.layout.Pane;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Switcher extends Pane {
    private Rectangle rect;
    private Button leftButton;
    private Button rightButton;
    private boolean isCelsius;
    final int width = 40;
    final int height = 25;

    private Runnable onToggleAction;

    public Switcher(boolean isCelsius, String option1, String option2) {
        this.isCelsius = isCelsius;

        rect = new Rectangle(width * 2, height);
        rect.setFill(Color.TRANSPARENT);
        this.getChildren().add(rect);

        rect.setLayoutX(0);
        rect.setLayoutY(0);
        rect.setStyle("-fx-font-size: 10px;");

        rect.setOnMouseEntered(e -> rect.setFill(Color.rgb(153, 51, 255, 0.4)));
        rect.setOnMouseExited(e -> rect.setFill(Color.TRANSPARENT));

        leftButton = new Button(option1);
        rightButton = new Button(option2);

        leftButton.setLayoutX(0);
        leftButton.setPrefWidth(width);
        rightButton.setLayoutX(width);
        rightButton.setPrefWidth(width);

        setButtonStyle(leftButton, true);
        setButtonStyle(rightButton, false);

        this.getChildren().addAll(leftButton, rightButton);

        leftButton.setOnAction(e -> toggleOption(true));
        rightButton.setOnAction(e -> toggleOption(false));
    }

    private void toggleOption(boolean isLeft) {
        if (isLeft) {
            isCelsius = true;
            setButtonStyle(leftButton, true);
            setButtonStyle(rightButton, false);
        } else {
            isCelsius = false;
            setButtonStyle(leftButton, false);
            setButtonStyle(rightButton, true);
        }
        if (onToggleAction != null) {
            onToggleAction.run();
        }
    }

    private void setButtonStyle(Button button, boolean isActive) {
        if (isActive) {
            button.setStyle(
                "-fx-background-color: #8000ff; " +
                "-fx-text-fill: white; " +
                "-fx-font-size: 10px; " +
                "-fx-border-radius: 5px; " +
                "-fx-background-radius: 5px; " +
                "-fx-cursor: pointer;"         +
                "-fx-pref-height: " + height + "px;"
            ); 
        } else {
            button.setStyle(
                "-fx-background-color: white; " +
                "-fx-text-fill: black; " +     
                "-fx-font-size: 10px; " +
                "-fx-border-radius: 5px; " +
                "-fx-background-radius: 5px; " +
                "-fx-cursor: pointer;"           +
                "-fx-pref-height: " + height + "px;"
            );
        }

        button.setOnMouseExited(e -> setButtonStyle(button, isActive));

        button.setOnMousePressed(e -> button.setStyle(
            "-fx-background-color: #5a009a; " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 10px; " +
            "-fx-border-radius: 5px; " +
            "-fx-background-radius: 5px; " +
            "-fx-cursor: pointer; " +
            "-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.4), 10, 0, 0, 2);" +
            "-fx-pref-height: " + height + "px;"
        ));

        button.setOnMouseReleased(e -> setButtonStyle(button, isActive));
    }

    public boolean isCelsius() {
        return isCelsius;
    }

    public void setOnToggleAction(Runnable listener) {
        this.onToggleAction = listener;
    }
}
