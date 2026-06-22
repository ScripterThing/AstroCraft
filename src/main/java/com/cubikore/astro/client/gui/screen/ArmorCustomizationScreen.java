package com.cubikore.astro.client.gui.screen;

import com.cubikore.astro.AstroCraft;
import com.cubikore.astro.client.gui.action.ButtonAction;
import com.cubikore.astro.client.gui.action.ColorButtonAction;
import com.cubikore.astro.client.gui.widget.ColorButton;
import com.cubikore.astro.client.gui.widget.ItemButton;
import com.cubikore.astro.item.AstroCraftItems;
import com.cubikore.astro.networking.payload.ArmorCustomizationPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ArmorCustomizationScreen extends AstManagedScreen {
    public static Text TITLE = Text.literal("Armor Customization");

    private final List<String> colors = new ArrayList<>();

    private int originX = 0;
    private int originY = 0;

    private ItemButton selectedPiece;

    public ArmorCustomizationScreen() {
        super(TITLE);

        colors.addAll(sanitize(Formatting.getNames(true, false)));
    }

    @Override
    protected void init() {
        super.init();

        this.width = 270;
        this.height = 100;

        int half = 2 * client.options.getGuiScale().getValue();

        originX = (client.getFramebuffer().viewportWidth / half) - (this.width / 2);
        originY = (client.getFramebuffer().viewportHeight / half) - (this.height / 2);

        int halfWidth = this.width / 2;
        int halfHeight = this.height / 2;

        int xSpacing = 20;
        int ySpacing = 20;

        int baseX = originX + halfWidth + 50;
        int baseY = originY + halfHeight - 50;

        int columbNum = 0;
        int lineNum = 0;

        for(int i = 0; i < colors.size(); i++) {
            columbNum = i % 4;
            if(i % 4 == 0)
                lineNum++;

            ColorButton button = new ColorButton(baseX + (xSpacing * columbNum), baseY + (ySpacing * lineNum), 15, 10, this::colorPressed);
            button.setColor(colors.get(i));
            button.setNeedsConversion(true);

            addButton(button);
        }

        ItemStack helmet = new ItemStack(AstroCraftItems.SPACE_SUIT_HELMET);
        ItemStack chestplate = new ItemStack(AstroCraftItems.SPACE_SUIT_CHESTPLATE);
        ItemStack leggings = new ItemStack(AstroCraftItems.SPACE_SUIT_LEGGINGS);
        ItemStack boots = new ItemStack(AstroCraftItems.SPACE_SUIT_BOOTS);

        int iWidth = 16;
        int iHeight = 16;

        int bWidth = iWidth / 2;
        int bHeight = iHeight / 2;

        int spacing = 4;

        int plBaseX = (originX + (this.width / 2)) - bWidth;
        int plBaseY = ((originY + (this.height / 2)) - 50) - bHeight;

        ItemButton helmetButton = new ItemButton(plBaseX, plBaseY + ((iHeight + spacing)), helmet, "head", this::armorPressed);
        helmetButton.setBackColor(0xff353535);

        ItemButton chestplateButton = new ItemButton(plBaseX, plBaseY + ((iHeight + spacing) * 2), chestplate, "chest", this::armorPressed);
        chestplateButton.setBackColor(0xff353535);

        ItemButton leggingsButton = new ItemButton(plBaseX, plBaseY + ((iHeight + spacing) * 3), leggings, "legs", this::armorPressed);
        leggingsButton.setBackColor(0xff353535);

        ItemButton bootsButton = new ItemButton(plBaseX, plBaseY + ((iHeight + spacing) * 4), boots, "feet", this::armorPressed);
        bootsButton.setBackColor(0xff353535);

        addButtons(helmetButton, chestplateButton, leggingsButton, bootsButton);
    }

    private void armorPressed(ButtonAction action) {
        selectPiece((ItemButton) action.getButton());
    }

    private void selectPiece(ItemButton button) {
        if(selectedPiece != null)
            selectedPiece.setSelected(false);

        selectedPiece = button;
        selectedPiece.setSelected(true);
    }

    private void colorPressed(ColorButtonAction action) {
        if(selectedPiece != null) {
            String color = action.getColorString();
            String piece = selectedPiece.getMessage();

            ClientPlayNetworking.send(new ArmorCustomizationPayload(piece, color));
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        int halfWidth = this.width / 2;
        int halfHeight = this.height / 2;

        int baseX = originX + halfWidth - 85;
        int baseY = originY + halfHeight - 40;

        int enWidth = 45;
        int enHeight = 85;

        InventoryScreen.drawEntity(context, baseX, baseY, baseX + enWidth, baseY + enHeight, 40, 0.0625f, mouseX, mouseY, client.player);

        renderSuitSelection(context);

        int tBaseX = originX + halfWidth + 50;
        int tBaseY = originY + 4;

        context.drawText(this.textRenderer, "Color Selection", tBaseX, tBaseY, 0xffffff, true);
    }

    private void renderSuitSelection(DrawContext context) {
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        int scale = client.options.getGuiScale().getValue();

        int hWidth = client.getFramebuffer().viewportWidth / scale;
        int hHeight = client.getFramebuffer().viewportHeight / scale;

        this.applyBlur(delta);
        this.renderDarkening(context, 0, 0, hWidth, hHeight);
        context.fill(originX, originY, originX + this.width, originY + this.height, 0xff898989);

        int iWidth = 16;
        int iHeight = 16;

        int bWidth = iWidth / 2;
        int bHeight = iHeight / 2;

        int spacing = 4;

        int plBaseX = (originX + (this.width / 2)) - bWidth;
        int plBaseY = ((originY + (this.height / 2)) - 50) - bHeight;

        int border = 2;

        context.fill(plBaseX - border, originY, plBaseX + iWidth + border, originY + this.height, 0xff353535);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    private Collection<String> sanitize(Collection<String> collection) {
        collection.remove("reset");
        return collection;
    }
}
