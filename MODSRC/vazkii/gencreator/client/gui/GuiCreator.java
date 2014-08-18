/**
 * This Code is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 */
// Created @ 18 Apr 2013
package vazkii.gencreator.client.gui;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.BiomeGenBase;

import org.lwjgl.opengl.GL11;

import vazkii.gencreator.DataStorage;
import vazkii.gencreator.client.saving.TemporaryStructureData;
import vazkii.gencreator.client.saving.WriteStructureThread;
import vazkii.gencreator.helper.BoundingBox;
import vazkii.gencreator.helper.FullBlockData;
import vazkii.gencreator.lib.ObfuscationKeys;
import cpw.mods.fml.relauncher.ReflectionHelper;

/**
 * GuiCreator
 *
 * The Creator GUI. This is where the structure data
 * is edited, in order to be created.
 *
 * @author Vazkii
 */
public class GuiCreator extends GuiScreen {

	public static final ResourceLocation guiResource = new ResourceLocation("gencreator:textures/creator.png");

	GuiTextField textBox;

	private long ticksElapsed;
	private int rarity = 20;

	List<BiomeGenBase> availableBiomes;
	List<BiomeGenBase> chosenBiomes;

	int biomeScrollIndex = 0;
	int chosenBiomeScrollIndex = 0;

	String error = "";
	boolean correct = false;

	public GuiCreator() {
        availableBiomes = new ArrayList();
        for (BiomeGenBase biome : ReflectionHelper.<BiomeGenBase[], BiomeGenBase>getPrivateValue(BiomeGenBase.class, null, ObfuscationKeys.biomeList)) {
        	if(biome != null)
        		availableBiomes.add(biome);
        }
        chosenBiomes = new ArrayList();
	}

	@Override
	public void initGui() {
		super.initGui();

		int xSize = 176;
		int ySize = 166;
        int xStart = (width - xSize) / 2;
        int yStart = (height - ySize) / 2;

        final String allowedChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_";

		textBox = new GuiRestrictedTextField(fontRendererObj, xStart + 88, yStart + 19, 82, 12, allowedChars);
        textBox.setTextColor(0xDDDDDD);
//        textBox.func_82266_h(-1);
        textBox.setEnableBackgroundDrawing(false);
        textBox.setMaxStringLength(15);

        buttonList.clear();
        buttonList.add(new GuiButtonArrowVertical(0, xStart + 84, yStart + 49, false));
        buttonList.add(new GuiButtonArrowVertical(1, xStart + 160, yStart + 49, true));
        buttonList.add(new GuiButtonPlusMinus(2, xStart + 125, yStart + 80, false));
        buttonList.add(new GuiButtonPlusMinus(3, xStart + 145, yStart + 80, true));
        buttonList.add(new GuiButtonArrowHorizontal(4, xStart + 243, yStart + 49, false));
        buttonList.add(new GuiButtonArrowHorizontal(5, xStart + 243, yStart + 60, true));
        buttonList.add(new GuiButtonCheckbox(6, xStart + 156, yStart + 120));
        buttonList.add(new GuiButtonCheckbox(7, xStart + 156, yStart + 140));
        buttonList.add(new GuiButtonPlusMinus(8, xStart + 223, yStart + 12, true));
        buttonList.add(new GuiButtonPlusMinus(9, xStart + 223, yStart + 32, true));
        buttonList.add(new GuiButtonPlusMinus(10, xStart + 223, yStart + 52, true));
        buttonList.add(new GuiButtonInvisible(11, xStart + 8, yStart + ySize, 43, 22, "Create!"));
	}

	@Override
	protected void mouseClicked(int par1, int par2, int par3) {
		super.mouseClicked(par1, par2, par3);

		textBox.mouseClicked(par1, par2, par3);
	}

	@Override
	protected void keyTyped(char par1, int par2) {
		super.keyTyped(par1, par2);

		textBox.textboxKeyTyped(par1, par2);
	}

	@Override
	public void updateScreen() {
		if(!isCtrlKeyDown())
			++ticksElapsed;

		String error = verify();
		this.error = error;
		correct = MathHelper.stringNullOrLengthZero(error);
		((GuiButton) buttonList.get(11)).enabled = correct;
		((GuiButton) buttonList.get(11)).visible = correct;
	}

