package net.blay09.mods.chattweaks.gui.chat;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.resources.I18n;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraftforge.fml.client.FMLClientHandler;

import java.io.IOException;

public class GuiSleepMPExt extends GuiChatExt {

	public GuiSleepMPExt() {
		super("");
	}

	@Override
	public void initGui() {
		super.initGui();
		buttonList.add(new GuiButton(1, width / 2 - 100, height - 40, I18n.format("multiplayer.stopSleeping")));
	}

	@Override
	public void updateScreen() {
		super.updateScreen();

		if (!FMLClientHandler.instance().getClientPlayerEntity().isPlayerSleeping()) {
			Minecraft.getMinecraft().displayGuiScreen(null);
		}
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if (keyCode == 1) {
			wakeFromSleep();
		} else {
			super.keyTyped(typedChar, keyCode);
		}
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id == 1) {
			wakeFromSleep();
		} else {
			super.actionPerformed(button);
		}
	}

	private void wakeFromSleep() {
		EntityPlayerSP player = FMLClientHandler.instance().getClientPlayerEntity();
		NetHandlerPlayClient netHandler = player.connection;
		netHandler.sendPacket(new CPacketEntityAction(player, CPacketEntityAction.Action.STOP_SLEEPING));
		Minecraft.getMinecraft().displayGuiScreen(null);
	}
}
