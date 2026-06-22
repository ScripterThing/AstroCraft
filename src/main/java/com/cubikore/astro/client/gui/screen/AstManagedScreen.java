package com.cubikore.astro.client.gui.screen;

import com.cubikore.astro.client.gui.widget.AstButton;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class AstManagedScreen extends Screen {
    private List<AstButton> buttons = new ArrayList<>();

    protected AstManagedScreen(Text title) {
        super(title);
    }

    protected void addButton(AstButton button) {
        buttons.add(button);
    }

    protected void addButtons(AstButton... buttons) {
        this.buttons.addAll(List.of(buttons));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        for(AstButton button : buttons) {
            button.handle(mouseX, mouseY);
            button.renderBackground(context, mouseX, mouseY, delta);
            button.render(context, mouseX, mouseY, delta);
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
