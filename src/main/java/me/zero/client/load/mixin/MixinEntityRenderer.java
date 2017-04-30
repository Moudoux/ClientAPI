package me.zero.client.load.mixin;

import me.zero.client.api.util.render.camera.Camera;
import net.minecraft.client.renderer.EntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Created by Brady on 4/27/2017.
 */
@Mixin(EntityRenderer.class)
public class MixinEntityRenderer {

    @Inject(method = "getFOVModifier", at = @At("HEAD"), cancellable = true)
    public void getFOVModifier(float partialTicks, boolean useFOVSetting, CallbackInfoReturnable<Float> ci) {
        if (Camera.isCapturing()) {
            ci.setReturnValue(90.0F);
        }
    }
}