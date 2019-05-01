package calemiutils.command;

import calemiutils.CUReference;
import calemiutils.block.BlockBlueprint;
import calemiutils.config.CUConfig;
import calemiutils.init.InitBlocks;
import calemiutils.item.ItemBrush;
import calemiutils.util.Location;
import calemiutils.util.helper.ChatHelper;
import calemiutils.util.helper.WorldEditHelper;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class WorldEditCommandBase extends CommandBase {

    private final String[] commands = {"cube", "circle", "move"};

    @Override
    public String getName() {

        return "cu";
    }

    @Override
    public String getUsage(ICommandSender sender) {

        return "nothing";
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] strings, @Nullable BlockPos targetPos) {

        if (strings.length == 1) {
            return getListOfStringsMatchingLastWord(strings, commands);
        }

        if (strings.length == 2) {

            String[] names = new String[EnumDyeColor.values().length];

            for (int i = 0; i < EnumDyeColor.values().length; i++) {
                names[i] = EnumDyeColor.byMetadata(i).getName();
            }

            return getListOfStringsMatchingLastWord(strings, names);
        }

        else {

            return getListOfStringsMatchingLastWord(strings, Block.REGISTRY.getKeys());
        }
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] strings) throws NumberInvalidException {

        if (sender instanceof EntityPlayer) {

            EntityPlayer player = (EntityPlayer) sender;
            ItemStack stack = player.getHeldItemMainhand();

            if (strings.length == 0) {

                String holdBrush = "[Hold Brush]";

                ChatHelper.printModMessage(ChatFormatting.GREEN, "----- Help for " + CUReference.MOD_NAME + " -----", player);
                ChatHelper.printModMessage(ChatFormatting.GREEN, "() are optional arguments.", player);
                ChatHelper.printModMessage(ChatFormatting.GREEN, holdBrush + " /cu cube <color> (block) - Creates a cube of blueprint. <color> Color. (block) the block it replaces.", player);
                ChatHelper.printModMessage(ChatFormatting.GREEN, holdBrush + " /cu circle <color> (block) - Creates a circle of blueprint. <color> Color. (block) the block it replaces.", player);
                ChatHelper.printModMessage(ChatFormatting.GREEN, holdBrush + " /cu move - Moves the cube selection (Not implemented)", player);
            }

            else if (strings[0].equals("food")) {
                ChatHelper.printModMessage(ChatFormatting.GREEN, "Your Food is: " + player.getFoodStats().getFoodLevel(), player);
                ChatHelper.printModMessage(ChatFormatting.GREEN, "Your Saturation is: " + player.getFoodStats().getSaturationLevel(), player);
            }

            else {

                if (stack.getItem() instanceof ItemBrush) {

                    ItemBrush brush = (ItemBrush) stack.getItem();

                    BlockBlueprint block = (BlockBlueprint) InitBlocks.BLUEPRINT;

                    IBlockState state = block.getDefaultState();

                    if (strings.length > 1) {
                        state = block.getStateByPrefix(strings[1]);
                    }

                    boolean isAir = false;
                    Block mask;

                    if (strings.length > 2) {

                        mask = CommandBase.getBlockByText(sender, strings[2]);

                        if (strings[1].equalsIgnoreCase("air")) {
                            mask = Blocks.AIR;
                            isAir = true;
                        }
                    }

                    else {
                        mask = Blocks.AIR;
                        isAir = true;
                    }

                    if (brush.location1 != null && brush.location2 != null) {

                        ArrayList<Location> list = new ArrayList<>();

                        if (strings[0].equalsIgnoreCase("cube")) {
                            list = WorldEditHelper.selectCubeFromTwoPoints(brush.location1, brush.location2);
                        }

                        if (strings[0].equalsIgnoreCase("circle")) {
                            list = WorldEditHelper.selectCircleFromTwoPoints(brush.location1, brush.location2);
                        }

                        if (strings[0].equalsIgnoreCase("cube") || strings[0].equalsIgnoreCase("circle")) {

                            int count = 0;

                            for (Location loc : list) {

                                if (loc.getBlock() == mask) {
                                    count++;
                                }
                            }

                            generate(list, state, mask, isAir, player, count);
                            return;
                        }

                        if (strings[0].equalsIgnoreCase("move")) {
                            ItemBrush.getMessage(player).printMessage(ChatFormatting.RED, "Not implemented yet!");
                        }
                    }

                    else ItemBrush.getMessage(player).printMessage(ChatFormatting.RED, "You need to set two positions!");
                }

                else ChatHelper.printModMessage(ChatFormatting.RED, "You need to hold a Brush!", player);
            }
        }
    }

    private void generate(ArrayList<Location> list, IBlockState block, Block mask, boolean isAirMask, EntityPlayer player, int amount) {

        WorldEditHelper.generateCommand(list, block, mask, isAirMask, player, ItemBrush.getMessage(player));
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {

        return true;
    }

    @Override
    public int getRequiredPermissionLevel() {

        return CUConfig.misc.usePermission ? 2 : 0;
    }
}
