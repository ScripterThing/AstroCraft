package com.cubikore.astro.client.gui.action;

import com.cubikore.astro.client.gui.widget.AstButton;

public class ButtonAction {
    private AstButton button;

    private String message;

    public ButtonAction(AstButton button, String message) {
        this.button = button;
        this.message = message;
    }

    public AstButton getButton() {
        return button;
    }
    public String getMessage() {
        return message;
    }
}
