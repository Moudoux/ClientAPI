package me.zero.client.load.mixin;

import me.zero.client.api.ClientAPI;
import me.zero.client.api.event.defaults.LayerRenderEvent;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * @author Brady
 * @since 4/27/2017 12:00 PM
 */
@Mixin(RenderLivingBase.class)
public class MixinRenderLivingBase {

    @Redirect(method = "renderLayers", at = @At(value = "INVOKE", target = "net/minecraft/client/renderer/entity/layers/LayerRenderer.doRenderLayer(Lnet/minecraft/entity/EntityLivingBase;FFFFFFF)V"))
    @SuppressWarnings("unchecked")
    public void doRenderLayer(LayerRenderer renderer, EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scaleIn) {
        LayerRenderEvent event = new LayerRenderEvent(entitylivingbaseIn, renderer);
        ClientAPI.EVENT_BUS.post(event);
        if (!event.isCancelled())
            renderer.doRenderLayer(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scaleIn);
    }
}
