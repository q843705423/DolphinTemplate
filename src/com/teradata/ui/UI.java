package com.teradata.ui;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.Messages;
import com.teradata.db.DB;
import com.teradata.db.DataBaseUtil;
import com.teradata.db.DatabaseFactory;
import com.teradata.db.FieldInfo;
import com.teradata.document.DocumentWrite;
import com.teradata.template.Template;
import com.teradata.template.TemplateMachine;

import javax.swing.*;
import java.awt.event.*;
import java.io.File;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class UI extends JDialog {
//    private static String ERROR_INFO = "CQQTemplate";

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField textFieldObjectName;
    private JTextField textFieldClassName;
    private JTextField textFieldTitle;
    private JTextField textFieldKey;
    private JTextField textFieldTableName;
    private JFileChooser fileChoose;
    private JFileChooser templateChoose;
    private AnActionEvent anActionEvent;

    public void setAnActionEvent(AnActionEvent anActionEvent) {
        this.anActionEvent = anActionEvent;
    }

    public UI() {


        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        this.setLocationRelativeTo(null);
        buttonOK.addActionListener(e -> onOK());
        buttonCancel.addActionListener(e -> onCancel());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        // add your code here
        String key = textFieldKey.getText();
        String className = textFieldClassName.getText();
        String objectName = textFieldObjectName.getText();
        String title = textFieldTitle.getText();
        String tableName = textFieldTableName.getText();
        File databaseConfig = fileChoose.getSelectedFile();
        File template = templateChoose.getSelectedFile();
        System.out.println(key);
        System.out.println(className);
        System.out.println(objectName);
        System.out.println(title);
        System.out.println(tableName);
        System.out.println(databaseConfig);
        String fileName = Template.PATH + template.getAbsolutePath();
        HashMap<String, String> config = null;
        String ERROR_TITLE = "CQQTemplate";
        DataBaseUtil db;
        try {
            db = DatabaseFactory.readFile(databaseConfig);
        } catch (Exception e) {
            Messages.showErrorDialog(e.getMessage(), ERROR_TITLE);
            return;
        }

        List<FieldInfo> list = null;
        try {
            list = db.getTableFieldsBySql(tableName);
        } catch (SQLException e) {
            Messages.showErrorDialog(e.getMessage(), ERROR_TITLE);
            return;
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("key", key);
        map.put("className", className);
        map.put("objectName", objectName);
        map.put("title", title);
        List<HashMap<String, Object>> fieldList = list.stream().map(fieldInfo -> {
            HashMap<String, Object> map1 = new HashMap<>();
            String property = DB.underscore2hump(fieldInfo.getFieldName());
            map1.put("property", property);
            map1.put("label", fieldInfo.getCommit());
            return map1;
        }).collect(Collectors.toList());
        System.out.println(fieldList);
        map.put("fieldList", fieldList);
        String content = null;
        try {
            content = new Template().getContent(fileName);
        } catch (Exception e) {
            Messages.showErrorDialog(e.getMessage(), ERROR_TITLE);
            return;
//            e.printStackTrace();
        }

        String now = null;
        try {
            now = TemplateMachine.dfs(content, map);
        } catch (Exception e) {
            Messages.showErrorDialog(e.getMessage(),ERROR_TITLE);
            return;
        }
        DocumentWrite documentWrite = new DocumentWrite(anActionEvent);
        while (now.contains("\r\n")) {
            now = now.replace("\r\n", "\n");
        }
        documentWrite.set(now);


        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        UI dialog = new UI();
        dialog.pack();
        dialog.setVisible(true);
    }
}
