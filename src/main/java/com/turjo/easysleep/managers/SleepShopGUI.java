package com.turjo.easysleep.managers;

import com.turjo.easysleep.EasySleep;
import com.turjo.easysleep.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

/**
 * 🛒 SLEEP SHOP GUI MANAGER - BEAUTIFUL INTERACTIVE SHOP 🛒
 * Complete GUI overhaul for Dream Coin shopping experience
 * 
 * @author Turjo
 * @version 1.5.4
 */
public class SleepShopGUI implements Listener {
    
    private final EasySleep plugin;
    private final Map<UUID, Inventory> openShops;
    
    // Shop items with enhanced details
    private final Map<String, ShopItem> shopItems = new HashMap<String, ShopItem>() {{
        put("dream_boost", new ShopItem("Dream Boost", Material.POTION, 50, 
            Arrays.asList("§7Double your sleep rewards", "§7for the next hour!", "", "§e§lPrice: 50 Dream Coins")));
        put("lunar_blessing", new ShopItem("Lunar Blessing", Material.NETHER_STAR, 200,
            Arrays.asList("§7Permanent moon phase bonus", "§7for all future sleeps!", "", "§e§lPrice: 200 Dream Coins")));
        put("quantum_stabilizer", new ShopItem("Quantum Stabilizer", Material.END_CRYSTAL, 150,
            Arrays.asList("§7Guaranteed quantum entanglement", "§7in your next sleep!", "", "§e§lPrice: 150 Dream Coins")));
        put("dream_catcher", new ShopItem("Dream Catcher", Material.COBWEB, 100,
            Arrays.asList("§7Collect rare dream fragments", "§7while you sleep!", "", "§e§lPrice: 100 Dream Coins")));
        put("sleep_multiplier", new ShopItem("Sleep Multiplier", Material.BEACON, 300,
            Arrays.asList("§7Triple your sleep effectiveness", "§7for maximum benefits!", "", "§e§lPrice: 300 Dream Coins")));
        put("dimensional_key", new ShopItem("Dimensional Key", Material.ENDER_EYE, 500,
            Arrays.asList("§7Access exclusive dream realms", "§7and hidden dimensions!", "", "§e§lPrice: 500 Dream Coins")));
        put("time_crystal", new ShopItem("Time Crystal", Material.DIAMOND_BLOCK, 750,
            Arrays.asList("§7Control time acceleration", "§7during sleep events!", "", "§e§lPrice: 750 Dream Coins")));
        put("dream_crown", new ShopItem("Dream Crown", Material.NETHERITE_HELMET, 1000,
            Arrays.asList("§7Ultimate sleep mastery item", "§7for true dream masters!", "", "§e§lPrice: 1000 Dream Coins")));
    }};
    
    public SleepShopGUI(EasySleep plugin) {
        this.plugin = plugin;
        this.openShops = new HashMap<>();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    /**
     * 🌟 Open the Dream Shop GUI for player
     */
    public void openShop(Player player) {
        // Check permission
        if (!player.hasPermission("easysleep.shop")) {
            MessageUtils.sendMessage(player, "&cYou don't have permission to access the shop!");
            return;
        }
        
        long playerCoins = plugin.getSleepEconomyManager().getDreamCoins(player);
        
        // Create beautiful shop inventory
        Inventory shop = Bukkit.createInventory(null, 54, "§d§l💎 Dream Coin Shop 💎");
        
        // Add balance display item
        ItemStack balanceItem = createBalanceItem(playerCoins);
        shop.setItem(4, balanceItem);
        
        // Add shop items
        int[] shopSlots = {10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25};
        int slotIndex = 0;
        
        for (Map.Entry<String, ShopItem> entry : shopItems.entrySet()) {
            if (slotIndex >= shopSlots.length) break;
            
            ShopItem item = entry.getValue();
            ItemStack shopItemStack = createShopItem(item, playerCoins >= item.price, entry.getKey());
            shop.setItem(shopSlots[slotIndex], shopItemStack);
            slotIndex++;
        }
        
        // Add decorative items
        addDecorativeItems(shop);
        
        // Add close button
        ItemStack closeItem = new ItemStack(Material.BARRIER);
        ItemMeta closeMeta = closeItem.getItemMeta();
        closeMeta.setDisplayName("§c§lClose Shop");
        closeMeta.setLore(Arrays.asList("§7Click to close the shop"));
        closeItem.setItemMeta(closeMeta);
        shop.setItem(49, closeItem);
        
        // Open shop and track it
        player.openInventory(shop);
        openShops.put(player.getUniqueId(), shop);
        
        // Play shop open sound
        player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 0.5f, 1.2f);
    }
    
    /**
     * Create balance display item
     */
    private ItemStack createBalanceItem(long balance) {
        ItemStack balanceItem = new ItemStack(Material.EMERALD);
        ItemMeta balanceMeta = balanceItem.getItemMeta();
        balanceMeta.setDisplayName("§a§lYour Balance");
        balanceMeta.setLore(Arrays.asList(
            "§7Current Dream Coins:",
            "§e§l" + balance + " §d§lDream Coins",
            "",
            "§7Earn more by sleeping!"
        ));
        balanceItem.setItemMeta(balanceMeta);
        return balanceItem;
    }
    
