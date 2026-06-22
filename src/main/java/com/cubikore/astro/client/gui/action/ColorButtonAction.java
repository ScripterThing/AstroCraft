package com.cubikore.astro.client.gui.action;

import com.cubikore.astro.client.gui.widget.AstButton;

public class ColorButtonAction extends ButtonAction {
    private String colorString;
    private int color;

    public ColorButtonAction(String colorString, int color, AstButton button) {
        super(button, "");
        this.colorString = colorString;
        this.color = color;
    }

    public String getColorString() {
        return colorString;
    }

    public int getColor() {
        return color;
    }
}
