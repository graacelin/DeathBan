package com.momo.deathban;

import com.mojang.logging.LogUtils;
import com.momo.deathban.core.BanList;
import com.momo.deathban.helpers.DateTimeCalculator;
import com.momo.deathban.helpers.MessageParser;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkConstants;
import org.slf4j.Logger;

import java.util.Date;
// The value here should match an entry in the META-INF/mods.toml file
@Mod(DeathBan.MOD_ID)
public class DeathBan {
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();
    MinecraftServer server;
    BanList banList;
    public static final String MOD_ID = "deathban";
    public static final String MOD_NAME = "DeathBan";

    public DeathBan() {
        ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class,
                () -> new IExtensionPoint.DisplayTest(() -> NetworkConstants.IGNORESERVERONLY, (a, b) -> true));
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
    }

    private void setup(final FMLCommonSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onStart(ServerStartedEvent serverStartedEvent) {
        server = serverStartedEvent.getServer();
        banList = new BanList(server.getPlayerList().getBans());
    }

    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if(!event.getPlayer().getPersistentData().getBoolean("joinedBefore")) {
            event.getPlayer().getPersistentData().putBoolean("joinedBefore", true);
            event.getPlayer().sendMessage(new TranslatableComponent("Welcome!"), event.getPlayer().getUUID());
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.ServerTickEvent event) {
        if (this.server != null) {
           banList.removeBanIfTimeExpire();
        }
    }

    @SubscribeEvent(priority=EventPriority.LOWEST)
    public void onDeath(LivingDeathEvent event) {
        if (!event.getEntityLiving().getCommandSenderWorld().isClientSide() &&
                event.getEntityLiving() instanceof ServerPlayer deadPlayer) {
            String reason = MessageParser.deathReasonMessage(deadPlayer, event.getSource());
            Date expire = DateTimeCalculator.getExpiryDate(0, 0, 6, 0);
            banList.addToBanList(server, deadPlayer.getGameProfile(), expire, reason);
        }
    }
}