package com.teradata.ui;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.ui.Messages;
import com.teradata.db.DataBaseUtil;
import com.teradata.db.DataPrepare;
import com.teradata.db.DatabaseFactory;
import com.teradata.document.DocumentWrite;
import com.teradata.template.Template;
import com.teradata.template.TemplateMachine;

import javax.swing.*;
import java.awt.event.*;
import java.io.File;
import java.util.HashMap;

public class MainConfig extends JDialog {
    private static final String ERROR_TITLE = "DolphinTemplate";
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField classNameText;
    private JTextField objectNameText;
    private JTextField titleText;
    private JTextField keyText;
    private JTextField tableNameText;
    private JTextField databaseConfigText;
    private JButton databaseChooseButton;
    private JTextField templateConfigText;
    private JButton templateChooseButton;

    private AnActionEvent anAnction;
    private PropertiesComponent config;
    private DataBaseUtil db;

    public String value ;
    public void setAnAnction(AnActionEvent anAnction) {
        this.anAnction = anAnction;
    }

    public MainConfig() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        readConfigToShow();


        buttonOK.addActionListener(e -> onOK());

        templateConfigText.setEditable(false);
        databaseConfigText.setEditable(false);

        buttonCancel.addActionListener(e -> onCancel());
        templateChooseButton.addActionListener(e -> {
            JFileChooser jFileChooser = new JFileChooser();
            jFileChooser.setVisible(true);
            jFileChooser.addActionListener(l -> {
                System.out.println(l);
                if (l.getActionCommand().equalsIgnoreCase("ApproveSelection")) {
                    String path = jFileChooser.getSelectedFile().getAbsolutePath();
                    templateConfigText.setText(path);
                }
            });
            jFileChooser.showOpenDialog(null);
        });

        databaseChooseButton.addActionListener(e -> {
            JFileChooser jFileChooser = new JFileChooser();
            jFileChooser.setVisible(true);
            jFileChooser.addActionListener(l -> {
                System.out.println(l);
                if (l.getActionCommand().equalsIgnoreCase("ApproveSelection")) {
                    String path = jFileChooser.getSelectedFile().getAbsolutePath();
                    databaseConfigText.setText(path);
                }
            });
            jFileChooser.showOpenDialog(null);
        });
        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void readConfigToShow() {
        config = PropertiesComponent.getInstance();
        String className = config.getValue("className");
        if (className != null) {
            classNameText.setText(className);
        }
        String objectName = config.getValue("objectName");
        if (objectName != null) {
            objectNameText.setText(objectName);
        }
        String key = config.getValue("key");
        if (key != null) {
            keyText.setText(key);
        }
        String tableName = config.getValue("tableName");
        if (tableName != null) {
            tableNameText.setText(tableName);
        }
        String title = config.getValue("title");
        if (title != null) {
            titleText.setText(title);
        }
        String databaseConfig = config.getValue("databaseConfig");
        if (databaseConfig != null) {
            databaseConfigText.setText(databaseConfig);
        }

        String templateConfig = config.getValue("templateConfig");
        if (templateConfig != null) {
            templateConfigText.setText(templateConfig);
        }
    }

    private void onOK() {
        // add your code here
        saveConfig();
        if (databaseConfigText == null || databaseConfigText.equals("")) {
            Messages.showErrorDialog("数据库配置不能为空", "");
        }
        File file = new File(databaseConfigText.getText());
        if (file.exists() == false) {
            Messages.showErrorDialog("该文件已经不存在,请检查", "");
            return;
        }
        try {
            db = DatabaseFactory.readFile(file);
        } catch (Exception e) {
            Messages.showErrorDialog(e.getMessage(), "");
            return;
        }
        String key = keyText.getText();
        String className = classNameText.getText();
        String objectName = objectNameText.getText();
        String title = titleText.getText();
        String tableName = tableNameText.getText();
        HashMap<String, Object> map;
        try {
            map = DataPrepare.getData(db, key, className, objectName, title, tableName);
        } catch (Exception e) {
            Messages.showErrorDialog(e.getMessage(), "");
            return;
        }
        System.out.println(map);
        Template template = new Template();
        String content = null;
        try {
            content = template.getContent("file:" + templateConfigText.getText());
        } catch (Exception e) {
            Messages.showErrorDialog(e.getMessage(), "");
        }
        String now = null;
        try {
            now = TemplateMachine.dfs(content, map);
            System.out.println(now);
        } catch (Exception e) {

            Messages.showErrorDialog(e.getMessage(), ERROR_TITLE);
            return;
        }
        value = now;
        while (value.contains("\r\n")) value = value.replace("\r\n","\n");

//        DocumentWrite documentWrite = new DocumentWrite(an);
//        while (now.contains("\r\n")) {
//            now = now.replace("\r\n", "\n");
//        }
//        documentWrite.set(now);
        dispose();
    }

    private void saveConfig() {
        config.setValue("databaseConfig", databaseConfigText.getText());
        config.setValue("templateConfig", templateConfigText.getText());
        config.setValue("key", keyText.getText());
        config.setValue("tableName", tableNameText.getText());
        config.setValue("title", titleText.getText());
        config.setValue("objectName", objectNameText.getText());
        config.setValue("className", classNameText.getText());
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        MainConfig dialog = new MainConfig();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
