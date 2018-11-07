package me.calebbassham.scenariomanager.timber;

import me.calebbassham.scenariomanager.api.SimpleScenario;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.LinkedList;

public class Scenario extends SimpleScenario implements Listener {

    private BlockFace[] blockFaces = new BlockFace[]{BlockFace.UP, BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST};
    private Material[] treeMaterials = new Material[]{Material.OAK_WOOD, Material.BIRCH_WOOD, Material.JUNGLE_WOOD, Material.ACACIA_WOOD, Material.DARK_OAK_WOOD, Material.SPRUCE_WOOD};
    private Material[] tools = new Material[]{Material.WOODEN_AXE, Material.STONE_AXE, Material.IRON_AXE, Material.GOLDEN_AXE, Material.DIAMOND_AXE};

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        World world = e.getBlock().getWorld();
        if(!scenarioManager.getGameWorldProvider().isGameWorld(world)) return;

        LinkedList<Block> treeBlocks = getBlocksOfTree(e.getBlock());
        breakTree(e.getPlayer(), treeBlocks);

    }

    private boolean isTreeMaterial(Material material) {
        return arrayContains(treeMaterials, material);
    }

    private LinkedList<Block> getBlocksOfTree(Block startingBlock) {
        LinkedList<Block> blocks = new LinkedList<>();

        if (!isTreeMaterial(startingBlock.getType())) return blocks;

        blocks.add(startingBlock);

        for (BlockFace face : blockFaces) {
            Block loopBlock = startingBlock.getRelative(face);
            blocks.addAll(getBlocksOfTree(loopBlock));
        }

        return blocks;
    }

    private boolean isTool(ItemStack itemStack) {
        return arrayContains(tools, itemStack.getType());
    }

    private void damageTool(ItemStack tool, int damage) {
        Damageable meta = (Damageable) tool.getItemMeta();
        meta.setDamage(meta.getDamage() + damage);
        tool.setItemMeta((ItemMeta) meta);
    }

    private void breakTree(Player player, LinkedList<Block> blocks) {
        ItemStack tool = player.getInventory().getItemInMainHand();
        if (isTool(tool)) {
            damageTool(tool, blocks.size());
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                if (blocks.isEmpty()) {
                    cancel();
                    return;
                }

                Block block = blocks.poll();
                block.breakNaturally(tool);
            }
        }.runTaskTimer(plugin, 0, 5);
    }

    private <E> boolean arrayContains(E[] elements, E element) {
        for (E l : elements) {
            if (l.equals(element)) return true;
        }

        return false;
    }

}
