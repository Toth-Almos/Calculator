import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Calculator {
    //Attributes:
    private JFrame calcFrame;
    private JTextField textField;
    private double result;
    private String expression;      //contains the operands and the operation as a String
    private CalculatorLogic calcLogic;


    //Constructor:
    Calculator() {
        this.result = 0.0;
        this.expression = "";
        this.calcLogic = new CalculatorLogic(this);
        prepareCalc();
    }

    //GUI:
    public void prepareCalc() {
        //JFrame:
        this.calcFrame = new JFrame();
        this.calcFrame.setResizable(false);
        this.calcFrame.setVisible(true);
        this.calcFrame.setTitle("Calculator");
        //Custom icon:
        ImageIcon icon = new ImageIcon("icon2.png");
        this.calcFrame.setIconImage(icon.getImage());
        this.calcFrame.getContentPane().setBackground(new Color(26, 26, 26));
        this.calcFrame.getContentPane().setFont(new Font("Calibri", Font.PLAIN, 15));
        this.calcFrame.getContentPane().setForeground(SystemColor.windowBorder);
        this.calcFrame.getContentPane().setLayout(null);

        //JPanel for the result label and expression text-field:
        JPanel textPanel = new JPanel();
        textPanel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
        textPanel.setBounds(34, 25, 316, 80);
        this.calcFrame.getContentPane().add(textPanel);
        textPanel.setLayout(null);

        //Upper text for result:
        JLabel expressionLabel = new JLabel("");
        expressionLabel.setBackground(SystemColor.control);
        expressionLabel.setFont(new Font("Yu Gothic UI Light", Font.PLAIN, 20));
        expressionLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        expressionLabel.setForeground(UIManager.getColor("Button.disabledForeground"));
        expressionLabel.setBounds(2, 2, 312, 27);
        textPanel.add(expressionLabel);

        //Lower text for operation members:
        this.textField = new JTextField();
        expressionLabel.setLabelFor(textField);
        textField.setHorizontalAlignment(SwingConstants.RIGHT);
        textField.setBackground(SystemColor.control);
        textField.setEditable(false);
        textField.setText("");
        textField.setBorder(null);
        textField.setFont(new Font("Yu Gothic UI Light", textField.getFont().getStyle(), 32));
        textField.setBounds(2, 30, 312, 49);
        textPanel.add(textField);
        textField.setColumns(10);

        //Panel for the buttons:
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
        buttonPanel.setBackground(SystemColor.inactiveCaptionBorder);
        buttonPanel.setBounds(34, 120, 316, 322);
        this.calcFrame.getContentPane().add(buttonPanel);
        buttonPanel.setLayout(new GridLayout(0,4,0,0));

        //Buttons:------------------------------------------------------------------------------------------------------------------------------------------------------------
        //clear:
        JButton clearButton = new JButton("C");
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                result = 0.0;
                expression = "";
                textField.setText("");
            }
        });
        clearButton.setFocusPainted(false);
        buttonPanel.add(clearButton);

        //delete:
        JButton deleteButton = new JButton("DEL");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(textField.getText().length() > 0) {
                    StringBuffer sb = new StringBuffer(textField.getText());
                    char lastChar = sb.charAt(sb.length()-1);

                    if(Character.isDigit(lastChar) || lastChar == 'e' || lastChar == 'π') {
                        sb.deleteCharAt(sb.length() - 1);
                    }
                    else {
                        if(CalculatorLogic.isTwoOperandOperator(String.valueOf(lastChar))) {
                            sb.deleteCharAt(sb.length()-1);
                        }
                        else if(lastChar == 't') {
                            sb = sb.delete(sb.length()-4, sb.length());
                        }
                        else if(lastChar == 'n' && sb.charAt(sb.length()-2) == 'l') {
                            sb = sb.delete(sb.length()-2, sb.length());
                        }
                        else {
                            sb = sb.delete(sb.length()-3, sb.length());
                        }
                    }
                    textField.setText(sb.toString());
                }
            }
        });
        deleteButton.setFocusPainted(false);
        buttonPanel.add(deleteButton);

        //pi:
        JButton piButton = new JButton("π");
        piButton.addActionListener(new numberListener());
        piButton.setFocusPainted(false);
        buttonPanel.add(piButton);

        //e:
        JButton eButton = new JButton("e");
        eButton.addActionListener(new numberListener());
        eButton.setFocusPainted(false);
        buttonPanel.add(eButton);

        //exp:
        JButton exponentiationButton = new JButton("<html><body><span>x<sup>y<html><body><span>");
        exponentiationButton.addActionListener(new operationListener("^"));
        exponentiationButton.setFocusPainted(false);
        buttonPanel.add(exponentiationButton);

        //x!:
        JButton factorialButton = new JButton("x!");
        factorialButton.addActionListener(new operationListener("!"));
        factorialButton.setFocusPainted(false);
        buttonPanel.add(factorialButton);

        //ln:
        JButton naturalLogButton = new JButton("ln");
        naturalLogButton.addActionListener(new operationListener("ln"));
        naturalLogButton.setFocusPainted(false);
        buttonPanel.add(naturalLogButton);

        //log10:
        JButton logBase10Button = new JButton("<html><body><span>log<sub>10</span></body></html>");
        logBase10Button.addActionListener(new operationListener("log"));
        logBase10Button.setFocusPainted(false);
        buttonPanel.add(logBase10Button);

        //sin:
        JButton sinButton = new JButton("sin");
        sinButton.addActionListener(new operationListener("sin"));
        sinButton.setFocusPainted(false);
        buttonPanel.add(sinButton);

        //cos:
        JButton cosButton = new JButton("cos");
        cosButton.addActionListener(new operationListener("cos"));
        cosButton.setFocusPainted(false);
        buttonPanel.add(cosButton);

        //tan:
        JButton tanButton = new JButton("tan");
        tanButton.addActionListener(new operationListener("tan"));
        tanButton.setFocusPainted(false);
        buttonPanel.add(tanButton);

        //square root:
        JButton sqrtButton = new JButton("<html><body><span>√</span></body></html>");
        sqrtButton.addActionListener(new operationListener("sqrt"));
        sqrtButton.setFocusPainted(false);
        buttonPanel.add(sqrtButton);

        //7:
        JButton sevenButton = new JButton("7");
        sevenButton.addActionListener(new numberListener());
        sevenButton.setFocusPainted(false);
        buttonPanel.add(sevenButton);

        //8:
        JButton eightButton = new JButton("8");
        eightButton.addActionListener(new numberListener());
        eightButton.setFocusPainted(false);
        buttonPanel.add(eightButton);

        //9:
        JButton nineButton = new JButton("9");
        nineButton.addActionListener(new numberListener());
        nineButton.setFocusPainted(false);
        buttonPanel.add(nineButton);

        //divide:
        JButton divisionButton = new JButton("<html><body><span>÷</span></body></html>");
        divisionButton.addActionListener(new operationListener("/"));
        divisionButton.setFocusPainted(false);
        buttonPanel.add(divisionButton);

        //4:
        JButton fourButton = new JButton("4");
        fourButton.addActionListener(new numberListener());
        fourButton.setFocusPainted(false);
        buttonPanel.add(fourButton);

        //5:
        JButton fiveButton = new JButton("5");
        fiveButton.addActionListener(new numberListener());
        fiveButton.setFocusPainted(false);
        buttonPanel.add(fiveButton);

        //6:
        JButton sixButton = new JButton("6");
        sixButton.addActionListener(new numberListener());
        sixButton.setFocusPainted(false);
        buttonPanel.add(sixButton);

        //multiply
        JButton multiplyButton = new JButton("*");
        multiplyButton.addActionListener(new operationListener("*"));
        multiplyButton.setFocusPainted(false);
        buttonPanel.add(multiplyButton);

        //1:
        JButton oneButton = new JButton("1");
        oneButton.addActionListener(new numberListener());
        oneButton.setFocusPainted(false);
        buttonPanel.add(oneButton);

        //2:
        JButton twoButton = new JButton("2");
        twoButton.addActionListener(new numberListener());
        twoButton.setFocusPainted(false);
        buttonPanel.add(twoButton);

        //3:
        JButton threeButton = new JButton("3");
        threeButton.addActionListener(new numberListener());
        threeButton.setFocusPainted(false);
        buttonPanel.add(threeButton);

        //subtraction:
        JButton subtractionButton = new JButton("-");
        subtractionButton.addActionListener(new operationListener("-"));
        subtractionButton.setFocusPainted(false);
        buttonPanel.add(subtractionButton);

        //dot:
        JButton dotButton = new JButton(".");
        dotButton.addActionListener(new numberListener());
        dotButton.setFocusPainted(false);
        buttonPanel.add(dotButton);

        //0:
        JButton zeroButton = new JButton("0");
        zeroButton.addActionListener(new numberListener());
        zeroButton.setFocusPainted(false);
        buttonPanel.add(zeroButton);

        //equal:
        JButton equalButton = new JButton("=");
        equalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(expression.length() > 0) {
                    expression = textField.getText();
                    calcLogic.calculate();
                }
            }
        });
        equalButton.setBackground(new Color(180, 0, 0));
        equalButton.setFocusPainted(false);
        buttonPanel.add(equalButton);

        //addition:
        JButton additionButton = new JButton("+");
        additionButton.addActionListener(new operationListener("+"));
        additionButton.setFocusPainted(false);
        buttonPanel.add(additionButton);

        //finishing touches:
        this.calcFrame.setBounds(200, 100, 400, 500);
        this.calcFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    //Action listener for number buttons:
    class numberListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String digit = e.getActionCommand();
            textField.setText(textField.getText() + digit);
        }
    }

    //ActionListener for operators:
    class operationListener implements ActionListener {
        private String op;
        public operationListener(String givenOp) { this.op = givenOp; }

        @Override
        public void actionPerformed(ActionEvent e) {
            textField.setText(textField.getText() + this.op);
            expression = textField.getText();
        }
    }

    //Getters & Setters:
    public JTextField getTextField() { return textField; }
    public double getResult() { return result; }
    public void setResult(double result) { this.result = result; }
    public String getExpression() { return expression; }
    public void setExpression(String expression) { this.expression = expression; }
}
