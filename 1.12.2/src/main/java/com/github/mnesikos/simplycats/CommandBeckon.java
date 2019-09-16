package com.github.mnesikos.simplycats;

import com.github.mnesikos.simplycats.entity.EntityCat;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class CommandBeckon implements ICommand {
    private final List<String> aliases;

    public CommandBeckon() {
        aliases = new ArrayList<>();
        aliases.add("beckon");
        aliases.add("cat");
    }

    @Override
    public int compareTo(ICommand o) {
        return 0;
    }

    @Override
    public String getName() {
        return "beckon";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return new TextComponentTranslation("command.beckon.usage").getFormattedText();
    }

    @Override
    public List<String> getAliases() {
        return this.aliases;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        /*if (sender instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) sender;
            World world = player.getEntityWorld();
            if (!world.isRemote) {
                if (args.length == 0)
                    player.sendMessage(new TextComponentTranslation("command.beckon"));

                else if (args[0].equals("help") || args[0].equals("?")) {
                    if (args.length == 1) {
                        player.sendMessage(new TextComponentTranslation("command.beckon.usage"));
                        player.sendMessage(new TextComponentTranslation("command.beckon.help"));
                    }
                    else {
                        switch (args[1]) {
                            case "base":
                                player.sendMessage(new TextComponentTranslation("command.beckon.help.base"));
                                break;
                            case "eyes":
                                player.sendMessage(new TextComponentTranslation("command.beckon.help.eyes"));
                                break;
                            case "white":
                                player.sendMessage(new TextComponentTranslation("command.beckon.help.white"));
                                break;
                            default:
                                player.sendMessage(new TextComponentTranslation(TextFormatting.RED + "command.beckon.help.fail"));
                                break;
                        }
                    }
                }

                // TODO add optional argument for a player to set as owner
                else if (args.length == 5){
                    EntityCat cat = new EntityCat(world);
                    cat.setType(0);

                    // SET SEX
                    if (args[0].equals("male") || args[0].equals("female")) {
                        int sex = args[0].equals("female") ? 0 : 1;
                        cat.setSex((byte) sex);
                    } else
                        cat.setSex((byte) world.rand.nextInt(2));

                    // SET BASE
                    if (args[1].equals("x"))
                        cat.setBase(world.rand.nextInt(4));
                    else {
                        int base = Integer.parseInt(args[1]);
                        if (base >= 0 && base <= 3)
                            cat.setBase(base);
                        //else
                        //    cat.setBase(world.rand.nextInt(4));
                    }

                    // SET TORTIE
                    if (cat.getSex() == 0 && world.rand.nextInt(4) == 0 && cat.getBase() <= 1) {
                        // 25% chance black or grey female cats are tortie
                        int tortie = cat.getBase() == 1 ? 2 : 1;
                        cat.setMarkings("tortie", tortie);
                    } else
                        cat.setMarkings("tortie", 0);

                    // SET TABBY
                    if (args[3].equals("x"))
                        cat.setMarkings("tabby", cat.selectMarkings("tabby"));
                    else {
                        if (args[3].equals("true"))
                            cat.setMarkings("tabby", cat.getBase() + 1);
                        else if (cat.getBase() == 2 || cat.getBase() == 3) // red || cream are always tabby
                            cat.setMarkings("tabby", cat.getBase() + 1);
                        else
                            cat.setMarkings("tabby", 0);
                    }

                    // SET WHITE MARKINGS
                    if (args[4].equals("x"))
                        cat.setMarkings("white", cat.selectMarkings("white"));
                    else {
                        int white = Integer.parseInt(args[4]);
                        if (white >= 0 && white <= 6)
                            cat.setMarkings("white", white);
                        else
                            cat.setMarkings("white", cat.selectMarkings("white"));
                    }

                    // SET EYES
                    if (args[2].equals("x"))
                        cat.setMarkings("eyes", cat.selectMarkings("eyes"));
                    else {
                        int eyes = Integer.parseInt(args[2]);
                        if (eyes >= 0 && eyes <= 3)
                            cat.setMarkings("eyes", eyes);
                        else if (eyes == 4) {
                            if (cat.getMarkingNum("white") >= 5)
                                cat.setMarkings("eyes", eyes);
                            else if (cat.getMarkingNum("white") < 5)
                                cat.setMarkings("eyes", cat.selectMarkings("eyes"));
                        } else
                            cat.setMarkings("eyes", cat.selectMarkings("eyes"));
                    }

                    player.sendMessage(new TextComponentTranslation("command.beckon.success"));
                    cat.setPosition(Minecraft.getMinecraft().objectMouseOver.getBlockPos().getX(), Minecraft.getMinecraft().objectMouseOver.getBlockPos().getY() + 1, Minecraft.getMinecraft().objectMouseOver.getBlockPos().getZ());
                    world.spawnEntity(cat);
                    cat.setTamed(true);
                    cat.setOwnerId(player.getUniqueID());
                    cat.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(cat)), null);
                } else {
                    TextComponentTranslation beckonFailMsg = new TextComponentTranslation("command.beckon.fail" + TextFormatting.RED);
                    player.sendMessage(beckonFailMsg);
                }
            }
        }*/
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        if (sender instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) sender;
            return player.capabilities.isCreativeMode;
        }
        return false;
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        return null;
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return false;
    }
}
