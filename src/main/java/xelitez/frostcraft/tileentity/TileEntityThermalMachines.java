package xelitez.frostcraft.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import xelitez.frostcraft.energy.EnergyRequestRegistry;
import xelitez.frostcraft.energy.IIsSource;
import xelitez.frostcraft.interfaces.IConnect;
import xelitez.frostcraft.network.PacketSendManagerServer;
import xelitez.frostcraft.registry.IdMap;

public class TileEntityThermalMachines extends TileEntity
{
	public boolean isActive = false;	
	public int front = 0;
	private boolean hasData = false;
	
	public int capacity = 100;
	public int storage = 0;
	int counter = 0;
	
    public void setWorldObj(World par1World)
    {
        this.worldObj = par1World;
        if(!this.worldObj.isRemote)
        {
    		PacketSendManagerServer.sendBlockData(this);
        }
    }
    
    public void setHasData()
    {
    	hasData = true;
    }
    
    public boolean getHasData()
    {
    	return hasData;
    }
	
    public void updateEntity() 
    {
    	counter++;
    	if(counter >= Integer.MAX_VALUE)
    	{
    		counter = 0;
    	}
    }
	
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
    	super.readFromNBT(par1NBTTagCompound);
    	front = par1NBTTagCompound.getInteger("front");
    	isActive = par1NBTTagCompound.getBoolean("isActive");
    	storage = par1NBTTagCompound.getInteger("storage");	
    }

    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
    	super.writeToNBT(par1NBTTagCompound);
    	par1NBTTagCompound.setInteger("front", front);
    	par1NBTTagCompound.setBoolean("isActive", isActive);
    	par1NBTTagCompound.setInteger("storage", storage);
    }
    
    public void setActive(boolean par1)
    {
    	this.isActive = par1;
    	PacketSendManagerServer.sendBlockData(this);
    }
    
    public int recieveEnergy(int value)
    {
    	int prevValue = this.storage;
		int temp = this.storage;
		temp += value;
		if(temp >= this.capacity)
		{
			temp = this.capacity;
		}
		this.storage = temp;
		return this.storage - prevValue;
    }
    
    public void requestEnergy(int value)
    {
    	if(!this.worldObj.isRemote)
    	{
	    	if(this.storage >= this.capacity || EnergyRequestRegistry.getInstance().hasTileEntityRequests(this))
	    	{
	    		return;
	    	}
	    	boolean checked = false;
	    	int id = -1;
	    	for(int i = 0;i < 6;i++)
	    	{
	    		int x1 = this.xCoord;
	    		int y1 = this.yCoord;
	    		int z1 = this.zCoord;
	    		switch(i)
	    		{
	    		case 0:
	    			y1 += 1;
	    			break;
	    		case 1:
	    			y1 -= 1;
	    			break;
	    		case 2:
	    			x1 -= 1;
	    			break;
	    		case 3:
	    			x1 += 1;
	    			break;
	    		case 4:
	    			z1 -= 1;
	    			break;
	    		case 5:
	    			z1 += 1;
	    			break;
	    		}
	    		if(this.worldObj.getBlock(x1, y1, z1) == IdMap.blockThermalPipe)
	    		{
	    			if(id == -1)
	    			{
	    				id = EnergyRequestRegistry.getInstance().addRequest(this, value);
	    			}
	    			if(id != -1)
	    			{
	    				EnergyRequestRegistry.getInstance().addPipeToCheckQueue(id, (TileEntityThermalPipe)this.worldObj.getTileEntity(x1, y1, z1));
	    				checked = true;
	    			}
	    		}
	    		if(worldObj.getBlock(x1, y1, z1) instanceof IConnect && worldObj.getTileEntity(x1, y1, z1) instanceof IIsSource)
	    		{
	    			if(id == -1)
	    			{
	    				id = EnergyRequestRegistry.getInstance().addRequest(this, value);
	    			}
	    			if(id != -1)
	    			{
	    				if(((IIsSource)worldObj.getTileEntity(x1, y1, z1)).handleRequest(id))
	    				{
	    					EnergyRequestRegistry.getInstance().removeAll(id);
	    				}
	    			}
	    		}
	    	}
	    	if(!checked && id != -1)
	    	{
	    		EnergyRequestRegistry.getInstance().removeAll(id);
	    	}
    	}
    }
    
}
