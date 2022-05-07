package com.momo.deathban.helpers;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;

public class BanMessageParser {


    public static String deathReasonMessage(ServerPlayer deadPlayer, DamageSource source) {
        String deathMessage = source.getLocalizedDeathMessage(deadPlayer).getString();
        return deathMessage
                .replaceFirst(deadPlayer.getName().getString(), "You")
                .replaceFirst("was", "were");
    }

    public static Component banMessage(String reason, String expire) {

        return new TranslatableComponent(
                """
                §4§lYou died!§r
                Cause of death: §e{0}§r
                Ban expires in: §e{1}§r
                """,
                reason, String.valueOf(expire)
        );
    }

}
