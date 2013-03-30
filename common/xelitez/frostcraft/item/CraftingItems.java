package xelitez.frostcraft.item;

import java.util.List;

import xelitez.frostcraft.registry.CreativeTabs;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;

public class CraftingItems extends ItemBase
{
	String[] name = new String[] {"Iceball", "Ice-Covered String", "CFU Handler", "Frost Transformer", "Compressor", "Frost Sprayer", "CFU Storage Handler"};
	Icon[] icons;
	
	public CraftingItems(int par1) 
	{
		super(par1);
        this.setMaxDamage(0);
        this.setCreativeTab(CreativeTabs.FCMiscItems);
        this.setHasSubtypes(true);
	}
	
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
	public void getSubItems(int par1, net.minecraft.creativetab.CreativeTabs par2CreativeTabs, List par3List)
    {
    	for(int i = 0;i < 7;i++)
    	{
    		par3List.add(new ItemStack(par1, 1, i));
    	}
    }
	
    @Override
    public int getMetadata(int par1)
    {
        return par1;
    }
    
    @Override
    public Icon getIconFromDamage(int par1)
    {
        return icons[par1];
    }
    
    @Override
    public void updateIcons(IconRegister par1IconRegister)
    {
    	icons = new Icon[7];
    	icons[0] = par1IconRegister.registerIcon("FrostCraft:iceball");
    	icons[1] = par1IconRegister.registerIcon("FrostCraft:ice_covered_string");
    	icons[2] = par1IconRegister.registerIcon("FrostCraft:cfu_handler");
    	icons[3] = par1IconRegister.registerIcon("FrostCraft:frost_transformer");
    	icons[4] = par1IconRegister.registerIcon("FrostCraft:compressor");
    	icons[5] = par1IconRegister.registerIcon("FrostCraft:frost_sprayer");
    	icons[6] = par1IconRegister.registerIcon("FrostCraft:cfu_storage_handler");
    }
    
    @Override
    public String getUnlocalizedName(ItemStack par1ItemStack)
    {
    	return "XFC." + this.name[par1ItemStack.getItemDamage()];
    }

}
