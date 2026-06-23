package com.cubikore.astro.client.gui.screen;

import com.cubikore.astro.AstroCraft;
import com.cubikore.astro.client.gui.action.ButtonAction;
import com.cubikore.astro.client.gui.action.ColorButtonAction;
import com.cubikore.astro.client.gui.widget.ColorButton;
import com.cubikore.astro.client.gui.widget.ItemButton;
import com.cubikore.astro.item.AstroCraftItems;
import com.cubikore.astro.networking.payload.ArmorCustomizationPayload;
import com.cubikore.astro.util.Colors;
import com.cubikore.astro.util.EntityUtils;
import com.cubikore.astro.util.UIUtils;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
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

    private ItemButton helmetButton;
    private ItemButton chestplateButton;
    private ItemButton leggingsButton;
    private ItemButton bootsButton;

    private ItemButton selectedPiece;

    public ArmorCustomizationScreen() {
        super(TITLE);

        colors.add(Formatting.BLACK.getName());
        colors.add(Formatting.DARK_GRAY.getName());
        colors.add(Formatting.GRAY.getName());
        colors.add(Formatting.WHITE.getName());

        colors.add(Formatting.DARK_BLUE.getName());
        colors.add(Formatting.DARK_AQUA.getName());
        colors.add(Formatting.BLUE.getName());
        colors.add(Formatting.AQUA.getName());

        colors.add(Formatting.DARK_GREEN.getName());
        colors.add(Formatting.GOLD.getName());
        colors.add(Formatting.GREEN.getName());
        colors.add(Formatting.YELLOW.getName());

        colors.add(Formatting.DARK_RED.getName());
        colors.add(Formatting.DARK_PURPLE.getName());
        colors.add(Formatting.RED.getName());
        colors.add(Formatting.LIGHT_PURPLE.getName());
    }

    @Override
    protected void init() {
        super.init();

        setUpScreen();
    }

    @Override
    public void resize(MinecraftClient client, int width, int height) {
        super.resize(client, width, height);
        setUpScreen();
    }

    private void setUpScreen() {
        this.clearButtons();

        helmetButton = null;
        chestplateButton = null;
        leggingsButton = null;
        bootsButton = null;
        selectedPiece = null;

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

        int spacing = 8;

        int plBaseX = (originX + (this.width / 2)) - bWidth;
        int plBaseY = ((originY + (this.height / 2)) - 50) - bHeight;
        plBaseY -= 6;

        helmetButton = new ItemButton(plBaseX, plBaseY + ((iHeight + spacing)), helmet, "head", this::armorPressed);
        helmetButton.setDisabledToolTip(Text.translatable("text.astrocraft.armor_selection.disabled.head"));

        chestplateButton = new ItemButton(plBaseX, plBaseY + ((iHeight + spacing) * 2), chestplate, "chest", this::armorPressed);
        chestplateButton.setDisabledToolTip(Text.translatable("text.astrocraft.armor_selection.disabled.chest"));

        leggingsButton = new ItemButton(plBaseX, plBaseY + ((iHeight + spacing) * 3), leggings, "legs", this::armorPressed);
        leggingsButton.setDisabledToolTip(Text.translatable("text.astrocraft.armor_selection.disabled.legs"));

        bootsButton = new ItemButton(plBaseX, plBaseY + ((iHeight + spacing) * 4), boots, "feet", this::armorPressed);
        bootsButton.setDisabledToolTip(Text.translatable("text.astrocraft.armor_selection.disabled.feet"));

        addButtons(helmetButton, chestplateButton, leggingsButton, bootsButton);

        updateArmorButtons();
        selectBest();
    }

    private void armorPressed(ButtonAction action) {
        selectPiece((ItemButton) action.getButton());
    }

    private void updateArmorButtons() {
        helmetButton.setDisabled(!EntityUtils.wearingSpaceSuit(client.player, EquipmentSlot.HEAD));
        chestplateButton.setDisabled(!EntityUtils.wearingSpaceSuit(client.player, EquipmentSlot.CHEST));
        leggingsButton.setDisabled(!EntityUtils.wearingSpaceSuit(client.player, EquipmentSlot.LEGS));
        bootsButton.setDisabled(!EntityUtils.wearingSpaceSuit(client.player, EquipmentSlot.FEET));
    }

    private void selectBest() {
        if(!helmetButton.isDisabled()) {
            this.selectPiece(helmetButton);
        }
        else if(!chestplateButton.isDisabled()) {
            this.selectPiece(chestplateButton);
        }
        else if(!leggingsButton.isDisabled()) {
            this.selectPiece(leggingsButton);
        }
        else if(!bootsButton.isDisabled()) {
            this.selectPiece(bootsButton);
        }
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

        int baseX = originX + halfWidth - 120;
        int baseY = originY + halfHeight - 60;

        int enWidth = 80;
        int enHeight = 120;

        int backSize = 2;

        context.fill(baseX - backSize, baseY - backSize, baseX + enWidth + backSize, baseY + enHeight + backSize, 0x96000000);
        UIUtils.drawBorder(context, baseX - backSize, baseY - backSize, baseX + enWidth + backSize, baseY + enHeight + backSize, 1, Colors.BLACK);
        InventoryScreen.drawEntity(context, baseX, baseY, baseX + enWidth, baseY + enHeight, 55, 0.0625f, mouseX, mouseY, client.player);

        renderSuitSelection(context);

        int tBaseX = originX + halfWidth + 50;
        int tBaseY = originY + 4;

        context.drawText(this.textRenderer, Text.literal("Color Selection").formatted(Formatting.ITALIC), tBaseX, tBaseY, 0xffffff, true);

        float scale = 0.7f;
        float tY = originY + this.height - 4;

        MatrixStack matrices = context.getMatrices();
        matrices.push();

        matrices.translate(tBaseX + 15, tY, 0);
        matrices.scale(scale, scale, 1.0f);

        if(selectedPiece != null) {
            String selectedText = Text.translatable("text.astrocraft.armor_selection." + selectedPiece.getMessage()).getString();
            context.drawText(this.textRenderer, "Editing: " + selectedText, 0, 0, 0xffffff, true);
        }

        matrices.pop();
    }

    private void renderSuitSelection(DrawContext context) {
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        int scale = client.options.getGuiScale().getValue();

        int hWidth = client.getFramebuffer().viewportWidth / scale;
        int hHeight = client.getFramebuffer().viewportHeight / scale;

        int iWidth = 16;

        int bWidth = iWidth / 2;

        int plBaseX = (originX + (this.width / 2)) - bWidth;

        int armorSelectionSize = 2;

        int borderColor = Colors.BLACK;

        this.applyBlur(delta);
        this.renderDarkening(context, 0, 0, hWidth, hHeight);
        context.fill(originX + 140, originY - 7, originX + this.width, originY + this.height + 10, 0xc4030121);

        UIUtils.drawBorder(context, plBaseX - armorSelectionSize, originY - 7, originX + this.width, originY + this.height + 10, 2, borderColor);

        context.fill(plBaseX - armorSelectionSize, originY - 7, plBaseX + iWidth + armorSelectionSize, originY + this.height + 10, 0xff01000c);
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
