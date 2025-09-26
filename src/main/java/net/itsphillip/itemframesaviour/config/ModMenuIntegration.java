package net.itsphillip.itemframesaviour.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class ModMenuIntegration implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> new Screen(Text.literal("Item Frame Saviour")) {

            @Override
            protected void init() {
                // Toggle button
                this.addDrawableChild(ButtonWidget.builder(
                                Text.literal("Enabled: " + ModConfig.ENABLED),
                                button -> {
                                    ModConfig.ENABLED = !ModConfig.ENABLED;
                                    button.setMessage(Text.literal("Enabled: " + ModConfig.ENABLED));
                                    ModConfig.save();
                                })
                        .dimensions(this.width / 2 - 100, this.height / 2 - 10, 200, 20)
                        .build()
                );

                // Done button
                this.addDrawableChild(ButtonWidget.builder(
                                Text.translatable("gui.done"),
                                button -> {
                                    if (this.client != null) this.client.setScreen(parent);
                                })
                        .dimensions(this.width / 2 - 100, this.height / 2 + 20, 200, 20)
                        .build()
                );
            }
        };
    }
}