	@Override
	protected void actionPerformed(GuiButton par1GuiButton) {
		super.actionPerformed(par1GuiButton);
		boolean shift = isShiftKeyDown();

		if(par1GuiButton.id == 0)
			chosenBiomeScrollIndex = Math.max(0, chosenBiomeScrollIndex - 1);
		if(par1GuiButton.id == 1)
			chosenBiomeScrollIndex = Math.min(Math.max(0, chosenBiomes.size() - 1), chosenBiomeScrollIndex + 1);

		if(par1GuiButton.id == 2)
			rarity = Math.max(1, rarity - (shift ? 10 : 1));
		if(par1GuiButton.id == 3)
			rarity += shift ? 10 : 1;

		int maxBiomeSize = availableBiomes.size() - 3;
		if(par1GuiButton.id == 4)
			biomeScrollIndex = Math.max(0, biomeScrollIndex - 1);
		if(par1GuiButton.id == 5)
			biomeScrollIndex = Math.min(maxBiomeSize, biomeScrollIndex + 1);

		if(par1GuiButton.id == 4 || par1GuiButton.id == 5) {
			for(int i = 0; i < 3; i++) {
				BiomeGenBase biome = availableBiomes.get(biomeScrollIndex + i);
				boolean contains = chosenBiomes.contains(biome);
				GuiButtonPlusMinus button = (GuiButtonPlusMinus) buttonList.get(8 + i);
				button.setPlus(!contains);
			}
		}

		if(par1GuiButton.id >= 8 && par1GuiButton.id <= 10) {
			BiomeGenBase biome = availableBiomes.get(biomeScrollIndex + par1GuiButton.id - 8);
			boolean contains = chosenBiomes.contains(biome);
			if(contains)
				chosenBiomes.remove(biome);
			else chosenBiomes.add(biome);
			((GuiButtonPlusMinus) par1GuiButton).setPlus(contains);

			chosenBiomeScrollIndex = Math.min(chosenBiomeScrollIndex, Math.max(0, chosenBiomes.size() - 1));
		}

		if(par1GuiButton.id == 11) {
			TemporaryStructureData data = new TemporaryStructureData(DataStorage.selection.copy());
			data.rarity = rarity;
			data.validBiomes = chosenBiomes;
			data.randomizeStoneBricks = ((GuiButtonCheckbox) buttonList.get(6)).enabled;
			data.ignoreAirSpaces = ((GuiButtonCheckbox) buttonList.get(7)).enabled;

			WriteStructureThread thread = new WriteStructureThread(data, textBox.getText(), mc);

			mc.displayGuiScreen(null);
			DataStorage.selection = null;
			DataStorage.point1 = null;
			DataStorage.point2 = null;

			thread.start();
		}

		if(par1GuiButton instanceof GuiButtonCheckbox)
			((GuiButtonCheckbox) par1GuiButton).check();
	}

