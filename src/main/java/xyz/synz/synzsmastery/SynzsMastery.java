package xyz.synz.synzsmastery;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;

import net.minecraft.resources.Identifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xyz.synz.synzsmastery.command.MasteryCommand;
import xyz.synz.synzsmastery.event.MasterySyncEvents;
import xyz.synz.synzsmastery.event.WelcomeEvents;
import xyz.synz.synzsmastery.util.Notify;
import xyz.synz.synzsmastery.event.MiningEvents;

public class SynzsMastery implements ModInitializer {
	public static final String MOD_ID = "synzsmastery";
	public static final String MOD_NAME = "Synz's Mastery";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ServerLifecycleEvents.SERVER_STARTED.register(server ->
				Notify.success(server, MOD_NAME + " is active!"));

		MiningEvents.register();
		WelcomeEvents.register();
		MasterySyncEvents.register();
		MasteryCommand.register();
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}
