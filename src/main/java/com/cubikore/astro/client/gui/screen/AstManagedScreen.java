package com.cubikore.astro.client.gui.screen;

import com.cubikore.astro.client.gui.widget.AstButton;
import com.cubikore.astro.client.gui.widget.AstWidget;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class AstManagedScreen extends Screen {
    private List<AstButton> buttons = new ArrayList<>();
    private List<AstWidget> widgets = new ArrayList<>();

    protected AstManagedScreen(Text title) {
        super(title);
    }

    protected void addButton(AstButton button) {
        button.setScreen(this);
        buttons.add(button);
    }

    protected void addButtons(AstButton... buttons) {
        for(AstButton button : buttons) {
            addButton(button);
        }
    }

    protected void addWidget(AstWidget widget) {
        widget.setScreen(this);
        widgets.add(widget);
    }

    protected void addWidgets(AstWidget... widgets) {
        for(AstWidget widget : widgets) {
            addWidget(widget);
        }
    }

    protected void clearButtons() {
        this.buttons.clear();
    }

    public TextRenderer getTextRenderer() {
        return this.textRenderer;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        for(AstWidget widget : widgets) {
            widget.render(context, mouseX, mouseY);
        }

        for(AstButton button : buttons) {
            if(button.allowRendering) {
                button.handle(mouseX, mouseY);
                button.renderBackground(context, mouseX, mouseY, delta);
                button.render(context, mouseX, mouseY, delta);
            }
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if(button == GLFW.GLFW_MOUSE_BUTTON_1) {
            for(AstButton sButton : buttons) {
                sButton.handlePress((int) mouseX, (int) mouseY);
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }
}
