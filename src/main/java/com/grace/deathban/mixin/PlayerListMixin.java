package com.grace.deathban.mixin;

import com.mojang.authlib.GameProfile;
import com.grace.deathban.helpers.MessageParser;
import net.minecraft.network.chat.Component;
import net.minecraft.server.players.PlayerList;
import net.minecraft.server.players.UserBanList;
import net.minecraft.server.players.UserBanListEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.File;
import java.net.SocketAddress;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Mixin(PlayerList.class)
public class PlayerListMixin {

    @Shadow
    public static File USERBANLIST_FILE = new File("banned-players.json");

    @Shadow
    private UserBanList bans = new UserBanList(USERBANLIST_FILE);

    @Inject(at = @At("HEAD"), method = "Lnet/minecraft/server/players/PlayerList;canPlayerLogin(Ljava/net/SocketAddress;Lcom/mojang/authlib/GameProfile;)Lnet/minecraft/network/chat/Component;", cancellable = true)
    private void canPlayerLogin(SocketAddress address, GameProfile profile, CallbackInfoReturnable<Component> callback) {
        if (this.bans.isBanned(profile)) {
            UserBanListEntry userbanlistentry = this.bans.get(profile);
            if (userbanlistentry.getExpires() != null) {
                LocalDateTime ldtCurr = LocalDateTime.ofInstant(new Date().toInstant(), ZoneId.systemDefault());
                LocalDateTime ldtExpire = LocalDateTime.ofInstant(bans.get(profile).getExpires().toInstant(), ZoneId.systemDefault());
                Component component = MessageParser.banMessage(userbanlistentry.getReason(),
                        MessageParser.getTimeRemaining(ldtCurr, ldtExpire));
                callback.setReturnValue(component);
            }
        }
    }


}
