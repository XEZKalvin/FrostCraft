package xelitez.frostcraft.block;

import xelitez.frostcraft.entity.EntityFrostWing;
import xelitez.frostcraft.registry.CreativeTabs;
import xelitez.frostcraft.tileentity.TileEntityStatue;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockStatue extends BlockContainer{

	public BlockStatue(int par1, Material par2Material) {
		super(par1, par2Material);
		this.setCreativeTab(CreativeTabs.FCMiscItems);
	}

	@Override
	public TileEntity createNewTileEntity(World world) 
	{
		TileEntityStatue statue = new TileEntityStatue();
		return statue;
	}
	
    public boolean isOpaqueCube()
    {
        return false;
    }
    
    public boolean renderAsNormalBlock()
    {
        return false;
    }
    
    public int getRenderType()
    {
        return 22;
    }
    
    /**
     * Called when the block is placed in the world.
     */
    public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLivingBase par5EntityLivingBase, ItemStack par6ItemStack)
    {
        int var6 = MathHelper.floor_double((double)(par5EntityLivingBase.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
    	if(par1World != null)
    	{
    		par1World.setBlockMetadataWithNotify(par2, par3, par4, var6, 3);
    	}
    }
    
    public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4)
    {
        return par3 >= 254 ? false : super.canPlaceBlockAt(par1World, par2, par3, par4) && super.canPlaceBlockAt(par1World, par2, par3 + 1, par4) && super.canPlaceBlockAt(par1World, par2, par3 + 2, par4);
    }
    
    public int getMobilityFlag()
    {
        return 1;
    }
    
    public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5)
    {
        int i1 = par1World.getBlockMetadata(par2, par3, par4);
        if(i1 < 4)
        {
        	if(par1World.getBlockId(par2, par3 + 1, par4) != this.blockID || par1World.getBlockId(par2, par3 + 2, par4) != this.blockID )
        	{
                par1World.setBlockToAir(par2, par3, par4);
        	}
        }
        else if(i1 == 4)
        {
        	if(par1World.getBlockId(par2, par3 - 1, par4) != this.blockID || par1World.getBlockId(par2, par3 + 1, par4) != this.blockID)
        	{
                par1World.setBlockToAir(par2, par3, par4);
        	}
        }
        else if(i1 > 4)
        {
        	if(par1World.getBlockId(par2, par3 - 1, par4) != this.blockID || par1World.getBlockId(par2, par3 - 2, par4) != this.blockID)
        	{
                par1World.setBlockToAir(par2, par3, par4);
        	}
        }
    }
    
    public void onBlockAdded(World par1World, int par2, int par3, int par4)
    {
        super.onBlockAdded(par1World, par2, par3, par4);
        if(par1World.getBlockMetadata(par2, par3, par4) < 4)
        {
	        par1World.setBlock(par2, par3 + 1, par4, this.blockID, 4, 2);
	        par1World.setBlock(par2, par3 + 2, par4, this.blockID, 5, 2);
        }
    }
    
    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
    {
        if (par1World.isRemote)
        {
            return true;
        }
        if(par5EntityPlayer.isSneaking() || par1World.getBlockMetadata(par2, par3, par4) > 3)
        {
        	return false;
        }
        else
        {
        	if(par5EntityPlayer.getCurrentEquippedItem().itemID == Item.diamond.itemID)
        	{
        		boolean flag = false;
        		for(Object obj : par1World.loadedEntityList)
        		{
        			if(obj instanceof EntityFrostWing && ((Entity)obj).isEntityAlive())
        			{
        				System.out.println(((Entity)obj).posX);
        				flag = true;
        			}
        		}
        		if(flag)
        		{
        			par5EntityPlayer.addChatMessage("<Statue> There can only be one Frost Wing and it is already being fought somewhere.");
        		}
        		else
        		{
        			EntityFrostWing entity = new EntityFrostWing(par1World);
        			int x = par2;
        			int z = par4;
        			int y = par3 + 1;
        			if(par1World.getBlockMetadata(par2, par3, par4) == 0)
        			{
        				z--;
        			}
        			if(par1World.getBlockMetadata(par2, par3, par4) == 1)
        			{
        				x++;
        			}
        			if(par1World.getBlockMetadata(par2, par3, par4) == 2)
        			{
        				z++;
        			}
        			if(par1World.getBlockMetadata(par2, par3, par4) == 3)
        			{
        				x--;
        			}
        			entity.setPosition(x, y, z);
        			par1World.spawnEntityInWorld(entity);
        		}
        	}
        	return true;
        }
    }
    
}
