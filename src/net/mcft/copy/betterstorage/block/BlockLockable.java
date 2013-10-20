package net.mcft.copy.betterstorage.block;

import java.util.List;
import java.util.Random;

import net.mcft.copy.betterstorage.api.BetterStorageEnchantment;
import net.mcft.copy.betterstorage.attachment.Attachments;
import net.mcft.copy.betterstorage.attachment.EnumAttachmentInteraction;
import net.mcft.copy.betterstorage.attachment.IHasAttachments;
import net.mcft.copy.betterstorage.block.tileentity.TileEntityLockable;
import net.mcft.copy.betterstorage.utils.WorldUtils;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class BlockLockable extends BlockContainerBetterStorage {
	
	protected BlockLockable(int id, Material material) {
		super(id, material);
	}
	
	public boolean hasMaterial() { return true; }
	
	@Override
	public void getSubBlocks(int id, CreativeTabs tab, List list) {
		if (!hasMaterial()) super.getSubBlocks(id, tab, list);
		else for (ContainerMaterial material : ContainerMaterial.getMaterials())
			list.add(material.setMaterial(new ItemStack(id, 1, 0)));
	}
	
	@Override
	public boolean removeBlockByPlayer(World world, EntityPlayer player, int x, int y, int z) {
		if (hasMaterial() && !player.capabilities.isCreativeMode)
			dropBlockAsItem_do(world, x, y, z, WorldUtils.get(world, x, y, z, TileEntityLockable.class).material.setMaterial(new ItemStack(blockID, 1, 0)));
		return super.removeBlockByPlayer(world, player, x, y, z);
	}
	@Override
	public void onBlockExploded(World world, int x, int y, int z, Explosion explosion) {
		if (hasMaterial())
			dropBlockAsItem_do(world, x, y, z, WorldUtils.get(world, x, y, z, TileEntityLockable.class).material.setMaterial(new ItemStack(blockID, 1, 0)));
		super.onBlockExploded(world, x, y, z, explosion);
	}
	
	@Override
	public int quantityDropped(int meta, int fortune, Random random) { return (hasMaterial() ? 0 : 1); }
	
	@Override
	public float getBlockHardness(World world, int x, int y, int z) {
		TileEntityLockable lockable = WorldUtils.get(world, x, y, z, TileEntityLockable.class);
		if ((lockable != null) && (lockable.getLock() != null)) return -1;
		else return super.getBlockHardness(world, x, y, z);
	}
	
	@Override
	public float getExplosionResistance(Entity entity, World world, int x, int y, int z, double explosionX, double explosionY, double explosionZ) {
		float modifier = 1.0F;
		TileEntityLockable lockable = WorldUtils.get(world, x, y, z, TileEntityLockable.class);
		if (lockable != null) {
			int persistance = BetterStorageEnchantment.getLevel(lockable.getLock(), "persistance");
			if (persistance > 0) modifier += Math.pow(2, persistance);
		}
		return super.getExplosionResistance(entity) * modifier;
	}
	
	@Override
	public MovingObjectPosition collisionRayTrace(World world, int x, int y, int z, Vec3 start, Vec3 end) {
		return WorldUtils.get(world, x, y, z, IHasAttachments.class).getAttachments().rayTrace(world, x, y, z, start, end);
	}
	
	@Override
	public void onBlockClicked(World world, int x, int y, int z, EntityPlayer player) {
		// TODO: See if we can make a pull request to Forge to get PlayerInteractEvent to fire for left click on client.
		Attachments attachments = WorldUtils.get(world, x, y, z, IHasAttachments.class).getAttachments();
		boolean abort = attachments.interact(WorldUtils.rayTrace(player, 1.0F), player, EnumAttachmentInteraction.attack);
		// TODO: Abort block breaking? playerController.resetBlockBreaking doesn't seem to do the job.
	}
	
	// Trigger enchantment related
	
	@Override
	public boolean canProvidePower() { return true; }
	
	@Override
	public int isProvidingWeakPower(IBlockAccess world, int x, int y, int z, int side) {
		return (WorldUtils.get(world, x, y, z, TileEntityLockable.class).isPowered() ? 15 : 0);
	}
	@Override
	public int isProvidingStrongPower(IBlockAccess world, int x, int y, int z, int side) {
		return isProvidingWeakPower(world, x, y, z, side);
	}
	
	@Override
	public void updateTick(World world, int x, int y, int z, Random random) {
		WorldUtils.get(world, x, y, z, TileEntityLockable.class).setPowered(false);
	}
	
}
