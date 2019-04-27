/*package calemiutils.tileentity;

import java.util.ArrayList;
import java.util.List;

import calemiutils.config.CUConfig;
import calemiutils.gui.GuiDiggingUnit;
import calemiutils.inventory.ContainerDiggingUnit;
import calemiutils.tileentity.base.ICurrencyNetworkReciever;
import calemiutils.tileentity.base.TileEntityUpgradable;
import calemiutils.util.EnumOreCost;
import calemiutils.util.Location;
import calemiutils.util.helper.MathHelper;
import calemiutils.util.helper.WorldEditHelper;
import net.minecraft.block.Block;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

public class TileEntityMiningUnitCopy extends TileEntityUpgradable implements ICurrencyNetworkReciever {
	
	public int storedCurrency = 0;
	
	public List<Location> oreLocations = new ArrayList<Location>();
	private Location currentOre = null;
	
	private boolean shouldCheckForLocations;
	
	public TileEntityMiningUnitCopy() {
		setInputSlots(MathHelper.getCountingArray(0, 28));
		setSideInputSlots(MathHelper.getCountingArray(0, 28));
		setExtractSlots(MathHelper.getCountingArray(2, 28));
		enable = false;
		shouldCheckForLocations = true;
	}
	
	@Override
	public void update() {
		
		if (enable) {	
			
			if (shouldCheckForLocations) {				
				oreLocations = getOreLocations();
				shouldCheckForLocations = false;
			}
						
			if (!oreLocations.isEmpty()) {
								
				if (currentOre == null) {
					findCurrentOre();
				}			
				
				else {
					
					ItemStack stack = new ItemStack(currentOre.getBlock(), 1, currentOre.getBlockMeta());
					
					if (canFitStack(stack) && storedCurrency >= getCurrentOreCost()) {
						
						tickProgress();											
						
						if (isDoneAndReset()) {
							
							storedCurrency -= getCurrentOreCost();
							if (!world.isRemote) addItemStack(stack);
							currentOre.setBlock(getBlockToReplace());
							oreLocations.remove(currentOre);
							currentOre = null;							
						}
					}	
					
					else {
						currentProgress = 0;
					}
				}
			}
			
			else if (currentRange < getScaledRange()) {
				currentRange++;
				shouldCheckForLocations = true;
			}
		}
		
		else {
			currentOre = null;
			currentProgress = 0;
			currentRange = 0;
			shouldCheckForLocations = true;
		}
	}
	
	public Block getBlockToReplace() {
		
		int dim = world.provider.getDimensionType().getId();
		
		if (dim == -1) return Blocks.NETHERRACK;
		if (dim == 1) return Blocks.END_STONE; 
		
		return Blocks.STONE;
	}
	
	public ItemStack getCurrentOreStack() {
		
		if (currentOre != null) {
			ItemStack stack = new ItemStack(currentOre.getBlock(), 1, currentOre.getBlockMeta());
			return stack;
		}
		
		return null;
	}
	
	public int getCurrentOreCost() {
		
		EnumOreCost oreCost = EnumOreCost.getFromStack(getCurrentOreStack());
		
		if (oreCost != null) {
			return oreCost.cost;
		}		
		
		return 0;
	}
	
	public void findCurrentOre() {
		
		for (Location location : oreLocations) {
			
			ItemStack stack = new ItemStack(location.getBlock(), 1, location.getBlockMeta());
			
			EnumOreCost oreCost = EnumOreCost.getFromStack(stack);
			
			if (oreCost != null && !location.isAirBlock()) {				
				currentOre = location;				
				return;
			}
		}
		
		currentOre = null;
	}
	
	public List<Location> getOreLocations() {
		
		ArrayList<Location> locations = WorldEditHelper.selectWallsFromRadius(getLocation(), currentRange, -getLocation().y, getLocation().y);
			
		ArrayList<Location> oreList = new ArrayList<Location>();
		
		for (Location location : locations) {
			
			ItemStack stack = new ItemStack(location.getBlock(), 1, location.getBlockMeta());
					
			for (EnumOreCost oreCost : EnumOreCost.values()) {
				
				for (ItemStack oreStack : OreDictionary.getOres(oreCost.oreDict)) {
										
					if (ItemStack.areItemsEqual(oreStack, stack)) {						
						oreList.add(location);
					}
				}
			}
		}
				
		return oreList;
	}
	
	public void addItemStack(ItemStack stack) {
		
		for (int i = 2; i < getSizeInventory(); i++) {
			
			if (ItemStack.areItemsEqual(getStackInSlot(i), stack) && getStackInSlot(i).getCount() < getInventoryStackLimit()) {
				decrStackSize(i, -1);
				return;
			}
			
			else if (getStackInSlot(i).isEmpty()) {
				setInventorySlotContents(i, stack);
				return;
			}					
		}
	}
	
	public boolean canFitStack(ItemStack stack) {
		
		for (int i = 2; i < getSizeInventory(); i++) {
			
			if (getStackInSlot(i).isEmpty() || (ItemStack.areItemsEqual(getStackInSlot(i), stack)) && getStackInSlot(i).getCount() < getInventoryStackLimit()) {
				return true;
			}			
		}
		
		return false;
	}
	
	@Override
	public int getSizeInventory() {
		return 29;
	}

	@Override
	public Container getTileContainer(EntityPlayer player) {
		return new ContainerDiggingUnit(player, this);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public GuiContainer getTileGuiContainer(EntityPlayer player) {
		return new GuiDiggingUnit(player, this);
	}

	@Override
	public int getSpeedSlot() {
		return 0;
	}

	@Override
	public int getRangeSlot() {
		return 1;
	}

	@Override
	public int getMaxProgress() {
		return 100;
	}

	@Override
	public int getScaledSpeedMin() {
		return 3;
	}

	@Override
	public int getScaledSpeedMax() {
		return 15;
	}

	@Override
	public int getScaledRangeMin() {
		return 10;
	}

	@Override
	public int getScaledRangeMax() {
		return CUConfig.misc.miningUnitMaxRange;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		storedCurrency = nbt.getInteger("currency");
		super.readFromNBT(nbt);	
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		nbt.setInteger("currency", storedCurrency);
		return super.writeToNBT(nbt);	
	}

	@Override
	public EnumFacing[] getConnectedDirections() {
		return EnumFacing.VALUES;
	}

	@Override
	public int getStoredCurrency() {
		return storedCurrency;
	}

	@Override
	public int getMaxCurrency() {
		return CUConfig.misc.miningUnitCurrencyCapacity;
	}

	@Override
	public int setCurrency(int amount) {
		int setAmount = amount;
		
		if (amount > getMaxCurrency()) {
			setAmount = getMaxCurrency();			
		}
		
		storedCurrency = setAmount;
		
		return setAmount;
	}
}*/
