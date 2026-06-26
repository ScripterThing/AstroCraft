package com.cubikore.astro.client.gui.text;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

public class FormattedText {
    private MutableText text;

    private boolean indented;
    private boolean dotted;
    private boolean shadow = true;

    private int x;
    private int y;

    private int color = 0xffffffff;

    public FormattedText(int x, int y, String translation) {
        this.text = Text.translatable(translation);
        this.x = x;
        this.y = y;
        this.indented = false;
        this.dotted = false;
    }

    public FormattedText(int x, int y, MutableText text) {
        this.text = text;
        this.x = x;
        this.y = y;
        this.indented = false;
        this.dotted = false;
    }

    public static FormattedText empty() {
        return new FormattedText(0, 0, Text.empty());
    }

    public void render(DrawContext context, TextRenderer textRenderer, int cx, int cy) {
        String renderedText = this.text.getString();

        if(this.indented && !this.dotted) {
            renderedText = "    " + renderedText;
        }
        else if(this.dotted) {
            renderedText = "•   " + renderedText;
        }

        Style textStyle = this.text.getStyle();
        Text finalText = Text.literal(renderedText).setStyle(textStyle);

        context.drawText(textRenderer, finalText, cx + this.x, cy + this.y, this.color, this.shadow);
    }

    public MutableText getText() {
        return text;
    }

    public boolean isDotted() {
        return dotted;
    }

    public void setDotted(boolean dotted) {
        this.dotted = dotted;
    }

    public boolean isIndented() {
        return indented;
    }

    public void setIndented(boolean indented) {
        this.indented = indented;
    }

    public boolean hasShadow() {
        return shadow;
    }

    public void setShadow(boolean shadow) {
        this.shadow = shadow;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getColor() {
        return color;
    }
}
