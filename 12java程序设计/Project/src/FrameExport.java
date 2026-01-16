import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableImage;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class FrameExport extends JFrame {

    private JTextField pathField;

    public FrameExport() {
        super("导出题库");
        initUI();
    }

    private void initUI() {
        JPanel contentPanel = new JPanel(new BorderLayout());

        // 文件路径
        JPanel pathPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel pathLabel = new JLabel("文件路径：");
        pathField = new JTextField(25);
        pathField.setEditable(false);
        pathPanel.add(pathLabel);
        pathPanel.add(pathField);

        // 选择导出路径按钮
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton selectButton = new JButton("选择路径");
        selectButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("Excel文件", "xls"));
            int result = fileChooser.showSaveDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                if (!selectedFile.getName().endsWith(".xls")) {
                    selectedFile = new File(selectedFile.getAbsolutePath() + ".xls");
                }
                pathField.setText(selectedFile.getAbsolutePath());
            }
        });
        buttonPanel.add(selectButton);

        // 确认和取消按钮
        JPanel buttonContainerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton confirmButton = new JButton("确认导出");
        confirmButton.addActionListener(e -> {
            exportAction();
        });
        buttonContainerPanel.add(confirmButton);
        JButton cancelButton = new JButton("取消");
        cancelButton.addActionListener(e -> {
            dispose();
        });
        buttonContainerPanel.add(cancelButton);

        // 添加各个面板到内容面板
        JPanel centerPanel = new JPanel();
        centerPanel.add(buttonPanel);
        contentPanel.add(centerPanel, BorderLayout.NORTH);
        contentPanel.add(pathPanel, BorderLayout.CENTER);
        contentPanel.add(buttonContainerPanel, BorderLayout.SOUTH);
        add(contentPanel);

        pack();
        setResizable(false);
        setVisible(true);
        setLocationRelativeTo(null);
    }

    private void exportAction() {
        // 检查输入值是否为空
        if (pathField.getText() == null || pathField.getText().equals("")) {
            showError("请选择要导出的文件");
            return;
        }

        // 创建 Workbook 对象，创建 Excel 文件
        WritableWorkbook wwb = null;
        try {
            File file = new File(pathField.getText());
            wwb = Workbook.createWorkbook(file);
            // 创建 Sheet 对象，设置 Sheet 名称
            WritableSheet ws = wwb.createSheet("题目列表", 0);
            // 添加表头
            Label idLable = new Label(0,0,"id");
            Label typeLabel = new Label(1, 0, "类型");
            Label stemLabel = new Label(2, 0, "题干");
            Label aLabel = new Label(3, 0, "选项A");
            Label bLabel = new Label(4, 0, "选项B");
            Label cLabel = new Label(5, 0, "选项C");
            Label dLabel = new Label(6, 0, "选项D");
            Label answerLabel = new Label(7, 0, "答案");
            Label solutionLabel = new Label(8, 0, "解析");
            Label imageLable = new Label(9,0,"图片");
            ws.addCell(idLable);
            ws.addCell(typeLabel);
            ws.addCell(stemLabel);
            ws.addCell(aLabel);
            ws.addCell(bLabel);
            ws.addCell(cLabel);
            ws.addCell(dLabel);
            ws.addCell(answerLabel);
            ws.addCell(solutionLabel);
            ws.addCell(imageLable);

            // 从数据库中读取题目信息，并将信息写入 Excel 文件中
            Connection conn = null;
            Statement stmt = null;
            ResultSet rs = null;
            try {
                conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/training", "root", "242648");
                stmt = conn.createStatement();

                // 定义 SQL 查询语句
                String sql = "SELECT id,type, stem, a, b, c, d, answer, solution,image FROM allquestion";

                // 执行查询操作
                rs = stmt.executeQuery(sql);

                // 将查询结果写入 Excel 文件中
                int row = 1;
                while (rs.next()) {
                    String id = rs.getString("id");
                    String type = rs.getString("type");
                    String stem = rs.getString("stem");
                    String a = rs.getString("a");
                    String b = rs.getString("b");
                    String c = rs.getString("c");
                    String d = rs.getString("d");
                    String answer = rs.getString("answer");
                    String solution = rs.getString("solution");

                    // 插入图片到单元格
                    if (rs.getBytes("image") != null) {
                        byte[] imageBytes = rs.getBytes("image");
                        InputStream is = new ByteArrayInputStream(imageBytes);
                        BufferedImage image = ImageIO.read(is);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        ImageIO.write(image, "png", baos);
                        byte[] imageData = baos.toByteArray();
                        WritableImage writableImage = new WritableImage(9, row, 1, 1, imageData);
                        ws.addImage(writableImage);
                    }

                    Label idCell = new Label(0,row,id);
                    Label typeCell = new Label(1, row, type);
                    Label stemCell = new Label(2, row, stem);
                    Label aCell = new Label(3, row, a);
                    Label bCell = new Label(4, row, b);
                    Label cCell = new Label(5, row, c);
                    Label dCell = new Label(6, row, d);
                    Label answerCell = new Label(7, row, answer);
                    Label solutionCell = new Label(8, row, solution);


                    ws.addCell(idCell);
                    ws.addCell(typeCell);
                    ws.addCell(stemCell);
                    ws.addCell(aCell);
                    ws.addCell(bCell);
                    ws.addCell(cCell);
                    ws.addCell(dCell);
                    ws.addCell(answerCell);
                    ws.addCell(solutionCell);
                    row++;
                }

                wwb.write();
                showInfo("导出成功");
            } catch (SQLException e) {
                showError(e.getMessage());
            } finally {
                try {
                    if (rs != null) {
                        rs.close();
                    }
                    if (stmt != null) {
                        stmt.close();
                    }
                } catch (SQLException e) {
                    showError(e.getMessage());
                }
            }
        } catch (Exception e) {
            showError(e.getMessage());
        } finally {
            if (wwb != null) {
                try {
                    wwb.close();
                } catch (Exception e) {
                    showError(e.getMessage());
                }
            }
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "错误提示", JOptionPane.ERROR_MESSAGE);
    }

    private void showInfo(String message) {
        JOptionPane.showMessageDialog(this, message, "提示", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }
}