    /**
     * Create shop item with proper formatting
     */
    private ItemStack createShopItem(ShopItem item, boolean canAfford, String itemId) {
        ItemStack shopItem = new ItemStack(item.material);
        ItemMeta meta = shopItem.getItemMeta();
        
        // Set name with affordability color
        String nameColor = canAfford ? "§a§l" : "§c§l";
        meta.setDisplayName(nameColor + item.name);
        
        // Create lore with affordability status
        List<String> lore = new ArrayList<>(item.description);
        lore.add("");
        if (canAfford) {
            lore.add("§a§l✓ You can afford this!");
            lore.add("§7Click to purchase");
        } else {
            lore.add("§c§l✗ Not enough Dream Coins");
            lore.add("§7Sleep more to earn coins!");
        }
        
        meta.setLore(lore);
        shopItem.setItemMeta(meta);
        
        return shopItem;
    }
    
    /**
     * Add decorative items to shop
     */
    private void addDecorativeItems(Inventory shop) {
        ItemStack glass = new ItemStack(Material.PURPLE_STAINED_GLASS_PANE);
        ItemMeta glassMeta = glass.getItemMeta();
        glassMeta.setDisplayName("§d§l✦");
        glass.setItemMeta(glassMeta);
        
        // Add glass panes for decoration
        int[] glassSlots = {0, 1, 2, 3, 5, 6, 7, 8, 9, 17, 18, 26, 27, 35, 36, 44, 45, 46, 47, 48, 50, 51, 52, 53};
        for (int slot : glassSlots) {
            shop.setItem(slot, glass);
        }
        
        // Add special decorative items
        ItemStack star = new ItemStack(Material.NETHER_STAR);
        ItemMeta starMeta = star.getItemMeta();
        starMeta.setDisplayName("§e§l✦ Dream Shop ✦");
        starMeta.setLore(Arrays.asList("§7Welcome to the mystical", "§7Dream Coin marketplace!"));
        star.setItemMeta(starMeta);
        shop.setItem(13, star);
    }
    
    /**
     * Handle shop click events
     */
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();
        
        if (!openShops.containsKey(player.getUniqueId())) return;
        
        event.setCancelled(true); // Prevent item taking
        
        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null || clickedItem.getType() == Material.AIR) return;
        
        // Handle close button
        if (clickedItem.getType() == Material.BARRIER) {
            player.closeInventory();
            return;
        }
        
        // Handle shop item purchases
        String itemId = getItemIdFromSlot(event.getSlot());
        if (itemId != null) {
            handlePurchase(player, itemId);
        }
    }
    
    /**
     * Get item ID from clicked slot
     */
    private String getItemIdFromSlot(int slot) {
        int[] shopSlots = {10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25};
        String[] itemIds = {"food_bundle", "tool_kit", "armor_set", "building_blocks", "potion_bundle",
                           "dream_boost", "lunar_blessing", "quantum_stabilizer", "time_crystal"};
        
        for (int i = 0; i < shopSlots.length && i < itemIds.length; i++) {
            if (shopSlots[i] == slot) {
                return itemIds[i];
            }
        }
        return null;
    }
    
    /**
     * Handle item purchase
     */
    private void handlePurchase(Player player, String itemId) {
        ShopItem item = shopItems.get(itemId);
        if (item == null) return;
        
        long playerCoins = plugin.getSleepEconomyManager().getDreamCoins(player);
        
        if (playerCoins < item.price) {
            // Not enough coins
            MessageUtils.sendMessage(player, "§c§l✗ Insufficient Dream Coins!");
            MessageUtils.sendMessage(player, "§cNeed: §e" + item.price + " §cHave: §e" + playerCoins);
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 0.5f, 1.0f);
            return;
        }
        
        // Process purchase
        boolean success = plugin.getSleepEconomyManager().purchaseShopItem(player, itemId);
        
        if (success) {
            // Purchase successful
            MessageUtils.sendMessage(player, "§a§l✓ Purchase Successful!");
            MessageUtils.sendMessage(player, "§aYou bought: §e" + item.name);
            MessageUtils.sendMessage(player, "§aRemaining balance: §e" + (playerCoins - item.price) + " §dDream Coins");
            
            // Success effects
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 0.8f, 1.5f);
            player.getWorld().spawnParticle(org.bukkit.Particle.TOTEM, 
                player.getLocation().add(0, 1, 0), 10, 0.5, 0.5, 0.5, 0.1);
            
            // Refresh shop
            player.closeInventory();
            Bukkit.getScheduler().runTaskLater(plugin, () -> openShop(player), 5L);
        } else {
            MessageUtils.sendMessage(player, "§c§l✗ Purchase failed! Please try again.");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 0.5f, 1.0f);
        }
    }
    
    /**
     * Handle shop close
     */
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getPlayer() instanceof Player) {
            Player player = (Player) event.getPlayer();
            openShops.remove(player.getUniqueId());
            
            // Play close sound
            player.playSound(player.getLocation(), Sound.BLOCK_CHEST_CLOSE, 0.3f, 1.0f);
        }
    }
    
    /**
     * Cleanup method
     */
    public void cleanup() {
        openShops.clear();
    }
    
    /**
     * Shop item data class
     */
    private static class ShopItem {
        final String name;
        final Material material;
        final int price;
        final List<String> description;
        
        ShopItem(String name, Material material, int price, List<String> description) {
            this.name = name;
            this.material = material;
            this.price = price;
            this.description = description;
        }
    }
}