package com.teradata.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.teradata.ui.MainConfig;
import com.teradata.ui.UI;

public class DolphinTemplate extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        MainConfig mainConfig = new MainConfig();
        mainConfig.setTitle("DolphinTemplate");
        mainConfig.pack();
        mainConfig.setVisible(true);
    }
}
