package me.wusel.testing.controllers;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import me.wusel.testing.Launch;

public class LoginController {

    public PasswordField tokenField;
    public Button loginButton;
    public Label invalidTokenLabel;

    public void onButtonPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.ENTER)) {
            onLoginButton(new ActionEvent());
            return;
        }
        if (keyEvent.getCode().equals(KeyCode.END)) {
            System.exit(0);
        }
    }

    public void onLoginButton(ActionEvent actionEvent) {
        String token = tokenField.getText();

        tokenField.setText("");

        Launch.getInstance().handleLogin(token);
    }
}