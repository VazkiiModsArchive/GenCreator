/**
 * This Code is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 */
// Created @ 18 Apr 2013
package vazkii.gencreator.client;

import java.awt.Color;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ChunkCoordinates;
import net.minecraftforge.client.event.RenderWorldLastEvent;

import org.lwjgl.opengl.GL11;

import vazkii.gencreator.DataStorage;
import vazkii.gencreator.helper.BoundingBox;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

/**
 * SelectionRenderHandler
 *
 * Render for the world selection.
 *
 * @author Vazkii
 */
public class SelectionRenderHandler {

	@SubscribeEvent
	public void onWorldRenderLast(RenderWorldLastEvent event) {
		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_BLEND);

		Tessellator.renderingWorldRenderer = false;

		if(DataStorage.selection != null) {
			BoundingBox sel = DataStorage.selection;
			ChunkCoordinates startCoord = new ChunkCoordinates(sel.minX, sel.minY, sel.minZ);
			ChunkCoordinates size = new ChunkCoordinates(sel.getXSize(), sel.getYSize(), sel.getZSize());

			renderSizedOutlineAt(startCoord, 0x222299, size);
		}

		if(DataStorage.point1 != null)
			renderBlockOutlineAt(DataStorage.point1, 0xFFFF00);

		if(DataStorage.point2 != null)
			renderBlockOutlineAt(DataStorage.point2, 0x00FF00);

		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);

		GL11.glPopMatrix();
	}

	private void renderBlockOutlineAt(ChunkCoordinates pos, int color) {
		GL11.glPushMatrix();
		GL11.glTranslated(pos.posX - RenderManager.renderPosX, pos.posY + 1 - RenderManager.renderPosY, pos.posZ - RenderManager.renderPosZ);
		GL11.glScalef(1.0F, -1.0F, -1.0F);
		Color colorRGB = new Color(color);
		GL11.glColor4ub((byte) colorRGB.getRed(), (byte) colorRGB.getGreen(), (byte) colorRGB.getBlue(), (byte) 127);
		renderBlockOutline();
		GL11.glPopMatrix();
	}

	private void renderSizedOutlineAt(ChunkCoordinates pos, int color, ChunkCoordinates size) {
		GL11.glPushMatrix();
		GL11.glTranslated(pos.posX - RenderManager.renderPosX, pos.posY - RenderManager.renderPosY, pos.posZ - RenderManager.renderPosZ);
		//GL11.glScalef(1.0F, -1.0F, -1.0F);
		Color colorRGB = new Color(color);
		GL11.glColor4ub((byte) colorRGB.getRed(), (byte) colorRGB.getGreen(), (byte) colorRGB.getBlue(), (byte) 190);
		renderSizedOutline(size);
		GL11.glPopMatrix();
	}


	// Must be translated
	private void renderBlockOutline() {
		Tessellator tessellator = Tessellator.instance;

		tessellator.startDrawing(GL11.GL_LINES);

		tessellator.addVertex(0, 0, 0);
		tessellator.addVertex(0, 1, 0);
		tessellator.addVertex(0, 1, 0);
		tessellator.addVertex(1, 1, 0);
		tessellator.addVertex(1, 1, 0);
		tessellator.addVertex(1, 0, 0);
		tessellator.addVertex(1, 0, 0);
		tessellator.addVertex(0, 0, 0);
		tessellator.addVertex(0, 0, -1);
		tessellator.addVertex(0, 1, -1);
		tessellator.addVertex(0, 0, -1);
		tessellator.addVertex(1, 0, -1);
		tessellator.addVertex(1, 0, -1);
		tessellator.addVertex(1, 1, -1);
		tessellator.addVertex(0, 1, -1);
		tessellator.addVertex(1, 1, -1);
		tessellator.addVertex(0, 0, 0);
		tessellator.addVertex(0, 0, -1);
		tessellator.addVertex(0, 1, 0);
		tessellator.addVertex(0, 1, -1);
		tessellator.addVertex(1, 0, 0);
		tessellator.addVertex(1, 0, -1);
		tessellator.addVertex(1, 1, 0);
		tessellator.addVertex(1, 1, -1);

		tessellator.draw();
	}

	// Must be translated
	private void renderSizedOutline(ChunkCoordinates size) {
		Tessellator tessellator = Tessellator.instance;

		tessellator.startDrawing(GL11.GL_LINES);


		tessellator.addVertex(0, 0, 0);
		tessellator.addVertex(0, size.posY, 0);
		tessellator.addVertex(0, size.posY, 0);
		tessellator.addVertex(size.posX, size.posY, 0);
		tessellator.addVertex(size.posX, size.posY, 0);
		tessellator.addVertex(size.posX, 0, 0);
		tessellator.addVertex(size.posX, 0, 0);
		tessellator.addVertex(0, 0, 0);
		tessellator.addVertex(0, 0, size.posZ);
		tessellator.addVertex(0, size.posY, size.posZ);
		tessellator.addVertex(0, 0, size.posZ);
		tessellator.addVertex(size.posX, 0, size.posZ);
		tessellator.addVertex(size.posX, 0, size.posZ);
		tessellator.addVertex(size.posX, size.posY, size.posZ);
		tessellator.addVertex(0, size.posY, size.posZ);
		tessellator.addVertex(size.posX, size.posY, size.posZ);
		tessellator.addVertex(0, 0, 0);
		tessellator.addVertex(0, 0, size.posZ);
		tessellator.addVertex(0, size.posY, 0);
		tessellator.addVertex(0, size.posY, size.posZ);
		tessellator.addVertex(size.posX, 0, 0);
		tessellator.addVertex(size.posX, 0, size.posZ);
		tessellator.addVertex(size.posX, size.posY, 0);
		tessellator.addVertex(size.posX, size.posY, size.posZ);

		tessellator.draw();
	}

}
