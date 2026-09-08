package net.deechael.concentration.fabric.compat;

import net.caffeinemc.mods.sodium.api.config.ConfigEntryPoint;
import net.caffeinemc.mods.sodium.api.config.structure.ConfigBuilder;
import net.deechael.concentration.Concentration;
import net.deechael.concentration.FullscreenMode;
import net.deechael.concentration.fabric.config.ConcentrationConfigFabric;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

/**
 * Sodium 0.8+ config API entrypoint. Replaces SodiumVideoOptionsScreenMixin,
 * which mixed into Sodium's now-removed SodiumGameOptionPages internals.
 */
public class ConcentrationSodiumConfig implements ConfigEntryPoint {

    @Override
    public void registerConfigLate(ConfigBuilder builder) {
        builder.registerOwnModOptions()
                .addPage(builder.createOptionPage()
                        .setName(Component.literal("Concentration"))
                        .addOptionGroup(builder.createOptionGroup()
                                .addOption(builder.createEnumOption(Identifier.parse("concentration:fullscreen_mode"), FullscreenMode.class)
                                        .setName(Component.translatable("concentration.option.fullscreen_mode"))
                                        .setTooltip(Component.translatable("concentration.option.fullscreen_mode.tooltip"))
                                        .setElementNameProvider(mode -> Component.translatable(mode.getKey()))
                                        .setDefaultValue(FullscreenMode.BORDERLESS)
                                        .setBinding(
                                                ConcentrationSodiumConfig::onFullscreenModeChanged,
                                                () -> ConcentrationConfigFabric.getInstance().fullscreen
                                        )
                                )
                        )
                );
    }

    private static void onFullscreenModeChanged(FullscreenMode value) {
        ConcentrationConfigFabric config = ConcentrationConfigFabric.getInstance();
        config.fullscreen = value;
        config.save();

        Options options = Minecraft.getInstance().options;
        if (options.fullscreen().get()) {
            // Already fullscreen — re-apply now so the switch is visible immediately
            Concentration.toggleFullScreenMode(options, true);
        }
    }
}
