package com.cubikore.astro.client.gui.widget;

import com.cubikore.astro.client.gui.screen.AstManagedScreen;
import net.minecraft.client.gui.DrawContext;

public class AstWidget {
    protected int x;
    protected int y;

    protected int width;
    protected int height;

    protected int backgroundColor = 0x00000000;

    protected AstManagedScreen screen;

    public AstWidget(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void render(DrawContext context, int mouseX, int mouseY) {
        renderBackground(context, mouseX, mouseY);
    }

    protected void renderBackground(DrawContext context, int mouseX, int mouseY) {
        context.fill(this.x, this.y, this.x + this.width, this.y + this.height, this.backgroundColor);
    }

    public void setScreen(AstManagedScreen screen) {
        this.screen = screen;
    }

    public void setBackgroundColor(int color) {
        backgroundColor = color;
    }

    public void setDimensions(int width, int height) {
        setWidth(width);
        setHeight(height);
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
