package in.gracel.deathban.core;

import com.mojang.authlib.GameProfile;
import in.gracel.deathban.DeathBan;
import in.gracel.deathban.helpers.MessageParser;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.UserBanList;
import net.minecraft.server.players.UserBanListEntry;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class BanList {
    private UserBanList userBanList;

    public BanList(UserBanList banList) {
        this.userBanList = banList;
    }

    public void addToBanList(MinecraftServer server, GameProfile deadPlayer, Date expire, String reason) {
        if (!userBanList.isBanned(deadPlayer)) {
            ServerPlayer serverplayer = server.getPlayerList().getPlayer(deadPlayer.getId());
            UserBanListEntry entry = new UserBanListEntry(deadPlayer, new Date(), DeathBan.MOD_NAME, expire, reason);
            userBanList.add(entry);
            LocalDateTime ldtCurr = LocalDateTime.ofInstant(new Date().toInstant(), ZoneId.systemDefault());
            LocalDateTime ldtExpire = LocalDateTime.ofInstant(expire.toInstant(), ZoneId.systemDefault());

            Component component = MessageParser.banMessage(reason,
                    MessageParser.getTimeRemaining(ldtCurr, ldtExpire));
            assert serverplayer != null;
            serverplayer.connection.disconnect(component);
        }
    }

    public void removeBanIfTimeExpire() {
        Date currDate = new Date();
        userBanList.getEntries().removeIf(entry -> (entry.getExpires() != null && currDate.after((entry.getExpires()))));
    }

    public void removeAll() {
        userBanList.getEntries().clear();
    }
}
