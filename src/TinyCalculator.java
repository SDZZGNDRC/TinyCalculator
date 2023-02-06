import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TinyCalculator extends JFrame implements ActionListener  {
    // private JTextField content_input;// 输入/输出框
    JTextArea text1, text2; // 输入框第一第二行
    private JPanel jp_buttonArea;//按键区域
    private String []ButtonString={
        "%", "CE", "C", "DEL", // "DEL",
        "1/x", "x^2", "√￣", "÷",
        "7","8","9","×",
        "4","5","6","-",
        "1","2","3","+",
        "+/-","0", ".", "="
    };
    private static final int columns_text = 20;
    private String CurrentState;
    private String Current_Operator = "";
    private String Current_Operator1 = "";
    private String Current_Operator2 = "";
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
        }
    }

    private void State_Operator2(JButton button){
        if(isOperate(button)){
            text1.setText(Current_Operator1+button.getText());
            Current_Operator = button.getText();
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
        }
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
        }else if(button.getText().equals("/")){
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
        }else if(button.getText().equals("√￣")){
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

    // Append a string to a JTextArea.
    private void JTAAppend(JTextArea text, String str){
        if(text.getText().equals("0") && !str.equals(".")){
            text.setText(str);
            return;
        }
        text.setText(text.getText()+str);

    }

}
