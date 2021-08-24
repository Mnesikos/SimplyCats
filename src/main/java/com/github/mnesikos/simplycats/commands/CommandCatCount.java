package com.github.mnesikos.simplycats.commands;

import com.github.mnesikos.simplycats.SCNetworking;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.github.mnesikos.simplycats.SCNetworking.CHANNEL;

public class CommandCatCount extends CommandBase {
    private final List<String> aliases;

    public CommandCatCount() {
        aliases = new ArrayList<>();
        aliases.add("scc");
        aliases.add("catcount");
    }

    @Override
    public String getName() {
        return "setcatcount";
    }

    @Override
    public List<String> getAliases() {
        return this.aliases;
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "command.cat_count.usage";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length == 2) {
            EntityPlayer entityplayer = getPlayer(server, sender, args[0]);

            entityplayer.getEntityData().setInteger("CatCount", parseInt(args[1]));
            if (!entityplayer.world.isRemote)
                CHANNEL.sendTo(new SCNetworking(parseInt(args[1])), (EntityPlayerMP) entityplayer);
            notifyCommandListener(sender, this, "command.cat_count.success", args[0], args[1]);

        } else if (args.length == 1) {
            EntityPlayer entityplayer = getPlayer(server, sender, args[0]);

            int tamed_count = entityplayer.getEntityData().getInteger("CatCount");
            notifyCommandListener(sender, this, "command.cat_count.return_count", args[0], tamed_count);

        } else
            throw new WrongUsageException("command.cat_count.usage");
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        if (args.length == 1)
            return getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
        else
            return Collections.emptyList();
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return index == 0;
    }
}
