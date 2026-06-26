package com.cubikore.astro.client.gui.widget;

import com.cubikore.astro.client.gui.text.FormattedText;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class TextStack extends AstWidget{
    private List<FormattedText> lines = new ArrayList<>();

    private int spacing;

    public TextStack(int x, int y, int width, int height, int spacing) {
        super(x, y, width, height);
        this.spacing = 8 + spacing;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY) {
        super.render(context, mouseX, mouseY);

        TextRenderer textRenderer = this.screen.getTextRenderer();

        int i = 0;
        for(FormattedText text : lines) {
            text.render(context, textRenderer, this.x, this.y + (spacing * i));
            i++;
        }
    }

    public int getTextWidth() {
        TextRenderer textRenderer = this.screen.getTextRenderer();

        int longest = 0;
        for(FormattedText line : lines) {
            int length = textRenderer.getWidth(line.getText());
            if(length > longest)
                longest = length;
        }

        return longest;
    }

    public void clearLines() {
        this.lines.clear();
    }

    public void addLine(FormattedText text) {
        this.lines.add(text);
    }
}
