package com.cubikore.astro.client.gui.screen;

import com.cubikore.astro.AstroCraftClient;
import com.cubikore.astro.client.gui.AstroCraftGuiTextures;
import com.cubikore.astro.client.gui.action.ButtonAction;
import com.cubikore.astro.client.gui.text.FormattedText;
import com.cubikore.astro.client.gui.widget.TextButton;
import com.cubikore.astro.client.gui.widget.TextStack;
import com.cubikore.astro.networking.payload.ShipCommandPayload;
import com.cubikore.astro.universe.Planet;
import com.cubikore.astro.universe.Universe;
import com.cubikore.astro.math.VectorMath;
import com.cubikore.astro.util.UIUtils;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.joml.Vector2f;
import org.joml.Vector2i;

import java.util.ArrayList;
import java.util.List;

public class ControlPanelScreen extends AstManagedScreen {
    public static Text TITLE = Text.literal("Control Panel");

    private List<Identifier> planetList = new ArrayList<>();

    private TextStack planetAtrribsStack;
    private TextButton ftlButton;

    private String selectedPlanet = "";
    private String hoveredPlanet = "";

    public ControlPanelScreen() {
        super(TITLE);

        for(Planet planet : Universe.planets) {
            if(!planet.planetId.equals(Universe.SUN_ID))
                planetList.add(planet.planetId);
        }
    }

    @Override
    protected void init() {
        super.init();

        this.planetAtrribsStack = new TextStack(22, 22, 1, 1, 6);

        planetAtrribsStack.setBackgroundColor(0x93000000);

        this.addWidget(planetAtrribsStack);

        this.ftlButton = new TextButton(22, 22, 1, 1, Text.empty(), this::ftlJumpPressed);
        ftlButton.setBackColor(0xff0a0a0a);
        ftlButton.setDeselectedBorderColor(0xff000000);

        this.addButton(ftlButton);

//        this.addDrawableChild(
//                ButtonWidget.builder(Text.literal("Auto Thrust"), this::autoThrustPressed)
//                        .dimensions(this.width - 128, 22 + this.textRenderer.fontHeight, 98, 20)
//                        .build()
//        );

        Text autoThrustText = Text.translatable("text.astrocraft.auto_thrust_action");

        int endY = 22 + 60;

        TextButton autoThrustButton = new TextButton(this.width - 100, endY - 24, -1, 20, autoThrustText, this::autoThrustPressed);

        autoThrustButton.setBackColor(0xff0a0a0a);
        autoThrustButton.setDeselectedBorderColor(0xff000000);

        this.addButton(autoThrustButton);
    }

    private Vector2f getRadiusAndAngle(String planetId) {
        return switch (planetId) {
            case "astrocraft:mercury" -> new Vector2f(80f, -60f);
            case "astrocraft:venus" -> new Vector2f(120f, 60f);
            case "astrocraft:earth" -> new Vector2f(160f, 0f);
            case "astrocraft:mars" -> new Vector2f(200f, 100f);
            case "astrocraft:jupiter" -> new Vector2f(240f, 40f);
            case "astrocraft:saturn" -> new Vector2f(280f, -70f);
            case "astrocraft:uranus" -> new Vector2f(320f, -170f);
            case "astrocraft:neptune" -> new Vector2f(360f, -180);
            default -> new Vector2f(0, 0);
        };
    }

    private boolean isRocky(String planetId) {
        return switch (planetId) {
            case "astrocraft:mercury" -> true;
            case "astrocraft:venus" -> true;
            case "astrocraft:earth" -> true;
            case "astrocraft:mars" -> true;
            default -> false;
        };
    }

    private void autoThrustPressed(ButtonAction action) {
        ClientPlayNetworking.send(new ShipCommandPayload("auto_thrust", "Auto Thrust"));
        this.close();
    }

    private void ftlJumpPressed(ButtonAction action) {
        String planetName = action.getMessage();
        ClientPlayNetworking.send(new ShipCommandPayload("ftl_jump", planetName));
        this.close();
    }

