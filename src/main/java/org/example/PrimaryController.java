package org.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class PrimaryController {

    @FXML // fx:id="outputTF"
    private TextField outputTF; // Value injected by FXMLLoader

    @FXML   // handles nums or operators clicked, add the pressed button to the display
    public void onNumberOperatorClick(ActionEvent actionEvent) {
        Button button = (Button) actionEvent.getSource();
        outputTF.setText(outputTF.getText() + button.getText());
    }

    @FXML   // handles '=' clicks, calculate the string from the display
    public void onEqualClick() {
        String exp = outputTF.getText().replace(" ", "");
        double res = ((double) (int) (calculate(exp) * 100000) / 100000);
        outputTF.setText(String.format("%.5f", res));

    }

    @FXML
    void onCClick() {
        outputTF.setText("");
    }

    @FXML
        // handles del clicks, removes the last digit from the display
    void onDELClick() {
        String input = outputTF.getText();
        if (input.length() > 0)
            outputTF.setText(input.substring(0, input.length() - 1));
    }

    private Double calculate(String exp) {

        // parenthesis case
        int ind = exp.indexOf(")");
        if (ind != -1)
        {
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
        for (int i = 1; i < exp.length(); i = Math.max(i + 1, ind))
        {
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