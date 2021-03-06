package me.zero.client.load.mixin;

import me.zero.client.api.ClientAPI;
import me.zero.client.api.event.defaults.EntityRenderEvent;
import me.zero.alpine.type.EventState;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * @author Brady
 * @since 4/27/2017 12:00 PM
 */
@Mixin(RenderManager.class)
public class MixinRenderManager {

    @Redirect(method = "doRenderEntity(Lnet/minecraft/entity/Entity;DDDFFZ)V", at = @At(value = "INVOKE", target = "net/minecraft/client/renderer/entity/Render.doRender(Lnet/minecraft/entity/Entity;DDDFF)V"))
    @SuppressWarnings("unchecked")
    public void doRender(Render render, Entity entity, double x, double y, double z, float entityYaw, float partialTicks) {
        EntityRenderEvent event = new EntityRenderEvent(EventState.PRE, render, entity, x, y, z, entityYaw, partialTicks);
        ClientAPI.EVENT_BUS.post(event);
        if (!event.isCancelled())
            render.doRender(entity, x, y, z, entityYaw, partialTicks);

        ClientAPI.EVENT_BUS.post(new EntityRenderEvent(EventState.POST, render, entity, x, y, x, entityYaw, partialTicks));
    }
}
