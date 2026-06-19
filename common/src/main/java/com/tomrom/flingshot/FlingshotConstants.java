package com.tomrom.flingshot;

import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FlingshotConstants {

	public static final String MOD_ID = "flingshot";
	public static final String MOD_NAME = "Flingshot";
	public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);

	public static ResourceLocation id(String path) {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
	}
}
