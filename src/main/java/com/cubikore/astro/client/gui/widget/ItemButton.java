package com.cubikore.astro.client.gui.widget;

import com.cubikore.astro.client.gui.action.ButtonAction;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;

import java.util.function.Consumer;

public class ItemButton extends AstButton{
    private ItemStack stack;

    private Consumer<ButtonAction> onPressed;

    private String message;

    private int backColor = 0xff000000;

    public ItemButton(int x, int y, ItemStack stack, String message, Consumer<ButtonAction> onPressed) {
        super(x, y, 16, 16);
        this.stack = stack;
        this.onPressed = onPressed;
        this.message = message;
    }

    public void setBackColor(int color) {
        backColor = color;
    }

    public String getMessage() {
        return message;
    }

    @Override
    protected void onPressed(int mouseX, int mouseY) {
        if(onPressed != null)
            onPressed.accept(new ButtonAction(this, message));
    }

    @Override
    protected void hovering(int mouseX, int mouseY, AstButton button) {

    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float deltaTime) {
        int baseX = this.x;
        int baseY = this.y;
        int endX = this.x + width;
        int endY = this.y + height;

        int borderSize = 1;

        if(this.isHovered()) {
            context.fill(baseX - borderSize, baseY - borderSize, endX + borderSize, endY + borderSize, 0xffffffff);
        }

        if(this.isSelected()) {
            context.fill(baseX - borderSize, baseY - borderSize, endX + borderSize, endY + borderSize, 0xff4e88fc);
        }

        context.fill(baseX, baseY, endX, endY, backColor);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float deltaTime) {
        context.drawItem(stack, this.x, this.y);
    }
}
