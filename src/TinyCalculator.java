import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;

public class TinyCalculator extends JFrame implements ActionListener  {
    // private JTextField content_input;// 输入/输出框
    JTextArea text1, text2; // 输入框第一第二行
    private JPanel jp_buttonArea;//按键区域
    private String []ButtonString={
        "%", "CE", "C", "DEL", // "DEL",
        "1/x", "x^2", "√", "÷",
        "7","8","9","×",
        "4","5","6","-",
        "1","2","3","+",
        "+/-","0", ".", "="
    };
    private static final int columns_text = 20;
    private String CurrentState;
    private String Current_Opcode = "";
    private String Current_Operator1 = "";
    private String Current_Operator2 = "";
    private String Current_Result = "";
    private boolean Operator2_Changed_Flag = false;
    public TinyCalculator() {
        this.setTitle("TinyCalculator");
        Container c=getContentPane();
        c.setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));


        JPanel jp_display = new JPanel(); // 显示区域
        jp_display.setLayout(new GridLayout(2, 1));
        text1 = new JTextArea(1, columns_text);
        //text1.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT); // 右对齐
        text1.setEditable(false);
        text1.setFont(new Font("Arial", Font.BOLD, 25));
        text1.setText(""); // 设置显示内容为空
        text2 = new JTextArea(1, columns_text);
        //text2.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT); // 右对齐
        text2.setEditable(false);
        text2.setFont(new Font("Arial", Font.BOLD, 48)); // 设置字体大小
        text2.setText("0"); // 设置显示内容为0
        jp_display.add(text1);
        jp_display.add(text2);
        c.add(jp_display);

        jp_buttonArea = new JPanel();
        jp_buttonArea.setLayout(new GridLayout(6, 4, 1, 1));
        jp_buttonArea.setSize(new Dimension(50, 50));
        for(int i = 0; i < ButtonString.length; i++){
            JButton btn = new JButton(ButtonString[i]);
            btn.addActionListener(this);
            jp_buttonArea.add(btn);
        }
        c.add(jp_buttonArea);
        this.setSize(350, 500);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(this);
        this.setVisible(true);
        this.setResizable(false);
        CurrentState = "Operator1";
        Current_Operator1 = "0";
    }

    @Override
    public void actionPerformed(ActionEvent e){
        Object source = e.getSource();
        if(CurrentState.equals("Operator1")){
            State_Operator1((JButton)source);
        }else if(CurrentState.equals("Operator2")){
            State_Operator2((JButton)source);
        }
    }

    private void State_Operator1(JButton button){
        if(isNumDot(button)){
            if(isDot(button)){
                if(Current_Operator1.contains(".")){ // There should be only one dot at most.
                    return;
                }
                Current_Operator1 += ".";
            }else{
                if(Current_Operator1.equals("0")){
                    Current_Operator1 = button.getText();
                }else{
                    Current_Operator1 += button.getText();
                }
            }
            text2.setText(Current_Operator1);
        }else if(isOperate(button)){
            CurrentState = "Operator2";
            Operator2_Changed_Flag = false; // To specify if the text2 is changed.
            Current_Operator2 = Current_Operator1;
            State_Operator2(button);
        }else if(isEQUAL(button)){
            text1.setText(Current_Operator1+"=");
        }else if(isC(button)){
            C();
        }else if(isCE(button)){
            CE();
        }else if(isDEL(button)){
            DEL();
        }else if(isReciprocal(button)){
            Reci();
        }else if(isSQR(button)){
            SQR();
        }else if(isROOT(button)){
            ROOT();
        }else if(isPM(button)){
            PM();
        }
    }

    private void State_Operator2(JButton button){
        String result;
        if(isOperate(button)){
            if(Operator2_Changed_Flag){
                result = Compute();
                Current_Operator1 = result;
            }
            text1.setText(Current_Operator1+button.getText());
            text2.setText(Current_Operator1);
            Current_Opcode = button.getText();
            Operator2_Changed_Flag = false;
        }else if(isNumDot(button)){
            if(!Operator2_Changed_Flag){ // 未改变
                Operator2_Changed_Flag = true;
                if(button.getText().equals(".")){
                    Current_Operator2 = "0.";
                }else{
                    Current_Operator2 = button.getText();
                }
            }else{ // 已改变
                if(isDot(button)){
                    if(Current_Operator2.contains(".")){ // There should be only one dot at most.
                        return;
                    }
                    Current_Operator2 += ".";
                }else{
                    Current_Operator2 += button.getText();
                }
            }
            text2.setText(Current_Operator2);
        }else if(isEQUAL(button)){
            result = Compute();
            text1.setText(Current_Operator1+Current_Opcode+Current_Operator2+button.getText());
            Current_Operator1 = result;
            text2.setText(result);
            CurrentState = "Operator1";
            Operator2_Changed_Flag = false;
        }else if(isC(button)){
            C();
        }else if(isCE(button)){
            CE();
        }else if(isDEL(button)){
            DEL();
        }else if(isReciprocal(button)){
            Reci();
        }else if(isSQR(button)){
            SQR();
        }else if(isROOT(button)){
            ROOT();
        }else if(isPM(button)){
            PM();
        }
    }

    // Compute func Compute the result according the operator1 and operator2 and opcode.
    private String Compute(){
        String result;
        if(Current_Opcode.equals("+")){
            result = ADD(Current_Operator1, Current_Operator2);
        }else if(Current_Opcode.equals("-")){
            result = SUB(Current_Operator1, Current_Operator2);
        }else if(Current_Opcode.equals("×")){
            result = MUL(Current_Operator1, Current_Operator2);
        }else if(Current_Opcode.equals("÷")){
            result = DIV(Current_Operator1, Current_Operator2);
        }else if(Current_Opcode.equals("%")){
            result = MOD(Current_Operator1, Current_Operator2);
        }else {
            result = "ERROR";
        }
        return result;
    }
    // Check the button is Num or dot.
    private boolean isNumDot(JButton button){
        if(button.getText().equals("0")){
            return true;
        }else if(button.getText().equals("1")){
            return true;
        }else if(button.getText().equals("2")){
            return true;
        }else if(button.getText().equals("3")){
            return true;
        }else if(button.getText().equals("4")){
            return true;
        }else if(button.getText().equals("5")){
            return true;
        }else if(button.getText().equals("6")){
            return true;
        }else if(button.getText().equals("7")){
            return true;
        }else if(button.getText().equals("8")){
            return true;
        }else if(button.getText().equals("9")){
            return true;
        }else if(button.getText().equals(".")){
            return true;
        }
        return false;
    }
    // Check the button is operate.
    private boolean isOperate(JButton button){
        if(button.getText().equals("+")){
            return true;
        }else if(button.getText().equals("-")){
            return true;
        }else if(button.getText().equals("×")){
            return true;
        }else if(button.getText().equals("÷")){
            return true;
        }else if(button.getText().equals("%")){
            return true;
        }
        return false;
    }
    // Check the button is func.
    private boolean isFunc(JButton button){
        if(button.getText().equals("1/x")){
            return true;
        }else if(button.getText().equals("x^2")){
            return true;
        }else if(button.getText().equals("√")){
            return true;
        }else if(button.getText().equals("+/-")){
            return true;
        }else if(button.getText().equals("%")){
            return true;
        }
        return false;
    }
    // Check the button is Dot.
    private boolean isDot(JButton button){
        if(button.getText().equals(".")){
            return true;
        }
        return false;
    }
    // Check the button is EQUAL.
    private boolean isEQUAL(JButton button){
        if(button.getText().equals("=")){
            return true;
        }
        return false;
    }
    // Check the button is CE.
    private boolean isCE(JButton button){
        if(button.getText().equals("CE")){
            return true;
        }
        return false;
    }
    // Check the button is CE.
    private boolean isC(JButton button){
        if(button.getText().equals("C")){
            return true;
        }
        return false;
    }
    // Check the button is DEL.
    private boolean isDEL(JButton button){
        if(button.getText().equals("DEL")){
            return true;
        }
        return false;
    }
    // Check the button is Reciprocal.
    private boolean isReciprocal(JButton button){
        if(button.getText().equals("1/x")){
            return true;
        }
        return false;
    }
    // Check the button is SQR.
    private boolean isSQR(JButton button){
        if(button.getText().equals("x^2")){
            return true;
        }
        return false;
    }
    // Check the button is ROOT.
    private boolean isROOT(JButton button){
        if(button.getText().equals("x^2")){
            return true;
        }
        return false;
    }
    // Check the button is PM.
    private boolean isPM(JButton button){
        if(button.getText().equals("+/-")){
            return true;
        }
        return false;
    }
    
    private String ADD(String _op1, String _op2){
        double op1 = Double.parseDouble(_op1);
        double op2 = Double.parseDouble(_op2);
        DecimalFormat format = new DecimalFormat("0.##########"); // remove trailing zeros from a double
        return format.format(op1+op2);
    }
    private String SUB(String _op1, String _op2){
        double op1 = Double.parseDouble(_op1);
        double op2 = Double.parseDouble(_op2);
        DecimalFormat format = new DecimalFormat("0.##########"); // remove trailing zeros from a double
        return format.format(op1-op2);
    }
    private String MUL(String _op1, String _op2){
        double op1 = Double.parseDouble(_op1);
        double op2 = Double.parseDouble(_op2);
        DecimalFormat format = new DecimalFormat("0.##########"); // remove trailing zeros from a double
        return format.format(op1*op2);
    }
    private String DIV(String _op1, String _op2){
        double op1 = Double.parseDouble(_op1);
        double op2 = Double.parseDouble(_op2);
        DecimalFormat format = new DecimalFormat("0.##########"); // remove trailing zeros from a double
        return format.format(op1/op2);
    }
    private String MOD(String _op1, String _op2){
        double op1 = Double.parseDouble(_op1);
        double op2 = Double.parseDouble(_op2);
        DecimalFormat format = new DecimalFormat("0.##########"); // remove trailing zeros from a double
        return format.format(op1%op2);
    }
    private void C(){
        CurrentState = "Operator1";
        Current_Operator1 = "0";
        Current_Opcode = "";
        Operator2_Changed_Flag = false;
        text1.setText("");
        text2.setText(Current_Operator1);
    }
    private void CE(){
        Current_Operator2 = "0";
        Operator2_Changed_Flag = false;
        text2.setText(Current_Operator2);
    }

    private void DEL(){
        if(CurrentState=="Operator1"){
            if(Current_Operator1.length()==1){
                Current_Operator1 = "0";
            }else{
                Current_Operator1 = Current_Operator1.substring(0, Current_Operator1.length()-1);
            }
            text2.setText(Current_Operator1);
        }else if(CurrentState=="Operator2"){
            if(Current_Operator2.length()==1){
                Current_Operator2 = "0";
                Operator2_Changed_Flag = false;
            }else{
                Current_Operator2 = Current_Operator2.substring(0, Current_Operator2.length()-1);
            }
            text2.setText(Current_Operator2);
        }
    }

    private void Reci(){
        if(CurrentState == "Operator1") {
            text1.setText("1/("+Current_Operator1+")");
            Current_Operator1 = DIV("1", Current_Operator1);
            text2.setText(Current_Operator1);
        }else if(CurrentState == "Operator2"){
            text1.setText(Current_Operator1+Current_Opcode+"1/("+Current_Operator2+")");
            Current_Operator2 = DIV("1", Current_Operator2);
            text2.setText(Current_Operator2);
        }
    }
    private void SQR(){
        if(CurrentState == "Operator1") {
            text1.setText("sqr("+Current_Operator1+")");
            Current_Operator1 = MUL(Current_Operator1, Current_Operator1);
            text2.setText(Current_Operator1);
        }else if(CurrentState == "Operator2"){
            text1.setText(Current_Operator1+Current_Opcode+"sqr("+Current_Operator2+")");
            Current_Operator2 = MUL(Current_Operator2, Current_Operator2);
            text2.setText(Current_Operator2);
        }
    }
    private void ROOT(){
        String result;
        DecimalFormat format = new DecimalFormat("0.##########");
        if(CurrentState == "Operator1") {
            text1.setText("√("+Current_Operator1+")");
            result = format.format(Math.sqrt(Double.parseDouble((Current_Operator1))));
            Current_Operator1 = result;
            text2.setText(Current_Operator1);
        }else if(CurrentState == "Operator2"){
            text1.setText("√("+Current_Operator2+")");
            result = format.format(Math.sqrt(Double.parseDouble((Current_Operator2))));
            Current_Operator2 = result;
            text2.setText(Current_Operator2);
        }
    }
    private void PM(){
        if(CurrentState == "Operator1") {
            Current_Operator1 = MUL("-1", Current_Operator1);
            text2.setText(Current_Operator1);
        }else if(CurrentState == "Operator2"){
            Current_Operator2 = MUL("-1", Current_Operator2);;
            text2.setText(Current_Operator2);
        }
    }



}
