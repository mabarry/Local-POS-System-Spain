package com.example.possystemgui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class databaseGUIController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onButtonClick() {
        welcomeText.setText("Login Success");
    }
}