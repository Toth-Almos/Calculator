import javax.swing.*;

public class CalculatorLogic {
    //Calculator attribute:
    private final Calculator calculator;

    //Constructor:
    CalculatorLogic(Calculator c) { this.calculator = c; }

    //The main logic:
    public void calculate() {
        StringBuilder operator = new StringBuilder();
        char[] expressionAsCharArray = this.calculator.getExpression().toCharArray();


        //if we have a two operand expressions:
        if(this.calculator.getExpression().matches("-?[0-9|e|π|.]+[+*/^-]-?[0-9|e|π|.]+")) {
            boolean isFirstOperandNegative = false;
            boolean isSecondOperandNegative = false;
            int whereToSplit = 0;

            //search the operand and check if operands are negative numbers:
            for(int i = 0; i < expressionAsCharArray.length; i++) {
                if(CalculatorLogic.isTwoOperandOperator(String.valueOf(expressionAsCharArray[i]))) {
                    if(expressionAsCharArray[i] == '-') {
                        if(i == 0) { isFirstOperandNegative = true; }
                        else if((Character.isDigit(expressionAsCharArray[i+1]) || expressionAsCharArray[i+1] == 'e' || expressionAsCharArray[i+1] == 'π') && !Character.isDigit(expressionAsCharArray[i-1])) {
                            isSecondOperandNegative = true;
                        }
                        else {
                            operator = operator.append(expressionAsCharArray[i]);
                            whereToSplit = i;
                        }
                    }
                    else {
                        operator = operator.append(expressionAsCharArray[i]);
                        System.out.println(operator);
                        whereToSplit = i;
                    }
                }
            }

            StringBuilder d1, d2;
            if(isFirstOperandNegative) { d1 = new StringBuilder(this.calculator.getExpression().substring(1, whereToSplit)); }
            else { d1 = new StringBuilder(this.calculator.getExpression().substring(0, whereToSplit)); }
            if(isSecondOperandNegative) { d2 = new StringBuilder(this.calculator.getExpression().substring(whereToSplit + 2)); }
            else { d2 = new StringBuilder(this.calculator.getExpression().substring(whereToSplit + 1)); }

            //if user write more than one 'e' or 'π' we will reduce it to only one
            //we need to check it here
            //check for e:
            if(String.valueOf(d1).matches("e+")) { d1 = new StringBuilder(String.valueOf(Math.E)); }
            if(String.valueOf(d2).matches("e+")) { d2 = new StringBuilder(String.valueOf(Math.E)); }
            //check for π:
            if(String.valueOf(d1).matches("π+")) { d1 = new StringBuilder(String.valueOf(Math.PI)); }
            if(String.valueOf(d2).matches("π+")) { d2 = new StringBuilder(String.valueOf(Math.PI)); }

            double operand1 = Double.parseDouble(String.valueOf(d1));
            double operand2 = Double.parseDouble(String.valueOf(d2));
            //if negative:
            if(isFirstOperandNegative) { operand1 = operand1 * (-1); }
            if(isSecondOperandNegative) { operand2 = operand2 * (-1); }


            System.out.println(operand1 + " " + operand2);
            this.calculator.setResult(twoOperandCalculation(operand1, operand2, String.valueOf(operator), this.calculator.getTextField()));
            this.calculator.getTextField().setText(String.valueOf(this.calculator.getResult()));
        }
        //if we have a one operand expressions:
        else if(this.calculator.getExpression().matches("(ln|log|sin|cos|tan|sqrt)-?[0-9|e|π|.]+")) {
            operator = new StringBuilder();
            StringBuilder operand = new StringBuilder();
            for(char c : expressionAsCharArray) {
                if(!Character.isDigit(c) && c != 'e' && c != 'π' && c != '.' && c != '-') { operator.append(c); }
                else { operand.append(c); }
            }
            System.out.println(operator);
            System.out.println(operand);

            //if user write more than one 'e' or 'π' we will reduce it to only one
            //we need to check it here
            //check for e:
            if(String.valueOf(operand).matches("e+")) { operand.replace(0, operand.length(), String.valueOf(Math.E)); }
            //check for π:
            if(String.valueOf(operand).matches("π+")) { operand.replace(0, operand.length(), String.valueOf(Math.PI)); }

            this.calculator.setResult(oneOperandCalculation(Double.parseDouble(String.valueOf(operand)), String.valueOf(operator), this.calculator.getTextField()));
            this.calculator.getTextField().setText(String.valueOf(this.calculator.getResult()));
        }
        //for factorial: It's a different story because '!' needs to be after the number
        else if(this.calculator.getExpression().matches("[0-9|e|π|.]+!")) {
            StringBuilder operand = new StringBuilder(this.calculator.getExpression());
            operand.deleteCharAt(operand.length()-1);
            this.calculator.setResult(oneOperandCalculation(Double.parseDouble(String.valueOf(operand)), "!", this.calculator.getTextField()));
            this.calculator.getTextField().setText(String.valueOf(this.calculator.getResult()));
        }
        //for wrong input:
        else {
            this.calculator.setResult(0.0);
            this.calculator.getTextField().setText("");
            System.out.println("error");
        }

        this.calculator.setExpression("");
    }

    public static double factorial(double x) {
        if(x == 0 || x == 1) { return 1; }
        double answer = 1;
        for(int i = 2; i <= x; i++ ) { answer *= i; }
        return answer;
    }
    public static double twoOperandCalculation(double x, double y, String c, JTextField tf) {
        double answer = 0;
        switch (c) {
            case "+":
                answer = x + y;
                break;
            case "-":
                answer = x - y;
                break;
            case "*":
                answer = x * y;
                break;
            case "/":
                if(y == 0) { System.out.println("can't divide by 0"); }
                else { answer = x / y; }
                break;
            case "^":
                answer = Math.pow(x,y);
                break;
            default:
                System.out.println("error!");
                tf.setText("");
                break;
        }
        return answer;
    }
    public static double oneOperandCalculation(double x, String c, JTextField tf) {
        double answer = 0;
        switch (c) {
            case "log":
                answer = Math.log10(x);
                break;
            case "ln":
                answer = Math.log(x);
                break;
            case "sin":
                answer = formatFloat(Math.sin(Math.toRadians(x)));
                break;
            case "cos":
                answer = formatFloat(Math.cos(Math.toRadians(x)));
                break;
            case "tan":
                answer = formatFloat(Math.tan(Math.toRadians(x)));
                break;
            case "sqrt":
                answer = Math.sqrt(x);
                break;
            case "!":
                answer = CalculatorLogic.factorial(x);
                break;
            default:
                System.out.println("error!");
                tf.setText("");
                break;
        }
        return answer;
    }
    public static boolean isTwoOperandOperator(String s) {
        if(s.equals("+") || s.equals("-") || s.equals("*") || s.equals("/") || s.equals("^")) {
            return true;
        }
        return false;
    }
    public static boolean isOneOperandOperator(String s) {
        if(s.equals("ln") || s.equals("log") || s.equals("!") || s.equals("sqrt") || s.equals("sin") || s.equals("cos") || s.equals("tan")) {
            return true;
        }
        return false;
    }

    public static double formatFloat(double value) {
        return (double)Math.round(value * 100000000) / 100000000;
    }

}
