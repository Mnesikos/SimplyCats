package com.github.mnesikos.simplycats;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

public class CommandBeckon implements ICommand {
	private final List<String> aliases;
	
	public CommandBeckon() {
		aliases = new ArrayList<String>();
		aliases.add("beckon");
		aliases.add("bekn");
	}
	
	@Override
	public int compareTo(Object o) {
		return 0;
	}

	@Override
	public String getCommandName() {
		return "beckon";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "command.beckon.usage";
	}

	@Override
	public List<?> getCommandAliases() {
		return this.aliases;
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {
		if (sender instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) sender;
			World world = player.getEntityWorld();
			if (world.isRemote)
				return;
			
			else {
				if (args.length == 0) {
					player.addChatComponentMessage(new ChatComponentTranslation("command.beckon"));
				} 
				
				else if (args[0].equals("help") || args[0].equals("?")) {
					if (args.length == 1) {
						ChatComponentTranslation usageMessage = new ChatComponentTranslation("command.beckon.usage");
						usageMessage.getChatStyle().setColor(EnumChatFormatting.RED);
						player.addChatMessage(usageMessage);
						player.addChatComponentMessage(new ChatComponentTranslation("command.beckon.help"));
					}
					else if (args.length > 1) {
						if (args[1].equals("base"))
							player.addChatComponentMessage(new ChatComponentTranslation("command.beckon.help.base"));
						else if (args[1].equals("eyes"))
							player.addChatComponentMessage(new ChatComponentTranslation("command.beckon.help.eyes"));
						else if (args[1].equals("white"))
							player.addChatComponentMessage(new ChatComponentTranslation("command.beckon.help.white"));
						else {
							ChatComponentTranslation helpFailMsg = new ChatComponentTranslation("command.beckon.help.fail");
							helpFailMsg.getChatStyle().setColor(EnumChatFormatting.RED);
							player.addChatMessage(helpFailMsg);
						}
					}
				} 
				
				else if (args.length == 5){
					/*EntityCat cat = new EntityCat(world);
					cat.setType(0);
					if (args[0].equals("male") || args[0].equals("female")) {
						int sex = args[0].equals("female") ? 0 : 1;
						cat.setSex((byte) sex);
					} else
						cat.setSex((byte) world.rand.nextInt(2));
					
					if (args[1].equals("x"))
						cat.setBase(world.rand.nextInt(4));
					else {
						int base = Integer.parseInt(args[1]);
						if (base >= 0 && base <= 3)
							cat.setBase(base);
					}
					
					if (args[3].equals("true") || args[3].equals("false")) {
						int tabby = args[3].equals("true") ? cat.getBase()+1 : 0;
						cat.setMarkings("tabby", tabby);
					} else
						cat.setMarkings("tabby", cat.selectMarkings("tabby"));
					
					if (args[4].equals("x"))
						cat.setMarkings("white", cat.selectMarkings("white"));
					else {
						int white = Integer.parseInt(args[4]);
						if (white >= 0 && white <= 6)
							cat.setMarkings("white", white);
					}
					
					if (args[2].equals("x"))
						cat.setMarkings("eyes", cat.selectMarkings("eyes"));
					else {
						int eyes = Integer.parseInt(args[2]);
						if (eyes >= 0 && eyes <= 3)
							cat.setMarkings("eyes", eyes);
						else if (eyes >= 4) {
							if (cat.getMarkingNum("white") >= 5)
								cat.setMarkings("eyes", eyes);
							else if (cat.getMarkingNum("white") < 5)
								cat.setMarkings("eyes", cat.selectMarkings("eyes"));
						}
					}
					
					player.addChatComponentMessage(new ChatComponentTranslation("command.beckon.success"));
					cat.setPosition(Minecraft.getMinecraft().objectMouseOver.blockX, Minecraft.getMinecraft().objectMouseOver.blockY + 1, Minecraft.getMinecraft().objectMouseOver.blockZ);
					world.spawnEntityInWorld(cat);
					cat.setTamed(true);
			        cat.func_152115_b(((EntityPlayer) player).getUniqueID().toString());*/
					player.addChatMessage(new ChatComponentText("Stop"));
				} else {
					ChatComponentTranslation beckonFailMsg = new ChatComponentTranslation("command.beckon.fail");
					beckonFailMsg.getChatStyle().setColor(EnumChatFormatting.RED);
					player.addChatMessage(beckonFailMsg);
				}
			}
		}
	}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender sender) {
		if (sender instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) sender;
			if (player.capabilities.isCreativeMode)
				return true;
		}
		return false;
	}

	@Override
	public List<?> addTabCompletionOptions(ICommandSender sender, String[] args) {
		return null;
	}

	@Override
	public boolean isUsernameIndex(String[] args, int index) {
		return false;
	}

}
