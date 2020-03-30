package org.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class PrimaryController {

    @FXML // fx:id="outputTF"
    private TextField outputTF; // Value injected by FXMLLoader

    @FXML
    public void onNumberOperatorClick(ActionEvent actionEvent) {
        Button button = (Button) actionEvent.getSource();
        outputTF.setText(outputTF.getText() + button.getText());
    }

    @FXML
    public void onEqualClick() {
        DecimalFormat numberFormat = new DecimalFormat("0.00000");
        numberFormat.setRoundingMode(RoundingMode.DOWN);
        String exp = outputTF.getText();
        outputTF.setText(numberFormat.format(calculate(exp)));
    }

    @FXML
    void onCClick() {
        outputTF.setText("");
    }

    @FXML
    void onDELClick() {
        String input = outputTF.getText();
        if (input.length() > 0)
            outputTF.setText(input.substring(0, input.length() - 1));
    }
//
//    @FXML
//    public void handleKeyboard(KeyEvent keyEvent) {
//
//        switch (keyEvent.getCode())
//        {
//            case EQUALS: onEqualClick();
//            break;
//            case DELETE: onDELClick();
//            break;
//
//
//        }
//
//    }

    private Double calculate(String exp) {

        // parenthesis case
        int ind = exp.indexOf(")");
        if (ind != -1) {
            // ind is the index of the first ')' and the loop finds the matching '('
            int lp = ind - 1;
            while (exp.charAt(lp) != '(')
                lp--;
            // then we calculate the value within the parenthesis, adding it back to the expression and call recursively
            return calculate(exp.substring(0, lp) + calculate(exp.substring(lp + 1, ind)) + exp.substring(ind + 1));
        }

        ind = exp.indexOf("+");
        if (ind != -1)
            return calculate(exp.substring(0, ind)) + calculate(exp.substring(ind + 1));

        // - case: we only want to execute cases of num1 - num2 (not -"exp" or num1 *-num2)
        for (int i = 1; i < exp.length(); i = Math.max(i + 1, ind)) {
            ind = exp.indexOf("-", i);
            if (ind != -1 && exp.charAt(ind - 1) != 'X' && exp.charAt(ind - 1) != '/')
                return calculate(exp.substring(0, ind)) - calculate(exp.substring(ind + 1));
        }
        // case of -num. (by the program structure and the if statement There won't be any other operators in the string)
        ind = exp.indexOf("-");
        if (ind != -1 && !exp.contains("X") && !exp.contains("/"))
            return -calculate(exp.substring(1));

        ind = exp.indexOf("X");
        if (ind != -1)
            return calculate(exp.substring(0, ind)) * calculate(exp.substring(ind + 1));

        ind = exp.lastIndexOf("/");
        if (ind != -1)
            return calculate(exp.substring(0, ind)) / calculate(exp.substring(ind + 1));

        return Double.parseDouble(exp);
    }
}