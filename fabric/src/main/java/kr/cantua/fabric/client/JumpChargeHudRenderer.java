package kr.cantua.fabric.client;

import kr.cantua.fabric.component.TraitComponents;
import kr.cantua.registries.Trait;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;

public class JumpChargeHudRenderer implements HudRenderCallback {
    @Override
    public void onHudRender(DrawContext drawContext, RenderTickCounter tickCounter) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;

        var trait = TraitComponents.get(client.player).get(Trait.loc.toString());

        double gauge = trait.getDouble(Trait.Double.JUMP_GAUGE.name());
        double gaugeMax = trait.getDouble(Trait.Double.JUMP_GAUGE_MAX.name());

        String text = String.format("Jump Gauge: %.2f / %.2f", gauge, gaugeMax);
        drawContext.drawText(client.textRenderer, text, 10, 10, 0xFFFFFF, true);
    }

    public static void register() {
        HudRenderCallback.EVENT.register(new JumpChargeHudRenderer());
    }
}
