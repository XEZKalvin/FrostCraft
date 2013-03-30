package xelitez.frostcraft.effect;

import cpw.mods.fml.client.FMLClientHandler;
import xelitez.frostcraft.registry.Settings;
import net.minecraft.entity.EntityLiving;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class FCPotion extends Potion
{
	public static FCPotion freeze;
	public static FCPotion frostburn;
	
	private int statusIconIndex = -1;
    private String name = "";
    
    private boolean usable;
	
	protected FCPotion(int par1, boolean par2, int par3) 
	{
		super(par1, par2, par3);
	}
	
	private static FCPotion addPotion(int par1, boolean par2, int par3)
	{
		if(par1 > 32)
		{
			return null;
		}
		return new FCPotion(par1, par2, par3);
	}
	
	public static void RegisterPotionEffects()
	{
		freeze = addPotion(Settings.potionFreezeId != -1 ? Settings.potionFreezeId : getPotionId(), true, 8171462).setPotionName("Frozen").setFCIconIndex(0, 0).setUseable();
		frostburn = addPotion(Settings.potionFrostburnId != -1 ? Settings.potionFrostburnId :getPotionId(), true, 8171462).setPotionName("Frostburn").setFCIconIndex(1, 0).setUseable();
	}
	
	private static int getPotionId()
	{
		int count;
		for(count = 0;count < Potion.potionTypes.length;count++)
		{
			if(Potion.potionTypes[count] == null)
			{
				return count;
			}
		}
		return count + 1;
	}
	
	
	
    protected FCPotion setFCIconIndex(int par1, int par2)
    {
        this.statusIconIndex = par1 + par2 * 16;
        return this;
    }
    
    public FCPotion setUseable()
    {
    	this.usable = true;
    	return this;
    }
    
    public boolean isReady(int par1, int par2)
    {
    	return true;
    }
    
    public void performEffect(EntityLiving par1EntityLiving, int par2)
    {
    	if(!EffectTicker.instance().hasEntityEffect(par1EntityLiving, this))
    	{
    		EffectTicker.addEffect(par1EntityLiving, new PotionEffect(this.id, par1EntityLiving.getActivePotionEffect(this).getDuration(), par2));
    	}
    }
    
    public boolean hasStatusIcon()
    {
        return true;
    }
    
    public int getStatusIconIndex()
    {
    	FMLClientHandler.instance().getClient().renderEngine.bindTexture("/mods/FrostCraft/textures/effects.png");
        return this.statusIconIndex;
    }
    
    public FCPotion setPotionName(String par1Str)
    {
        this.name = par1Str;
        return this;
    }

    public String getName()
    {
        return this.name;
    }
    
    public boolean isUsable()
    {
        return this.usable;
    }

}