	@Override
	public void drawScreen(int par1, int par2, float par3) {
		Minecraft mc = Minecraft.getMinecraft();
		mc.renderEngine.bindTexture(guiResource);

		int xSize = 176;
		int ySize = 166;
		int renderXSize = 246;

		fontRendererObj.getUnicodeFlag();
        int xStart = (width - xSize) / 2;
        int yStart = (height - ySize) / 2;
		drawTexturedModalRect(xStart, yStart, 0, 0, renderXSize, ySize);

		if(correct) {
			drawTexturedModalRect(xStart + 8, yStart + ySize, 0, ySize, 43, 22);
		} else drawCenteredString(fontRendererObj, error, width / 2, yStart + 170, 0xFF4444);

		drawCenteredString(fontRendererObj, "Editing Structure", width / 2, yStart - 13, 0xFFFFFF);
		drawCenteredString(fontRendererObj, "Biomes", xStart + xSize + 35, yStart - 7, 0xFFFFFF);
		drawCenteredStringNoShadow(fontRendererObj, "Name", xStart + 127, yStart + 5, 0x444444);
		drawCenteredStringNoShadow(fontRendererObj, "Gen Biomes (" + chosenBiomes.size() + ")", xStart + 127, yStart + 36, 0x444444);

		fontRendererObj.drawString("Rarity", xStart + 87, yStart + 100, 0x444444);
		fontRendererObj.drawString("" + rarity, xStart + 138, yStart + 100, 0x444444);
		fontRendererObj.drawString("Randomize Stone Bricks", xStart + 8, yStart + 125, 0x444444);
		fontRendererObj.drawString("Ignore Air Spaces", xStart + 8, yStart + 145, 0x444444);

		fontRendererObj.setUnicodeFlag(true);
		for(int i = 0; i < 3; i++) {
			BiomeGenBase biome = availableBiomes.get(i + biomeScrollIndex);
			String biomeName = biome.biomeName;
			String truncatedBiomeName = fontRendererObj.trimStringToWidth(biome.biomeName, 38);
			if(!truncatedBiomeName.equals(biomeName))
				truncatedBiomeName = truncatedBiomeName.concat(".");
			fontRendererObj.drawStringWithShadow(truncatedBiomeName, xStart + 183, yStart + 15 + i * 20, biome.color);
		}

		if(chosenBiomes.size() > 0) {
			BiomeGenBase biome = chosenBiomes.get(chosenBiomeScrollIndex);
			String biomeName = biome.biomeName;
			String truncatedBiomeName = fontRendererObj.trimStringToWidth(biome.biomeName, 63);
			if(!truncatedBiomeName.equals(biomeName))
				truncatedBiomeName = truncatedBiomeName.concat(".");
			drawCenteredString(fontRendererObj, truncatedBiomeName, xStart + 127, yStart + 52, biome.color);
		}
		fontRendererObj.setUnicodeFlag(false);

		String sizes = DataStorage.selection.getXSize() + "x" + DataStorage.selection.getYSize() + "x" +  DataStorage.selection.getZSize();
		drawCenteredStringNoShadow(fontRendererObj, sizes, xStart + 45, yStart + 105, 0x444444);
		renderSelection(xStart, yStart);

		textBox.drawTextBox();

		super.drawScreen(par1, par2, par3);
	}

	public String verify() {
		String error = "";

		if(MathHelper.stringNullOrLengthZero(textBox.getText()))
			error = "The Structure needs a Name.";

		if(isNameTaken())
			error = "That name is already used.";

		if(chosenBiomes.isEmpty())
			error = "You need to pick one biome at least.";

		return error;
	}

	public boolean isNameTaken() {
		return DataStorage.usedNames.contains(textBox.getText());
	}

	public void renderSelection(int xStart, int yStart) {
		final float maxX = 70, maxY = 80;
		BoundingBox sel = DataStorage.selection;

		GL11.glPushMatrix();
		GL11.glTranslatef(xStart + 46, yStart + 90, zLevel);
        GL11.glTranslatef(0, 0, zLevel + 500F);

        float diag = (float) Math.sqrt(sel.getXSize() * sel.getXSize() + sel.getZSize() * sel.getZSize());
        float height = sel.getYSize();
        float scaleX = maxX / (diag + 1.4F);
        float scaleY = maxY / (height + 1.6F);
        float scale = Math.min(scaleY, scaleX);

        GL11.glScalef(scale, scale, scale);

        GL11.glRotatef(180F, 0, 0, 0);
        GL11.glRotatef(-20F, 1, 1, 0);

        GL11.glRotatef((ticksElapsed % 360F), 0, 1, 0);

		for(int x = 0; x < sel.getXSize(); x++) {
			for(int y = 0; y < sel.getYSize(); y++) {
				for(int z = 0; z < sel.getZSize(); z++) {
					FullBlockData data = sel.getBlockData(x, y, z);
					renderBlock(data.block, data.meta, x, y, z);
				}
			}
		}

		GL11.glPopMatrix();
	}

	private static final RenderBlocks renderBlocks = new RenderBlocks();

	// Renders a block, all translations applied
	public void renderBlock(Block block, int meta, int x, int y, int z) {
		if(block != null) {
			mc.renderEngine.bindTexture(TextureMap.locationBlocksTexture);

			GL11.glPushMatrix();
			GL11.glTranslatef(x - DataStorage.selection.getXSize() / 2, y, z - DataStorage.selection.getZSize() / 2);
			renderBlocks.renderBlockAsItem(block, meta, 0.85F);
			GL11.glPopMatrix();
		}
	}

    public void drawCenteredStringNoShadow(FontRenderer par1fontRendererObj, String par2Str, int par3, int par4, int par5) {
        par1fontRendererObj.drawString(par2Str, par3 - par1fontRendererObj.getStringWidth(par2Str) / 2, par4, par5);
    }
}
