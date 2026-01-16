import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;

public class PracticeExam extends JFrame {
    private final String id;
    private final JFrame frame;
    JPanel leftPanel = new JPanel();
    private final JPanel rightPanel;
    private final JPanel bottomPanel;
    int score=0;

    private final JLabel timeLabel;

    ArrayList<Question> questions;
    private int currentQuestionIndex;
    private int[] answers;
    private int count;

    private Panel typePanel;
    private Button confirmButton;
    private final Timer timer;
    private int remainingTime;

    public PracticeExam(String id) {
        this.id = id;
        //设置界面
        frame = new JFrame("Practice Exam");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setLocation(600, 150);
        frame.setSize(1600, 1600);
        frame.setResizable(false);

        // 初始化题目列表和答案数组
        questions = new ArrayList<>();
        answers = new int[50];
        for (int i = 0; i < 50; i++) {
            answers[i] = -1;
        }
        // 加载题目数据
        loadQuestions();


        // 初始化左侧面板
        leftPanel.setPreferredSize(new Dimension(250, 300));
        leftPanel.setOpaque(false);
        leftPanel.setLayout(new GridLayout(10, 2, 10, 5));
        leftPanel.add(new JLabel("考生账号"));
        leftPanel.add(new JLabel(id));
        leftPanel.add(new JLabel("选择车型："));
        leftPanel.add(new JLabel("C1"));
        leftPanel.add(new JLabel("考试类别"));
        leftPanel.add(new JLabel("科目四理论"));
        leftPanel.add(new JLabel("考试题数："));
        leftPanel.add(new JLabel("50题"));
        leftPanel.add(new JLabel("合格标准："));
        leftPanel.add(new JLabel("90分/100分"));
        leftPanel.add(new JLabel("考试时间："){
            {setForeground(Color.MAGENTA);}
        });
        timeLabel = new JLabel("30:00");
        timeLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));

        
        leftPanel.setBackground(Color.blue);
        // 初始化倒计时器
        leftPanel.add(timeLabel);



        frame.add(leftPanel, BorderLayout.WEST);

        // 初始化右侧面板
        rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());
        rightPanel.setSize(1000, 300);
        frame.add(rightPanel, BorderLayout.CENTER);

        // 初始化底部面板
        bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridLayout(5, 10));
        for (int i = 0; i < 50; i++) {
            JButton button = new JButton(String.valueOf(i + 1));
            bottomPanel.add(button);
            int  index = i;
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (!questions.get(currentQuestionIndex).getisanswerd()&&answers[currentQuestionIndex] >= 0){
                        JOptionPane.showMessageDialog(frame, "请确定答案", "提示", JOptionPane.WARNING_MESSAGE);
                    } else{
                        currentQuestionIndex = index;
                        showQuestion(currentQuestionIndex);
                    }
                }
            });
        }
        frame.add(bottomPanel, BorderLayout.SOUTH);


        // 随机排序题目列表
        currentQuestionIndex = 0;

        // 显示第一道题目
        showQuestion(currentQuestionIndex);

        // 初始化倒计时器
        remainingTime = 30 * 60;
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                remainingTime--;
                if (remainingTime == 0) {
                    timer.stop();
                    submitExamtime();
                } else {
                    updateRemainingTime();
                }
            }
        });

        // 提醒用户开始考试
        JOptionPane.showMessageDialog(frame, "点击确定开始考试\n你有三十分钟的时间", "提示", JOptionPane.PLAIN_MESSAGE);

        // 开始倒计时
        timer.start();

        frame.pack();
        frame.setVisible(true);
    }
    private void loadQuestions() {
        String url = "jdbc:mysql://localhost:3306/training";
        String user = "root";
        String password = "242648";

        try (Connection con = DriverManager.getConnection(url, user, password);
             Statement stmt = con.createStatement()) {

            // 加载所有非多选题
            ResultSet rs = stmt.executeQuery("SELECT * FROM allquestion WHERE type!='多选题' ORDER BY RAND() LIMIT 45");
            while (rs.next()) {
                if (rs.getBytes("image") != null) {
                    Question question = new Question(
                            rs.getString("type"),
                            rs.getString("stem"),
                            rs.getString("a"),
                            rs.getString("b"),
                            rs.getString("c"),
                            rs.getString("d"),
                            rs.getString("answer"),
                            rs.getString("solution"),
                            rs.getBytes("image")
                    );
                    questions.add(question);
                } else {
                    Question question = new Question(
                            rs.getString("type"),
                            rs.getString("stem"),
                            rs.getString("a"),
                            rs.getString("b"),
                            rs.getString("c"),
                            rs.getString("d"),
                            rs.getString("answer"),
                            rs.getString("solution")
                    );
                    questions.add(question);
                }
            }

            // 加载多选题
            rs = stmt.executeQuery("SELECT * FROM allquestion WHERE type='多选题' ORDER BY RAND() LIMIT 5");
            while (rs.next()) {
                if (rs.getBytes("image") != null) {
                    Question question = new Question(
                            rs.getString("type"),
                            rs.getString("stem"),
                            rs.getString("a"),
                            rs.getString("b"),
                            rs.getString("c"),
                            rs.getString("d"),
                            rs.getString("answer"),
                            rs.getString("solution"),
                            rs.getBytes("image")
                    );
                    questions.add(question);
                } else {
                    Question question = new Question(
                            rs.getString("type"),
                            rs.getString("stem"),
                            rs.getString("a"),
                            rs.getString("b"),
                            rs.getString("c"),
                            rs.getString("d"),
                            rs.getString("answer"),
                            rs.getString("solution")
                    );
                    questions.add(question);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    private void showQuestion(int index) {

        rightPanel.removeAll();
        Question question = questions.get(index);

        // 显示题干和图片（如果有）
        JTextArea stemArea = new JTextArea("第"+(index+1)+"题:\n"+question.getStem());
        stemArea.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        stemArea.setEditable(false);
        stemArea.setLineWrap(true);
        stemArea.setPreferredSize(new Dimension(600, 150));
        Panel imagePanel = null;
        if (question.getImage() != null) {
            imagePanel = new Panel();
            imagePanel.setPreferredSize(new Dimension(400, 150));
            ImageIcon icon = new ImageIcon(question.getImage());
            JLabel lable = new JLabel(icon);
            lable.setPreferredSize(new Dimension(400, 150));
            imagePanel.add(lable);
            rightPanel.add(imagePanel, BorderLayout.CENTER);
            imagePanel.setVisible(true);
        } else {
            imagePanel = new Panel();
            imagePanel.setPreferredSize(new Dimension(400, 150));
            rightPanel.add(imagePanel, BorderLayout.CENTER);
            imagePanel.setVisible(true);
        }
        //显示题型
        Panel typePanel = new Panel();
        typePanel.setPreferredSize(new Dimension(600, 200));
        typePanel.add(new JLabel("当前题型:" + question.getType()+"                "+"当前得分为：" + score+"，当前错题数为："+count), BorderLayout.NORTH);
        typePanel.setFont(new Font("微软雅黑", Font.PLAIN, 10));
        typePanel.add(stemArea, BorderLayout.CENTER);
        rightPanel.add(typePanel, BorderLayout.NORTH);

        // 显示选项和按钮
        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new GridLayout(0, 1));
        optionsPanel.setPreferredSize(new Dimension(400, 150));

        JRadioButton option1A = new JRadioButton("正确");
        JRadioButton option2B = new JRadioButton("错误");

        JCheckBox optionA = new JCheckBox("A. " + question.getA());
        JCheckBox optionB = new JCheckBox("B. " + question.getB());
        JCheckBox optionC = new JCheckBox("C. " + question.getC());
        JCheckBox optionD = new JCheckBox("D. " + question.getD());

        option1A.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        option2B.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        optionA.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        optionB.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        optionC.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        optionD.setFont(new Font("微软雅黑", Font.PLAIN, 14));

        if (question.getType().equals("判断题")) {
            ButtonGroup group = new ButtonGroup();
            group.add(option1A);
            group.add(option2B);
            if (answers[index] == 0) {
                option1A.setSelected(true);
            } else if (answers[index] == 1) {
                option2B.setSelected(true);
            }
            option1A.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    answers[currentQuestionIndex] = 0;
                }
            });
            option2B.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    answers[currentQuestionIndex] = 1;
                }
            });
            optionsPanel.add(option1A);
            optionsPanel.add(option2B);
        } else {
            if (answers[index] == 0 || answers[index] == 5) {
                optionA.setSelected(true);
            }
            if (answers[index] == 1 || answers[index] == 5) {
                optionB.setSelected(true);
            }
            if (answers[index] == 2 || answers[index] == 5) {
                optionC.setSelected(true);
            }
            if (answers[index] == 3 || answers[index] == 5) {
                optionD.setSelected(true);
            }
            optionA.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (question.getType().equals("单选题")) {
                        answers[currentQuestionIndex] = 0;
                        optionB.setSelected(false);
                        optionC.setSelected(false);
                        optionD.setSelected(false);
                    } else if (question.getType().equals("多选题")){
                        if (optionA.isSelected()) {
                            answers[currentQuestionIndex] += 1;
                        } else
                            answers[currentQuestionIndex] += 0;
                    }
                }
            });
            optionB.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (question.getType().equals("单选题")) {
                        answers[currentQuestionIndex] = 1;
                        optionA.setSelected(false);
                        optionC.setSelected(false);
                        optionD.setSelected(false);
                    } else  if (question.getType().equals("多选题")){
                        if (optionB.isSelected()) {
                            answers[currentQuestionIndex] += 2;
                        } else
                            answers[currentQuestionIndex] += 0;
                    }
                }
            });
            optionC.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (question.getType().equals("单选题")) {
                        answers[currentQuestionIndex] = 2;
                        optionA.setSelected(false);
                        optionB.setSelected(false);
                        optionD.setSelected(false);
                    } else  if (question.getType().equals("多选题")){
                        if (optionC.isSelected()) {
                            answers[currentQuestionIndex] += 4;
                        } else
                            answers[currentQuestionIndex] += 0;
                    }
                }
            });
            optionD.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (question.getType().equals("单选题")) {
                        answers[currentQuestionIndex] = 3;
                        optionA.setSelected(false);
                        optionB.setSelected(false);
                        optionC.setSelected(false);
                    } else  if (question.getType().equals("多选题")){
                        if (optionD.isSelected()) {
                            answers[currentQuestionIndex] += 8;
                        } else
                            answers[currentQuestionIndex] += 0;
                    }
                }
            });
            optionsPanel.add(optionA);
            optionsPanel.add(optionB);
            optionsPanel.add(optionC);
            optionsPanel.add(optionD);
        }
        Panel downPanel = new Panel();
        downPanel.setPreferredSize(new Dimension(400, 150));

        downPanel.add(optionsPanel, BorderLayout.CENTER);
        rightPanel.add(downPanel, BorderLayout.SOUTH);


        // 显示按钮
        JPanel buttonPanel = new JPanel();
        JButton confirmButton = new JButton("确定");
        confirmButton.setBackground(Color.green);
        confirmButton.setFont(new Font("微软雅黑", Font.PLAIN, 18));
        JButton nextButton = new JButton("下一题");
        nextButton.setBackground(Color.blue);
        nextButton.setForeground(Color.white);
        nextButton.setFont(new Font("微软雅黑", Font.PLAIN, 18));
        if(questions.get(currentQuestionIndex).getisanswerd()){
            confirmButton.setEnabled(false);
        };
        if (index >= 49) {
            nextButton.setText("提交");
            nextButton.setBackground(Color.yellow);
            nextButton.setForeground(Color.black);
            nextButton.setEnabled(true);
        }
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!questions.get(currentQuestionIndex).getisanswerd() && answers[currentQuestionIndex] >= 0) {
                    confirmButton.setEnabled(false);
                    String answerStr = "";
                    switch (questions.get(currentQuestionIndex).getType()) {
                        case "判断题" -> {
                            if (answers[currentQuestionIndex] == 0) {
                                answerStr = "A";
                            } else {
                                answerStr = "B";
                            }
                        }
                        case "单选题" -> {
                            if (answers[currentQuestionIndex] == 0) {
                                answerStr = "A";
                            }
                            if (answers[currentQuestionIndex] == 1) {
                                answerStr = "B";
                            }
                            if (answers[currentQuestionIndex] == 2) {
                                answerStr = "C";
                            }
                            if (answers[currentQuestionIndex] == 3) {
                                answerStr = "D";
                            }
                        }
                        case "多选题" -> {
                            if (answers[currentQuestionIndex] == 2) {
                                answerStr = "AB";
                            }
                            if (answers[currentQuestionIndex] == 4) {
                                answerStr = "AC";
                            }
                            if (answers[currentQuestionIndex] == 8) {
                                answerStr = "AD";
                            }
                            if (answers[currentQuestionIndex] == 5) {
                                answerStr = "BC";
                            }
                            if (answers[currentQuestionIndex] == 9) {
                                answerStr = "BD";
                            }
                            if (answers[currentQuestionIndex] == 11) {
                                answerStr = "CD";
                            }
                            if (answers[currentQuestionIndex] == 6) {
                                answerStr = "ABC";
                            }
                            if (answers[currentQuestionIndex] == 13) {
                                answerStr = "BCD";
                            }
                            if (answers[currentQuestionIndex] == 14) {
                                answerStr = "ABCD";
                            }
                        }
                    }
                    if (answerStr.equals(questions.get(currentQuestionIndex).getAnswer())) {
                        score += 2;
                    } else {
                        count++;
                        // 设置解释题目
                        JTextArea textArea = new JTextArea(10, 30);
                        textArea.setLineWrap(true);
                        textArea.setText("回答错误!\n正确答案是："+questions.get(currentQuestionIndex).getAnswer()+"\n "+questions.get(currentQuestionIndex).getSolution());
                        JScrollPane scrollPane = new JScrollPane(textArea);
                        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

                        JOptionPane.showMessageDialog(frame, scrollPane, "提示", JOptionPane.WARNING_MESSAGE);
                    }
                    if (count > 5) {
                        JOptionPane.showMessageDialog(frame, "错误题目达到六个，考试结束！\n\n" +"您的总分是：" + score +  "\n您答对了" + score / 2 + "道题，\n" + "答错了6道题\n还有" + (88 - score) / 2 + "道题没回答。\n考试时间为："+(1800-remainingTime) / 60+"分"+(1800-remainingTime) % 60+"秒", "提示", JOptionPane.WARNING_MESSAGE);
                        update(id, score);
                        frame.dispose();
                    }
                    questions.get(currentQuestionIndex).setisanswerd(true);
                } else if (answers[currentQuestionIndex] < 0) {
                    JOptionPane.showMessageDialog(frame, "请输入答案后提交", "提示", JOptionPane.WARNING_MESSAGE);
                }
                //判断本题是否已经被回答。
                if (questions.get(currentQuestionIndex).getisanswerd()) {
                    optionA.setEnabled(false);
                    optionB.setEnabled(false);
                    optionC.setEnabled(false);
                    optionD.setEnabled(false);
                    option1A.setEnabled(false);
                    option2B.setEnabled(false);
                }
            }
        });
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(index < 49){
                    if (!questions.get(currentQuestionIndex).getisanswerd()&&answers[currentQuestionIndex] >= 0) {
                    confirmButton.doClick();
                    currentQuestionIndex++;
                    showQuestion(currentQuestionIndex);
                    }else {
                        currentQuestionIndex++;
                        showQuestion(currentQuestionIndex);
                    }
                }else {
                    if(questions.get(currentQuestionIndex).getisanswerd()) {
                        submitExam();
                    }
                    else if (!questions.get(currentQuestionIndex).getisanswerd()&&answers[currentQuestionIndex] < 0){
                        JOptionPane.showMessageDialog(frame, "请输入答案后提交", "提示", JOptionPane.WARNING_MESSAGE);
                    }else if(!questions.get(currentQuestionIndex).getisanswerd()&&answers[currentQuestionIndex] >= 0){
                        confirmButton.doClick();
                        submitExam();
                    }
                }

            }  }   );
        buttonPanel.add(confirmButton,BorderLayout.WEST);
        buttonPanel.add(nextButton,BorderLayout.CENTER);
        nextButton.setEnabled(true);
        downPanel.add(buttonPanel, BorderLayout.EAST);

        // 更新底部面板中的小方框颜色
        for (int i = 0; i < 50; i++) {
            JButton button = (JButton) bottomPanel.getComponent(i);
            if (i == index) {
                button.setBackground(Color.BLUE);
                button.setForeground(Color.WHITE);
            } else if (questions.get(i).getisanswerd()) {
                button.setBackground(Color.GREEN);
                button.setForeground(Color.WHITE);
            } else {
                button.setBackground(Color.WHITE);
                button.setForeground(Color.BLACK);
            }
        }

        frame.pack();
    }
    //更新倒计时
    private void updateRemainingTime() {
        int minutes = remainingTime / 60;
        int seconds = remainingTime % 60;
        if (seconds < 10) {
            timeLabel.setText(minutes + ":0" + seconds);
        } else {
            timeLabel.setText(minutes + ":" + seconds);
        }
//remainingTime / 60+":0"+remainingTime % 60;
    }
    //交卷判断
    private void submitExam() {
        for (int i = 0; i < 50; i++) {
            if (!questions.get(i).getisanswerd()) {
                JOptionPane.showMessageDialog(frame, "还有未答题目，请全部作答再提交", "提示", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }
        timer.stop();
        if(score>=90) {
            JOptionPane.showMessageDialog(frame, "考试结束！\n恭喜您通过了考试！\n" + "您的总分是：" + score + "\n您答对了" + score / 2 + "道题\n" + "答错了" + count + "道题\n考试时间为："+(1800-remainingTime) / 60+"分"+(1800-remainingTime) % 60+"秒", "提示", JOptionPane.PLAIN_MESSAGE);
        }else {
            JOptionPane.showMessageDialog(frame, "考试结束！\n很遗憾您没有通过。\n" + "您的总分是：" + score + "\n您答对了" + score / 2 + "道题\n" + "答错了" + count + "道题\n还有" + (50 - (score / 2) - count) + "道题没有完成\n考试时间为："+(1800-remainingTime) / 60+"分"+(1800-remainingTime) % 60+"秒", "提示", JOptionPane.PLAIN_MESSAGE);
        }
        update(id,score);
        frame.dispose();
    }
    private void submitExamtime() {
        if(score>=90) {
            JOptionPane.showMessageDialog(frame, "考试时间耗尽！\n恭喜您通过了考试！\n" + "您的总分是：" + score + "\n您答对了" + score / 2 + "道题\n" + "答错了" + count + "道题\\n还有\" + (50 - (score / 2) - count) + \"道题没有完成", "提示", JOptionPane.PLAIN_MESSAGE);
        }else {
            JOptionPane.showMessageDialog(frame, "考试时间耗尽！\n很遗憾您没有通过。\n" + "您的总分是：" + score + "\n您答对了" + score / 2 + "道题\n" + "答错了" + count + "道题\n还有" + (50 - (score / 2) - count) + "道题没有完成", "提示", JOptionPane.PLAIN_MESSAGE);
        }
        update(id,score);
        frame.dispose();
    }
    //上传数据方法
    public void update(String id,int score) {
        String url = "jdbc:mysql://localhost:3306/training";
        String user = "root";
        String password = "242648";

        try (Connection con = DriverManager.getConnection(url, user, password);
        ) {
            PreparedStatement ps = con.prepareStatement("INSERT INTO grade VALUES (?, NOW(), ?))");
            ps.setString(1, id);
            ps.setInt(2, score);
            ps.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    private static class Question {
        private String id;
        private String type;
        private String stem;
        private String a;
        private String b;
        private String c;
        private String d;
        private String answer;
        private String solution;
        private byte[] image;
        private boolean isanswerd = false;

        public Question(String type, String stem, String a, String b, String c, String d, String answer, String solution, byte[] image) {
            this.type = type;
            this.stem = stem;
            this.a = a;
            this.b = b;
            this.c = c;
            this.d = d;
            this.answer = answer;
            this.solution = solution;
            this.image=image;
        }
        public Question(String type, String stem, String a, String b, String c, String d, String answer, String solution) {
            this.type = type;
            this.stem = stem;
            this.a = a;
            this.b = b;
            this.c = c;
            this.d = d;
            this.answer = answer;
            this.solution = solution;
        }


        public boolean getisanswerd() {
            return isanswerd;
        }

        public void setisanswerd(boolean isanswerd) {
            this.isanswerd = isanswerd;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getStem() {
            return stem;
        }

        public void setStem(String stem) {
            this.stem = stem;
        }

        public String getA() {
            return a;
        }

        public void setA(String a) {
            this.a = a;
        }

        public String getB() {
            return b;
        }

        public void setB(String b) {
            this.b = b;
        }

        public String getC() {
            return c;
        }

        public void setC(String c) {
            this.c = c;
        }

        public String getD() {
            return d;
        }

        public void setD(String d) {
            this.d = d;
        }

        public String getAnswer() {
            return answer;
        }

        public void setAnswer(String answer) {
            this.answer = answer;
        }

        public String getSolution() {
            return solution;
        }

        public void setSolution(String solution) {
            this.solution = solution;
        }

        public byte[] getImage() {
            return image;
        }

        public void setImage(byte[] image) {
            this.image = image;
        }
    }
}
