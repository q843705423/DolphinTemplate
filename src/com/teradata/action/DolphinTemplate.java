package com.teradata.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.command.CommandProcessor;
import com.teradata.document.DocumentWrite;
import com.teradata.ui.MainConfig;

public class DolphinTemplate extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        MainConfig mainConfig = new MainConfig();
        mainConfig.setTitle("DolphinTemplate");
        mainConfig.setAnAnction(e);
        mainConfig.pack();
        mainConfig.setVisible(true);
        CommandProcessor.getInstance().runUndoTransparentAction(() -> {
            DocumentWrite documentWrite = new DocumentWrite(e);
            String value = mainConfig.value;
            documentWrite.set(value);

        });
    }
}
