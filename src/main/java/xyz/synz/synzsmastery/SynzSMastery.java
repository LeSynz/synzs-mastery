package xyz.synz.synzsmastery;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;

import net.minecraft.resources.Identifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xyz.synz.synzsmastery.util.Notify;

public class SynzSMastery implements ModInitializer {
	public static final String MOD_ID = "synzsmastery";
	public static final String MOD_NAME = "Synz's Mastery";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ServerLifecycleEvents.SERVER_STARTED.register(server ->
				Notify.success(server, MOD_NAME + " is active!"));
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}
