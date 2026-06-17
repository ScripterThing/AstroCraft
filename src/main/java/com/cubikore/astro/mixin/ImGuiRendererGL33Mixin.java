package com.cubikore.astro.mixin;

import foundry.imgui.impl.renderer.v0.ImGuiRendererGL33;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ImGuiRendererGL33.class)
public class ImGuiRendererGL33Mixin {
    @Inject(at = @At("HEAD"), method = "vertexShaderGlsl130", cancellable = true)
    private void vertGL130(CallbackInfoReturnable<String> cir) {
        cir.setReturnValue("""
                #version 130
                uniform mat4 ProjMtx;
                in vec2 Position;
                in vec2 UV;
                in vec4 Color;
                out vec2 Frag_UV;
                out vec4 Frag_Color;
                void main()
                {
                    Frag_UV = UV;
                    Frag_Color = Color;
                    gl_Position = ProjMtx * vec4(Position.xy,0,1);
                }
                """);
    }

    @Inject(at = @At("HEAD"), method = "vertexShaderGlsl410Core", cancellable = true)
    private void vertGL410(CallbackInfoReturnable<String> cir) {
        cir.setReturnValue("""
                #version 410
                layout (location = 0) in vec2 Position;
                layout (location = 1) in vec2 UV;
                layout (location = 2) in vec4 Color;
                uniform mat4 ProjMtx;
                out vec2 Frag_UV;
                out vec4 Frag_Color;
                void main()
                {
                    Frag_UV = UV;
                    Frag_Color = Color;
                    gl_Position = ProjMtx * vec4(Position.xy,0,1);
                }
                """);
    }

    @Inject(at = @At("HEAD"), method = "fragmentShaderGlsl410Core", cancellable = true)
    private void fragGL410(CallbackInfoReturnable<String> cir) {
        cir.setReturnValue("""
                #version 410
                in vec2 Frag_UV;
                in vec4 Frag_Color;
                uniform sampler2D Texture;
                layout (location = 0) out vec4 Out_Color;
                void main()
                {
                    Out_Color = Frag_Color * texture(Texture, Frag_UV.st);
                }
                """);
    }

    @Inject(at = @At("HEAD"), method = "fragmentShaderGlsl130", cancellable = true)
    private void fragGL130(CallbackInfoReturnable<String> cir) {
        cir.setReturnValue("""
                #version 130
                uniform sampler2D Texture;
                in vec2 Frag_UV;
                in vec4 Frag_Color;
                out vec4 Out_Color;
                void main()
                {
                    Out_Color = Frag_Color * texture(Texture, Frag_UV.st);
                }
                """);
    }
}
