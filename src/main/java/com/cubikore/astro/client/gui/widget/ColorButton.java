package com.cubikore.astro.client.gui.widget;

import com.cubikore.astro.client.gui.action.ColorButtonAction;
import com.cubikore.astro.math.AstMath;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.PressableWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.function.Consumer;

public class ColorButton extends AstButton {
    private String colorString;
    private int color;

    private boolean needsConversion = false;

    private Consumer<ColorButtonAction> onPressed;

    public ColorButton(int x, int y, int width, int height, Consumer<ColorButtonAction> onPressed) {
        super(x, y, width, height);
        this.onPressed = onPressed;
    }

    public void setColor(String colorString) {
        Formatting colorFort = Formatting.byName(colorString);
        if(colorFort != null && colorFort.getColorValue() != null) {
            this.colorString = colorString;
            this.color = colorFort.getColorValue();
        }
        else {
            throw new IllegalArgumentException("Not a supported color '" + colorString + "'");
        }
    }

    public int getColor() {
        return color;
    }

    public void setNeedsConversion(boolean bl) {
        this.needsConversion = true;
    }

    @Override
    protected void onPressed(int mouseX, int mouseY) {
        this.onPressed.accept(new ColorButtonAction(colorString, color, this));
    }

    @Override
    protected void hovering(int mouseX, int mouseY, AstButton button) {

    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float deltaTime) {

    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float deltaTime) {
        int x1 = this.x;
        int y1 = this.y;
        int x2 = this.x + this.width;
        int y2 = this.y + this.height;

        if(this.isHovered()) {
            int borderWidth = 1;
            context.fill(x1 - borderWidth, y1 - borderWidth, x2 + borderWidth, y2 + borderWidth, 0xffffffff);
        }

        context.fill(x1, y1, x2, y2, AstMath.toARGB(color));
    }
}