    private void planetPressed(String planetName) {
        selectedPlanet = planetName;

        Identifier planetId = Identifier.tryParse(planetName);

        Planet planet = Universe.getPlanet(planetId);

        if(planet != null) {
            Formatting nameFormatting = Formatting.BOLD;
            Formatting attributeFormatting = Formatting.ITALIC;

            FormattedText nameText = new FormattedText(1, 1, Text.translatable(planetId.toTranslationKey()).formatted(nameFormatting));

            planetAtrribsStack.addLine(nameText);
            planetAtrribsStack.addLine(FormattedText.empty());

            for(Text attribute : planet.attributes) {
                FormattedText attribText = new FormattedText(1, 1, ((MutableText) attribute).formatted(attributeFormatting));
                attribText.setDotted(true);
                planetAtrribsStack.addLine(attribText);
            }
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        renderSelectionPanel(context, mouseX, mouseY);
        renderPlanetMap(context, mouseX, mouseY);
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        super.renderBackground(context, mouseX, mouseY, delta);

        int startX = this.width - 104;
        int startY = 22;

        int endX = startX + 72;
        int endY = startY + 60;

        context.fill(startX, startY, endX, endY, 0x93000000);
        UIUtils.drawBorder(context, startX, startY, endX, endY, 1, 0xff000000);

        context.drawText(this.textRenderer, Text.translatable("text.astrocraft.auto_pilot"), startX + 12, startY + 4, 0xffffffff, false);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        super.mouseClicked(mouseX, mouseY, button);

        if(!hoveredPlanet.isEmpty() && button == 0) {
//            ClientPlayNetworking.send(new ShipCommandPayload("ftl_jump", hoveredPlanet));
//            this.close();
            planetAtrribsStack.clearLines();
            planetPressed(hoveredPlanet);
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    private void renderSelectionPanel(DrawContext context, int mouseX, int mouseY) {
        if(!selectedPlanet.isEmpty()) {
            ftlButton.allowRendering = true;

            Identifier planetId = Identifier.tryParse(selectedPlanet);

            int stackWidth = planetAtrribsStack.getTextWidth() + 40;
            planetAtrribsStack.setDimensions(stackWidth, 150);

            int startX = planetAtrribsStack.getX();
            int startY = planetAtrribsStack.getY();

            int endX = startX + planetAtrribsStack.getWidth();
            int endY = startY + planetAtrribsStack.getHeight();

            UIUtils.drawBorder(context, startX, startY, endX, endY, 1, 0xff000000);

            ftlButton.setMessage(planetId.toString());
            ftlButton.setText(Text.translatable("text.astrocraft.ftl_action").formatted(Formatting.BOLD));
            ftlButton.setDimensions(planetAtrribsStack.getWidth() - 8, 20);
            ftlButton.setPosition(startX + 4, (endY - ftlButton.getHeight()) - 4);
        }
        else {
            ftlButton.allowRendering = false;
        }
    }

    private void renderPlanetMap(DrawContext context, int mouseX, int mouseY) {
        int guiScale = client.options.getGuiScale().getValue();

        Vector2i mousePos = new Vector2i(mouseX * guiScale, mouseY * guiScale);

        float rat = 1f / (float) guiScale;

        int screenWidth = client.getFramebuffer().textureWidth;
        int screenHeight = client.getFramebuffer().textureHeight;

        if(!hoveredPlanet.isEmpty()) {
            Identifier planetId = Identifier.tryParse(hoveredPlanet);
            Text planetText = Text.translatable(planetId.toTranslationKey()).formatted(Formatting.BOLD);
            int textWidth = this.textRenderer.getWidth(planetText);
            context.drawText(this.textRenderer, planetText, ((this.width / 2) + 100) - textWidth, 65, 0xffffffff, true);
        }

        MatrixStack matrices = context.getMatrices();
        matrices.push();

        matrices.scale(rat, rat, 1);

        int baseX = (screenWidth / 2) - 300;
        int baseY = (screenHeight / 2) - 300;

        int backWidth = 600;
        int backHeight = 600;

        context.fill(baseX, baseY, baseX + backWidth, baseY + backHeight, 0x93000000);
        UIUtils.drawBorder(context, baseX, baseY, baseX + backWidth, baseY + backHeight, 2, 0xff000000);

        String hoveredPlanet = "";

        for(Identifier planetId : planetList) {
            float gameTime = AstroCraftClient.clientGameManager.getGameTime() * 2;

            Vector2f p = getRadiusAndAngle(planetId.toString());
            float radius = p.x / 1.5f;
            float angle = p.y + 90 + (gameTime * (1.0f - (radius / 380f)));

            Vector2f orbitPos = VectorMath.orbit(angle, radius);
            orbitPos.add((float) screenWidth / 2, (float) screenHeight / 2);

            String idString = planetId.toString();

            boolean rocky = isRocky(idString);
            int planetSize = rocky ? 20 : 32;
            int plC = planetSize / 2;

            int centerX = (int) (orbitPos.x - plC);
            int centerY = (int) (orbitPos.y - plC);

            UIUtils.drawCircle(context, (screenWidth / 2), (screenHeight / 2), (int)radius, 0xff939393);

            context.drawGuiTexture(planetId, centerX, centerY, planetSize, planetSize);

            int selectionP = rocky ? 11 : 20;

            if(mousePos.x >= orbitPos.x - selectionP && mousePos.y >= orbitPos.y - selectionP && mousePos.x <= orbitPos.x + selectionP && mousePos.y <= orbitPos.y + selectionP) {
                hoveredPlanet = idString;

                int selectionS = selectionP * 2;
                context.drawGuiTexture(AstroCraftGuiTextures.PLANET_SELECT_SPRITE, (int) (orbitPos.x - selectionP), (int) (orbitPos.y - selectionP), selectionS, selectionS);
            }

            if(planetId.toString().equals(selectedPlanet)) {
                int selectionS = selectionP * 2;
                context.drawGuiTexture(AstroCraftGuiTextures.PLANET_SELECT_SPRITE_Y, (int) (orbitPos.x - selectionP), (int) (orbitPos.y - selectionP), selectionS, selectionS);
            }
        }

        this.hoveredPlanet = hoveredPlanet;

        context.drawGuiTexture(AstroCraftGuiTextures.SUN_SPRITE, (screenWidth / 2) - 16, (screenHeight / 2) - 16, 32, 32);

        matrices.pop();
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
