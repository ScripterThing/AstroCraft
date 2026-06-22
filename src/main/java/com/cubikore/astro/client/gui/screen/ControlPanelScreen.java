package com.cubikore.astro.client.gui.screen;

import com.cubikore.astro.client.ClientStorage;
import com.cubikore.astro.gui.AstroCraftSprites;
import com.cubikore.astro.networking.payload.ShipCommandPayload;
import com.cubikore.astro.universe.Planet;
import com.cubikore.astro.universe.Universe;
import com.cubikore.astro.util.VectorMath;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

public class ControlPanelScreen extends Screen {
    public static Text TITLE = Text.literal("Control Panel");

    private List<Identifier> planetList = new ArrayList<>();

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

        this.addDrawableChild(
                ButtonWidget.builder(Text.literal("Auto Thrust"), this::autoThrustPressed)
                        .dimensions(this.width - 128, 22 + this.textRenderer.fontHeight, 98, 20)
                        .build()
        );
    }

    private Vector2f getRadiusAndAngle(String planetId) {
        return switch (planetId) {
            case "astrocraft:mercury" -> new Vector2f(60f, -30f);
            case "astrocraft:venus" -> new Vector2f(80f, 30f);
            case "astrocraft:earth" -> new Vector2f(120f, 0f);
            case "astrocraft:mars" -> new Vector2f(150f, 45f);
            case "astrocraft:jupiter" -> new Vector2f(170f, 120f);
            case "astrocraft:saturn" -> new Vector2f(150f, -90f);
            case "astrocraft:uranus" -> new Vector2f(170f, -140f);
            case "astrocraft:neptune" -> new Vector2f(200f, -180);
            default -> new Vector2f(0, 0);
        };
    }

    private void autoThrustPressed(ButtonWidget button) {
        ClientPlayNetworking.send(new ShipCommandPayload("auto_thrust", "Auto Thrust"));
        this.close();
    }

    private void ftlJumpPressed(String planetName) {
        ClientPlayNetworking.send(new ShipCommandPayload("ftl_jump", planetName));
        this.close();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        context.drawGuiTexture(AstroCraftSprites.BLACK_SPRITE, (this.width / 2) - 150, (this.height / 2) - 150, 300, 300);

        Text aupt = Text.literal("Auto Pilot Options");
        int aw = this.textRenderer.getWidth(aupt);

        context.drawText(this.textRenderer, aupt, this.width - aw - 40, 20, 0xffffff, true);

        for(Identifier planetId : planetList) {
            Vector2f p = getRadiusAndAngle(planetId.toString());
            float radius = p.x / 1.5f;
            float angle = p.y + 90 + ClientStorage.renderedWorldOffset.w;

            Vector2f orbitPos = VectorMath.orbit(angle, radius);
            orbitPos.add((float) this.width / 2, (float) this.height / 2);

            context.drawGuiTexture(planetId, (int) (orbitPos.x - 8), (int) (orbitPos.y - 8), 16, 16);

            if(mouseX >= orbitPos.x - 10 && mouseY >= orbitPos.y - 10 && mouseX <= orbitPos.x + 10 && mouseY <= orbitPos.y + 10) {
                context.drawGuiTexture(AstroCraftSprites.PLANET_SELECT_SPRITE, (int) (orbitPos.x - 10), (int) (orbitPos.y - 10), 20, 20);

                context.drawTooltip(this.textRenderer, Text.translatable(planetId.toTranslationKey()), mouseX, mouseY);
            }
        }

        context.drawGuiTexture(AstroCraftSprites.SUN_SPRITE, (this.width / 2) - 16, (this.height / 2) - 16, 32, 32);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for(Identifier planetId : planetList) {
            Vector2f p = getRadiusAndAngle(planetId.toString());
            float radius = p.x / 1.5f;
            float angle = p.y + 90 + ClientStorage.renderedWorldOffset.w;

            Vector2f orbitPos = VectorMath.orbit(angle, radius);
            orbitPos.add((float) this.width / 2, (float) this.height / 2);

            if(mouseX >= orbitPos.x - 10 && mouseY >= orbitPos.y - 10 && mouseX <= orbitPos.x + 10 && mouseY <= orbitPos.y + 10) {
                this.ftlJumpPressed(planetId.toString());
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        super.renderBackground(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
