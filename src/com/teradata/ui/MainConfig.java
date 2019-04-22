package com.teradata.ui;

import com.intellij.ide.util.PropertiesComponent;

import javax.swing.*;
import java.awt.event.*;

public class MainConfig extends JDialog {
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

    private PropertiesComponent config;

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
