package com.cubikore.astro.client.gui.widget;

import com.cubikore.astro.client.gui.action.ColorButtonAction;
import com.cubikore.astro.client.gui.screen.ArmorCustomizationScreen;
import com.cubikore.astro.client.gui.screen.AstManagedScreen;
import com.cubikore.astro.math.AstMath;
import com.cubikore.astro.util.ColorUtil;
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

    @Override
    protected void onPressed(int mouseX, int mouseY) {
        this.onPressed.accept(new ColorButtonAction(colorString, color, this));
    }

    @Override
    protected void hovering(int mouseX, int mouseY) {

    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float deltaTime) {
        super.renderBackground(context, mouseX, mouseY, deltaTime);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float deltaTime) {
        int x1 = this.x;
        int y1 = this.y;
        int x2 = this.x + this.width;
        int y2 = this.y + this.height;

        if(this.isHovered()) {
            Formatting formatting = Formatting.byName(colorString);

            context.drawTooltip(screen.getTextRenderer(), Text.translatable("color.astrocraft." + colorString).formatted(formatting), mouseX, mouseY);
        }

        context.fill(x1, y1, x2, y2, ColorUtil.toARGB(color));
    }
}
