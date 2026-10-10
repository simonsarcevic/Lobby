package de.lobby.main.util;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.UUID;

public class ItemBuilder {

    public static ItemStack createItem(ItemStack item, String displayName, int amount){
        return createItem(item, displayName, amount, new String[0]);
    }

    public static ItemStack createItem(ItemStack item, String displayName, int amount, String... lore){
        ItemStack itemStack = new ItemStack(item);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(displayName);
        if (lore.length > 0) {
            itemMeta.setLore(Arrays.asList(lore));
        }
        itemStack.setItemMeta(itemMeta);
        itemStack.setAmount(amount);

        return itemStack;
    }

    public static ItemStack createCustomSkull(String displayName, String texture) {
        return createCustomSkull(displayName, texture, new String[0]);
    }

    public static ItemStack createCustomSkull(String displayName, String texture, String... lore) {
        ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
        skullMeta.setDisplayName(displayName);
        if (lore.length > 0) {
            skullMeta.setLore(Arrays.asList(lore));
        }

        UUID profileId = UUID.nameUUIDFromBytes(texture.getBytes(StandardCharsets.UTF_8));
        GameProfile profile = new GameProfile(profileId, null);
        profile.getProperties().put("textures", new Property("textures", texture));

        try {
            Field profileField = skullMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);

            Object profileValue = profile;
            if (!profileField.getType().isAssignableFrom(GameProfile.class)) {
                Constructor<?> constructor = profileField.getType().getDeclaredConstructor(GameProfile.class);
                constructor.setAccessible(true);
                profileValue = constructor.newInstance(profile);
            }

            profileField.set(skullMeta, profileValue);
        } catch (ReflectiveOperationException exception) {
            throw new IllegalStateException("Die Custom-Skull-Textur konnte nicht gesetzt werden.", exception);
        }

        skull.setItemMeta(skullMeta);
        return skull;
    }
}

