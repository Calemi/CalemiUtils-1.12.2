package calemiutils.item;

import calemiutils.CalemiUtils;
import calemiutils.init.InitItems;
import calemiutils.registry.IHasModel;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;

import java.util.Set;

public class ItemSledgehammer extends ItemPickaxe implements IHasModel {

    private static final Set<Block> EFFECTIVE_ON = Sets.newHashSet(Blocks.CLAY, Blocks.DIRT, Blocks.FARMLAND, Blocks.GRASS, Blocks.GRAVEL, Blocks.MYCELIUM, Blocks.SAND, Blocks.SNOW, Blocks.SNOW_LAYER, Blocks.SOUL_SAND, Blocks.GRASS_PATH, Blocks.CONCRETE_POWDER, Blocks.PLANKS, Blocks.BOOKSHELF, Blocks.LOG, Blocks.LOG2, Blocks.CHEST, Blocks.PUMPKIN, Blocks.LIT_PUMPKIN, Blocks.MELON_BLOCK, Blocks.LADDER, Blocks.WOODEN_BUTTON, Blocks.WOODEN_PRESSURE_PLATE);

    private double attackSpeed, attackDamage;

    public ItemSledgehammer(String name, Item.ToolMaterial toolMaterial, double attackSpeed, double attackDamage, int maxDamage) {
        super(toolMaterial);

        String realName = "sledgehammer_" + name;
        setUnlocalizedName(realName);
        setRegistryName(realName);
        setCreativeTab(CalemiUtils.TAB);
        InitItems.ITEMS.add(this);

        this.attackSpeed = attackSpeed;
        this.attackDamage = attackDamage;
        this.setMaxDamage(maxDamage);
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        stack.damageItem(1, attacker);
        return true;
    }

    @Override
    public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot) {

        Multimap<String, AttributeModifier> multimap = super.getItemAttributeModifiers(equipmentSlot);

        if (equipmentSlot == EntityEquipmentSlot.MAINHAND) {

            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Tool modifier", (double)attackDamage, 0));
            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Tool modifier", (double)attackSpeed, 0));
        }

        return multimap;
    }

    @Override
    public Set<String> getToolClasses(ItemStack stack) {
        return ImmutableSet.of("pickaxe", "axe", "shovel");
    }

    @Override
    public boolean canHarvestBlock(IBlockState block) {
        return EFFECTIVE_ON.contains(block.getBlock()) || super.canHarvestBlock(block);
    }

    public float getDestroySpeed(ItemStack stack, IBlockState state) {

        Material material = state.getMaterial();
        if( material == Material.WOOD && material == Material.PLANTS && material == Material.VINE) {
            return this.efficiency;
        }

        return EFFECTIVE_ON.contains(state.getBlock()) ? this.efficiency : super.getDestroySpeed(stack, state);
    }

    @Override
    public void registerModels() {

        CalemiUtils.proxy.registerItemRenderer(this, 0, "inventory");
    }
}
