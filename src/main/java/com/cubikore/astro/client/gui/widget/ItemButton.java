package com.cubikore.astro.client.gui.widget;

import com.cubikore.astro.client.gui.AstroCraftGuiTextures;
import com.cubikore.astro.client.gui.action.ButtonAction;
import com.cubikore.astro.util.UIUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.texture.Sprite;
import net.minecraft.item.ItemStack;

import java.util.function.Consumer;

public class ItemButton extends AstButton{
    private ItemStack stack;

    private Consumer<ButtonAction> onPressed;

    private String message;

    public ItemButton(int x, int y, ItemStack stack, String message, Consumer<ButtonAction> onPressed) {
        super(x, y, 16, 16);
        this.stack = stack;
        this.onPressed = onPressed;
        this.message = message;
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
    protected void hovering(int mouseX, int mouseY) {

    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float deltaTime) {
        super.renderBackground(context, mouseX, mouseY, deltaTime);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float deltaTime) {
        MinecraftClient client = MinecraftClient.getInstance();

        Sprite disabledSprite = UIUtils.getSprite(context, AstroCraftGuiTextures.DISABLED);

        BakedModel model = client.getItemRenderer().getModel(stack, client.world, client.player, 0);
        Sprite itemSprite = model.getParticleSprite();

        float r = 1.0f;
        float g = 1.0f;
        float b = 1.0f;

        if(isDisabled()) {
            r = g = b = 0.5f;
            if(showingDisabledToolTip()) {
                context.drawTooltip(screen.getTextRenderer(), this.disabledToolTip, mouseX, mouseY);
            }
        }

        context.drawSprite(this.x, this.y, 0, this.width, this.height, itemSprite, r, g, b, 1);

        if(isDisabled())
            context.drawSprite(this.x, this.y, 0, this.width, this.height, disabledSprite, r, g, b, 1);
    }
}
