package com.grace.deathban;

import com.grace.deathban.config.Config;
import com.grace.deathban.core.BanList;
import com.grace.deathban.helpers.DateTimeCalculator;
import com.grace.deathban.helpers.MessageParser;
import com.mojang.logging.LogUtils;
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
import net.minecraftforge.fml.config.ModConfig;
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
    public static boolean deathBanOn;

    public DeathBan() {
        ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class,
                () -> new IExtensionPoint.DisplayTest(() -> NetworkConstants.IGNORESERVERONLY, (a, b) -> true));
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC, "deathban.toml");
    }

    private void setup(final FMLCommonSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        deathBanOn = Config.weekTime.get() != 0 || Config.dayTime.get() != 0 ||
                Config.hourTime.get() != 0 || Config.minuteTime.get() != 0;
    }

    @SubscribeEvent
    public void onStart(ServerStartedEvent serverStartedEvent) {
        server = serverStartedEvent.getServer();
        banList = new BanList(server.getPlayerList().getBans());
    }

    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        deathBanOn = Config.weekTime.get() != 0 || Config.dayTime.get() != 0 ||
                Config.hourTime.get() != 0 || Config.minuteTime.get() != 0;
        if(!event.getPlayer().getPersistentData().getBoolean(MOD_ID + "joinedBefore")
            && !server.isSingleplayer() && deathBanOn) {
            event.getPlayer().getPersistentData().putBoolean(MOD_ID + "joinedBefore", true);
            event.getPlayer().sendMessage(
                    MessageParser.firstTimeMessage((ServerPlayer) event.getPlayer()),
                    event.getPlayer().getUUID()
            );
            LOGGER.info("Sent welcome message to " + event.getPlayer().getName().getString());
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
        deathBanOn = Config.weekTime.get() != 0 || Config.dayTime.get() != 0 ||
                Config.hourTime.get() != 0 || Config.minuteTime.get() != 0;
        if (!event.getEntityLiving().getCommandSenderWorld().isClientSide() &&
                event.getEntityLiving() instanceof ServerPlayer deadPlayer &&
                !server.isSingleplayer() && deathBanOn) {
            String reason = MessageParser.deathReasonMessage(deadPlayer, event.getSource());
            Date expire = DateTimeCalculator.getExpiryDate(
                    Config.weekTime.get(),
                    Config.dayTime.get(),
                    Config.hourTime.get(),
                    Config.minuteTime.get()
            );
            banList.addToBanList(server, deadPlayer.getGameProfile(), expire, reason);
        }
    }
}