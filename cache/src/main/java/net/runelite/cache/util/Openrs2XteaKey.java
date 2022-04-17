package net.runelite.cache.util;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

@Getter
public class Openrs2XteaKey {
    @SerializedName("mapsquare")
    private int region;
    @SerializedName("key")
    private int[] keys;
}
