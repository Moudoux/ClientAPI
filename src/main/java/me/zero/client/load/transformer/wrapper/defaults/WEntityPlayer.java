package me.zero.client.load.transformer.wrapper.defaults;

import javassist.CtPrimitiveType;
import me.zero.client.api.wrapper.IEntityPlayer;
import me.zero.client.load.transformer.wrapper.ClassWrapper;

import static me.zero.client.load.transformer.reference.obfuscation.MCMappings.*;

/**
 * Wraps IEntityPlayer to EntityPlayer
 *
 * @since 1.0
 *
 * Created by Brady on 2/25/2017.
 */
public class WEntityPlayer extends ClassWrapper {

    public WEntityPlayer() {
        super(EntityPlayer, IEntityPlayer.class);
    }

    @Override
    protected void loadImplementations() {
        this.implementS("setSleeping", CtPrimitiveType.booleanType, sleeping);
        this.implementS("setSleepTimer", CtPrimitiveType.intType, sleepTimer);
    }
}