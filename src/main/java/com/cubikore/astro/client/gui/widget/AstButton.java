package com.cubikore.astro.client.gui.widget;

import com.cubikore.astro.client.gui.screen.AstManagedScreen;
import com.cubikore.astro.util.UIUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.joml.Vector2i;

import java.util.function.Consumer;

public abstract class AstButton {
    protected int x, y;
    protected int width, height;

    protected boolean hovered = false;
    protected boolean selected = false;
    protected boolean disabled = false;

    private Vector2i mousePos;

    protected AstManagedScreen screen;

    protected int backColor = 0x00000000;
    protected int deselectedBackColor = 0x00000000;
    protected int selectedColor = 0xff4e88fc;
    protected int hoverColor = 0xffffffff;
    protected int disabledColor = 0x47000000;

    protected int borderSize = 1;

    protected Text disabledToolTip = Text.empty();
    private boolean showDisabledToolTip = false;

    public AstButton(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.mousePos = new Vector2i(0);
    }

    public void setScreen(AstManagedScreen screen) {
        this.screen = screen;
    }

    protected abstract void onPressed(int mouseX, int mouseY);

    protected abstract void hovering(int mouseX, int mouseY);

    public void renderBackground(DrawContext context, int mouseX, int mouseY, float deltaTime) {
        int baseX = this.x;
        int baseY = this.y;
        int endX = this.x + width;
        int endY = this.y + height;

        if(!isSelected() && !isDisabled()) {
            renderBorder(context, borderSize, deselectedBackColor);
        }

        if(this.isHovered() && !isDisabled()) {
            renderBorder(context, borderSize, hoverColor);
        }

        if(this.isSelected() && !isDisabled()) {
            renderBorder(context, borderSize, selectedColor);
        }

        context.fill(baseX, baseY, endX, endY, backColor);
    }

    public abstract void render(DrawContext context, int mouseX, int mouseY, float deltaTime);

    protected void renderBorder(DrawContext context, int size, int color) {
        int baseX = this.x;
        int baseY = this.y;
        int endX = this.x + width;
        int endY = this.y + height;

        UIUtils.drawBorder(context, baseX, baseY, endX, endY, size, color);
    }

    public void handle(int mouseX, int mouseY) {
        this.mousePos.set(mouseX, mouseY);
        if(isHovering(mouseX, mouseY)) {
            if(!isDisabled()) {
                showDisabledToolTip = false;
                hovered = true;
                hovering(mouseX, mouseY);
            }
            else if(!disabledToolTip.equals(Text.empty()))
                showDisabledToolTip = true;
        }
        else {
            hovered = false;
            showDisabledToolTip = false;
        }
    }

    public void handlePress(int mouseX, int mouseY) {
        if(isHovering(mouseX, mouseY) && !isDisabled())
            this.onPressed(mouseX, mouseY);
    }

    public void setBackColor(int color) {
        backColor = color;
    }
    public void setSelectedColor(int color) {
        selectedColor = color;
    }
    public void setDeselectedBackColor(int color) {
        deselectedBackColor = color;
    }

    public void setHoverColor(int hoverColor) {
        this.hoverColor = hoverColor;
    }

    public void setBorderSize(int borderSize) {
        this.borderSize = borderSize;
    }

    public void setDisabledColor(int disabledColor) {
        this.disabledColor = disabledColor;
    }

    public void setDisabledToolTip(String text, boolean translate) {
        MutableText fText = translate ? Text.translatable(text) : Text.literal(text);
        disabledToolTip = fText.formatted(Formatting.RED);
    }

    public void setDisabledToolTip(MutableText text) {
        disabledToolTip = text.formatted(Formatting.RED);
    }

    protected boolean showingDisabledToolTip() {
        return showDisabledToolTip;
    }

    public void setSelected(boolean bl) {
        this.selected = bl;
    }

    public boolean isSelected() {
        return selected;
    }

    public boolean isHovered() {
        return hovered;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public boolean isDisabled() {
        return disabled;
    }

    private boolean isHovering(int mouseX, int mouseY) {
        return mouseX >= this.x && mouseY >= this.y && mouseX <= this.x + this.width && mouseY <= this.y + this.height;
    }
}
