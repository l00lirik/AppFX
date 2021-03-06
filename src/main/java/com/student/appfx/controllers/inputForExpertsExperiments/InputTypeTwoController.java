package com.student.appfx.controllers.inputForExpertsExperiments;

import com.student.appfx.cache.DataForExpertExperiments;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputTypeTwoController {

    private static VBox paneLog;

    private final Pattern patternAccuracy = Pattern.compile("\\d+\\.\\d+");
    private final Pattern patternExpert = Pattern.compile("\\d+");

    @FXML
    private TextArea fieldAlert;
    @FXML
    private TextField accuracyMax;
    @FXML
    private TextField accuracyMin;
    @FXML
    private TextField expertMax;
    @FXML
    private TextField expertMin;


    public static void setPaneLog(VBox paneLog) {
        InputTypeTwoController.paneLog = paneLog;
    }

    private void close(ActionEvent actionEvent) {
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    public void actionClose(ActionEvent actionEvent) {
        Label label = new Label("Отмена ввода данных");
        addRecordToLog(label);
        DataForExpertExperiments.clear();
        close(actionEvent);
    }

    public void actionSave(ActionEvent actionEvent) {
        String minAc = accuracyMin.getText();
        if (Objects.equals(minAc, "")) {
            minAc = null;
        }
        String maxAC = accuracyMax.getText();
        if (Objects.equals(maxAC, "")) {
            maxAC = null;
        }
        String minEx = expertMin.getText();
        if (Objects.equals(minEx, "")) {
            minEx = null;
        }
        String maxEx = expertMax.getText();
        if (Objects.equals(maxEx, "")) {
            maxEx = null;
        }
        if (checkAccuracyInput(minAc, maxAC, patternAccuracy) &
                checkExpertInput(minEx, maxEx, patternExpert)) {
            DataForExpertExperiments.diapasons.add(1);
            DataForExpertExperiments.diapasons.add(2);
            DataForExpertExperiments.diapasons.add(3);
            DataForExpertExperiments.experimentType = 2;
            DataForExpertExperiments.dataInput = true;
            close(actionEvent);
        } else {
            DataForExpertExperiments.clear();
        }
    }

    private boolean checkAccuracyInput(String min, String max, Pattern pattern) {
        if (min == null & max == null) {
            accuracyMax.setText("error");
            accuracyMin.setText("error");
            return false;
        }
        if(min == null | max == null) {
            if (min != null) {
                if (match(min, pattern)) {
                    DataForExpertExperiments.accuracy.add(Double.valueOf(min));
                    Label label = new Label("Введена только min точность");
                    addRecordToLog(label);
                    return true;
                } else {
                    accuracyMin.setText("error");
                    return false;
                }
            }
            if (match(max, pattern)) {
                DataForExpertExperiments.accuracy.add(Double.valueOf(max));
                Label label = new Label("Введена только max точность");
                addRecordToLog(label);
                return true;
            } else {
                accuracyMax.setText("error");
                return false;
            }
        }
        boolean minMatch = match(min, pattern);
        boolean maxMatch = match(max, pattern);
        if (minMatch & maxMatch) {
            if (Double.parseDouble(max) <= Double.parseDouble(min)) {
                fieldAlert.setText("max <= min: Проверьте правильность ввода. При исследовании одного значения заполните одно из полей");
                fieldAlert.setVisible(true);
                return false;
            }
            DataForExpertExperiments.accuracy.add(Double.parseDouble(min));
            DataForExpertExperiments.accuracy.add(Double.parseDouble(max));
            Label label = new Label("Диапазон точности введен");
            addRecordToLog(label);
            return true;
        } else {
            if (!minMatch) {
                accuracyMin.setText("error");
            }
            if (!maxMatch) {
                accuracyMax.setText("error");
            }
            return false;
        }
    }

    private boolean checkExpertInput(String min, String max, Pattern pattern) {
        if (min == null & max == null) {
            expertMax.setText("error");
            expertMin.setText("error");
            return false;
        }
        if(min == null | max == null) {
            if (min != null ) {
                if (match(min, pattern)) {
                    DataForExpertExperiments.experts.add(Integer.parseInt(min));
                    Label label = new Label("Введено только min количество экспертов");
                    addRecordToLog(label);
                    return true;
                }else {
                    expertMin.setText("error");
                    return false;
                }
            }
            if (match(max, pattern)) {
                DataForExpertExperiments.experts.add(Integer.parseInt(max));
                Label label = new Label("Введено только max количество экспертов");
                addRecordToLog(label);
                return true;
            }else {
                expertMax.setText("error");
                return false;
            }
        }
        boolean minMatch = match(min, pattern);
        boolean maxMatch = match(max, pattern);
        if (minMatch & maxMatch) {
            if (Integer.parseInt(max) <= Integer.parseInt(min)) {
                fieldAlert.setText("max <= min: Проверьте правильность ввода. При исследовании одного значения заполните одно из полей");
                fieldAlert.setVisible(true);
                return false;
            }
            if (Integer.parseInt(min) == 0) {
                fieldAlert.setText("Нулевые значения недопустимы");
                fieldAlert.setVisible(true);
                return false;
            }
            DataForExpertExperiments.experts.add(Integer.parseInt(min));
            DataForExpertExperiments.experts.add(Integer.parseInt(max));
            Label label = new Label("Диапазон количества экспетов введен");
            addRecordToLog(label);
            return true;
        } else {
            if (!minMatch) {
                expertMin.setText("error");
            }
            if (!maxMatch) {
                expertMax.setText("error");
            }
            return false;
        }
    }

    private boolean match(String value, Pattern pattern) {
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }

    private void addRecordToLog(Label label) {
        VBox.setMargin(label, new Insets(0, 5, 2, 5));
        paneLog.getChildren().add(label);
    }
}
