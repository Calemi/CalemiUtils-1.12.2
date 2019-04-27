package calemiutils.block.base;

import calemiutils.CalemiUtils;
import calemiutils.init.InitBlocks;
import calemiutils.init.InitItems;
import calemiutils.registry.IHasModel;
import calemiutils.util.MaterialSound;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemCloth;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockColoredBase extends Block implements IHasModel {

    protected static final PropertyEnum<EnumDyeColor> COLOR = PropertyEnum.create("color", EnumDyeColor.class);

    protected BlockColoredBase(String name, MaterialSound matSound, float hardness, int harvestLevel, float resistance) {

        super(matSound.mat);
        setUnlocalizedName(name);
        setRegistryName(name);

        setSoundType(matSound.sound);
        setHardness(hardness);
        setHarvestLevel("pickaxe", harvestLevel);
        setResistance(resistance);

        if (getRegistryName() != null) {

            InitBlocks.BLOCKS.add(this);
            InitItems.ITEMS.add(new ItemCloth(this).setRegistryName(getRegistryName()));
        }
    }

    @Override
    public void registerModels() {

        for (int i = 0; i < EnumDyeColor.values().length; i++) CalemiUtils.proxy.registerItemRenderer(Item.getItemFromBlock(this), i, "color=" + EnumDyeColor.byMetadata(i).getName());
    }

    @Override
    public int damageDropped(IBlockState state) {

        return (state.getValue(COLOR)).getMetadata();
    }

    @Override
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {

        for (EnumDyeColor enumdyecolor : EnumDyeColor.values()) {
            items.add(new ItemStack(this, 1, enumdyecolor.getMetadata()));
        }
    }

    @Override
    public MapColor getMapColor(IBlockState state, IBlockAccess worldIn, BlockPos pos) {

        return MapColor.getBlockColor(state.getValue(COLOR));
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {

        return this.getDefaultState().withProperty(COLOR, EnumDyeColor.byMetadata(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state) {

        return (state.getValue(COLOR)).getMetadata();
    }

    @Override
    protected BlockStateContainer createBlockState() {

        return new BlockStateContainer(this, COLOR);
    }
}
