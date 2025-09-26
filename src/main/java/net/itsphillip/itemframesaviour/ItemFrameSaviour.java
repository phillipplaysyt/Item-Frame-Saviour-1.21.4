package net.itsphillip.itemframesaviour;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.itsphillip.itemframesaviour.config.ModConfig;

public class ItemFrameSaviour implements ModInitializer {
    public static final String MOD_ID = "itemframesaviour";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {

        ModConfig.load();

        LOGGER.info("Item Frame Saviour initialized! Enabled: " + ModConfig.ENABLED);

        // Listen for item frame breaks
        AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if (!world.isClient && ModConfig.ENABLED && player.isCreative() && entity instanceof ItemFrameEntity frame) {
                ItemStack stack = frame.getHeldItemStack();
                if (!stack.isEmpty()) { // ✅ any item now
                    ItemStack copy = stack.copy(); // keeps all NBT, damage, lore, etc.

                    // Try to insert into player's inventory
                    if (!player.getInventory().insertStack(copy)) {
                        // Inventory full → force-drop in the world
                        ItemEntity drop = new ItemEntity(
                                world,
                                frame.getX(), frame.getY(), frame.getZ(),
                                copy
                        );
                        drop.setToDefaultPickupDelay(); // prevents instant re-pickup glitches
                        world.spawnEntity(drop);
                    }
                }
            }
            return ActionResult.PASS; // allow vanilla to break the frame
        });

    }
}
