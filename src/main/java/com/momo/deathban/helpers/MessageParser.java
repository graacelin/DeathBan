package com.momo.deathban.helpers;

import com.momo.deathban.DeathBan;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;

public class MessageParser {

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

    public static Component firstTimeMessage(ServerPlayer joinedPlayer) {
        return new TranslatableComponent("[{0}] Welcome to the server {1}!", DeathBan.MOD_NAME, joinedPlayer);
    }

}
