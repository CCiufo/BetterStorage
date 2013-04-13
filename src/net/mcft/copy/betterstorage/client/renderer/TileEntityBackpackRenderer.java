package net.mcft.copy.betterstorage.client.renderer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.mcft.copy.betterstorage.block.TileEntityBackpack;
import net.mcft.copy.betterstorage.block.TileEntityLocker;
import net.mcft.copy.betterstorage.client.model.ModelBackpack;
import net.mcft.copy.betterstorage.client.model.ModelLargeLocker;
import net.mcft.copy.betterstorage.client.model.ModelLocker;
import net.mcft.copy.betterstorage.misc.Constants;
import net.mcft.copy.betterstorage.utils.DirectionUtils;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;

@SideOnly(Side.CLIENT)
public class TileEntityBackpackRenderer extends TileEntitySpecialRenderer {
	
	private ModelBackpack backpackModel = new ModelBackpack();
	
	public void renderTileEntityAt(TileEntityBackpack backpack, double x, double y, double z, float par8) {
		
		bindTextureByName(Constants.backpackTexture);
		
		GL11.glPushMatrix();
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glTranslated(x, y + 2.0, z + 1.0);
		GL11.glScalef(1.0F, -1.0F, -1.0F);
		GL11.glTranslated(0.5F, 0.5F, 0.5F);
		
		ForgeDirection orientation = ForgeDirection.getOrientation(backpack.getBlockMetadata());
		int rotation = DirectionUtils.getRotation(orientation);
		GL11.glRotatef(rotation, 0.0F, 1.0F, 0.0F);
		
		float angle = backpack.prevLidAngle + (backpack.lidAngle - backpack.prevLidAngle) * par8;
		angle = 1.0F - angle;
		angle = 1.0F - angle * angle * angle;
		backpackModel.setLidRotation((float)(angle * Math.PI / 2.0));
		backpackModel.renderAll();
		
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		GL11.glPopMatrix();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		
	}
	
	@Override
	public void renderTileEntityAt(TileEntity entity, double x, double y, double z, float par8) {
		renderTileEntityAt((TileEntityBackpack)entity, x, y, z, par8);
	}
	
}
