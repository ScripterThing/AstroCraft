package com.cubikore.astro.client.gui.widget;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import org.joml.Vector2i;

import java.util.function.Consumer;

public abstract class AstButton {
    protected int x, y;
    protected int width, height;

    protected boolean hovered = false;
    protected boolean selected = false;

    private Vector2i mousePos;

    public AstButton(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.mousePos = new Vector2i(0);
    }

    protected abstract void onPressed(int mouseX, int mouseY);

    protected abstract void hovering(int mouseX, int mouseY, AstButton button);

    public abstract void renderBackground(DrawContext context, int mouseX, int mouseY, float deltaTime);

    public abstract void render(DrawContext context, int mouseX, int mouseY, float deltaTime);

    public void handle(int mouseX, int mouseY) {
        this.mousePos.set(mouseX, mouseY);
        if(isHovering(mouseX, mouseY)) {
            hovered = true;
            hovering(mouseX, mouseY, this);
        }
        else {
            hovered = false;
        }
    }

    public void setSelected(boolean bl) {
        this.selected = bl;
    }

    public boolean isSelected() {
        return selected;
    }

    protected boolean isHovered() {
        return hovered;
    }

    public void handlePress(int mouseX, int mouseY) {
        if(isHovering(mouseX, mouseY))
            this.onPressed(mouseX, mouseY);
    }

    private boolean isHovering(int mouseX, int mouseY) {
        return mouseX >= this.x && mouseY >= this.y && mouseX <= this.x + this.width && mouseY <= this.y + this.height;
    }
}
