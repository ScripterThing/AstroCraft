package com.cubikore.astro.client.gui.widget;

import com.cubikore.astro.client.gui.action.ButtonAction;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

import java.util.function.Consumer;

public class TextButton extends AstButton {
    private Text text;

    private String message;

    private Consumer<ButtonAction> onPressed;

    public TextButton(int x, int y, int width, int height, Text text, Consumer<ButtonAction> onPressed) {
        super(x, y, width, height);
        this.text = text;
        this.onPressed = onPressed;
    }

    public void setText(Text text) {
        this.text = text;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    protected void onPressed(int mouseX, int mouseY) {
        onPressed.accept(new ButtonAction(this, this.message));
    }

    @Override
    protected void hovering(int mouseX, int mouseY) {

    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float deltaTime) {
        TextRenderer textRenderer = this.screen.getTextRenderer();

        int textWidth = textRenderer.getWidth(text);
        int textHeight = 8;

        if(this.width == -1) {
            this.width = textWidth + 4;
        }

        int tX = this.x + (this.width / 2) - (textWidth / 2);
        int tY = this.y + (this.height / 2) - (textHeight / 2);

        context.drawText(textRenderer, this.text, tX, tY, 0xffffffff, false);
    }
}
