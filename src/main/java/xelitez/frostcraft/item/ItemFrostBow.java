package xelitez.frostcraft.item;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import xelitez.frostcraft.enchantment.FrostEnchantment;
import xelitez.frostcraft.entity.EntityFrostArrow;
import xelitez.frostcraft.registry.FrostcraftCreativeTabs;
import xelitez.frostcraft.registry.IdMap;

public class ItemFrostBow extends ItemBow
{
    private IIcon[] icons;
    		
	public ItemFrostBow() 
	{
		super();
		this.maxStackSize = 1;
		this.setMaxDamage(284);
		this.setCreativeTab(FrostcraftCreativeTabs.FCEquipment);
	}
	
    @Override
    public void onPlayerStoppedUsing(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer, int par4)
    {
        int var6 = this.getMaxItemUseDuration(par1ItemStack) - par4;

        boolean var5 = par3EntityPlayer.capabilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId, par1ItemStack) > 0;

        if (var5 || (par3EntityPlayer.inventory.hasItem(Items.arrow) && ItemHelper.hasPlayerItem(par3EntityPlayer, new ItemStack(IdMap.itemCraftingItems, 1, 0))))
        {
            float var7 = (float)var6 / 20.0F;
            var7 = (var7 * var7 + var7 * 2.0F) / 3.0F;

            if ((double)var7 < 0.1D)
            {
                return;
            }

            if (var7 > 1.0F)
            {
                var7 = 1.0F;
            }

            EntityFrostArrow var8 = new EntityFrostArrow(par2World, par3EntityPlayer, var7 * 2.0F);

            if (var7 == 1.0F)
            {
                var8.setIsCritical(true);
            }

            int var9 = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, par1ItemStack);

            if (var9 > 0)
            {
                var8.setDamage(var8.getDamage() + (double)var9 * 0.5D + 0.5D);
            }

            int var10 = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, par1ItemStack);

            if (var10 > 0)
            {
                var8.setKnockbackStrength(var10);
            }
            
            int var11 = EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, par1ItemStack);

            if (var11 > 0)
            {
                var8.setCanFreeze(false);
            }
            
            int var12 = EnchantmentHelper.getEnchantmentLevel(FrostEnchantment.freeze.effectId, par1ItemStack);
            
            if(var12 > 0)
            {
            	var8.setFreezeLevel(var12);
            }
            
            int var13 = EnchantmentHelper.getEnchantmentLevel(FrostEnchantment.frostburn.effectId, par1ItemStack);
            
            if(var13 > 0)
            {
            	var8.setFrost(var13);
            }

            par1ItemStack.damageItem(1, par3EntityPlayer);
            par2World.playSoundAtEntity(par3EntityPlayer, "random.bow", 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + var7 * 0.5F);

            if (var5)
            {
                var8.canBePickedUp = 2;
            }
            else
            {
                par3EntityPlayer.inventory.consumeInventoryItem(Items.arrow);
                ItemHelper.consumeItemFromPlayer(par3EntityPlayer, new ItemStack(IdMap.itemCraftingItems, 1 ,0));
            }

            if (!par2World.isRemote)
            {
            	par2World.spawnEntityInWorld(var8);
            }
        }
    }

    @Override
    public ItemStack onEaten(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        return par1ItemStack;
    }

    @Override
    /**
     * How long it takes to use or consume an item
     */
    public int getMaxItemUseDuration(ItemStack par1ItemStack)
    {
        return 72000;
    }

    @Override
    /**
     * returns the action that specifies what animation to play when the items is being used
     */
    public EnumAction getItemUseAction(ItemStack par1ItemStack)
    {
        return EnumAction.bow;
    }

    @Override
    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        if (par3EntityPlayer.capabilities.isCreativeMode || (par3EntityPlayer.inventory.hasItem(Items.arrow) && ItemHelper.hasPlayerItem(par3EntityPlayer, new ItemStack(IdMap.itemCraftingItems, 1, 0))))
        {
            par3EntityPlayer.setItemInUse(par1ItemStack, this.getMaxItemUseDuration(par1ItemStack));
        }

        return par1ItemStack;
    }

    @Override
    /**
     * Return the enchantability factor of the item, most of the time is based on material.
     */
    public int getItemEnchantability()
    {
        return 1;
    }
    
    @Override
    public void registerIcons(IIconRegister par1IconRegister)
    {
    	icons = new IIcon[4];
    	icons[0] = par1IconRegister.registerIcon("frostcraft:frost_bow");
    	for(int i = 0;i < 3;i++)
    	{
    		icons[i + 1] = par1IconRegister.registerIcon(new StringBuilder().append("frostcraft:frost_bow_pull_").append(i).toString());
    	}
    }
    
    @Override
    public IIcon getIcon(ItemStack stack, int renderPass, EntityPlayer player, ItemStack usingItem, int useRemaining)
    {
        if (usingItem != null && usingItem.getItem() == IdMap.itemFrostBow)
        {
            int k = usingItem.getMaxItemUseDuration() - useRemaining;
            if (k >= 18) return icons[3];
            if (k >  13) return icons[2];
            if (k >   0) return icons[1];
        }
        return getIconIndex(stack);
    }
    
    @Override
    public IIcon getIconFromDamage(int par1)
    {
        return icons[0];
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
    	if(EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, par1ItemStack) > 0)
    	{
    		par3List.add("\u00a7oWhat a waste of a Frost Bow...");
    	}
    }
    
    @Override
    public boolean shouldRotateAroundWhenRendering()
    {
    	return false;
    }
}
