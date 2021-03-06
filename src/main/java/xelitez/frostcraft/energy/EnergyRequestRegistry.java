package xelitez.frostcraft.energy;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.world.World;
import xelitez.frostcraft.tileentity.TileEntityThermalMachines;
import xelitez.frostcraft.tileentity.TileEntityThermalPipe;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

public class EnergyRequestRegistry
{
	private List<int[]> ids = new ArrayList<int[]>();
	private List<int[]> queue = new ArrayList<int[]>();
	private List<int[]> checked = new ArrayList<int[]>();
	
	public int checkSpeed = 5;
	
	private static EnergyRequestRegistry INSTANCE = new EnergyRequestRegistry();
	
	public static EnergyRequestRegistry getInstance()
	{
		return INSTANCE;
	}

	public int getAvailableRequestId()
	{
		return getAvailableId(0);
	}
	
	private int getAvailableId(int par1)
	{
		if(par1 >= Integer.MAX_VALUE)
		{
			return -1;
		}
		for(int[] id : ids)
		{
			if(id != null && id[0] == par1)
			{
				par1++;
				return getAvailableId(par1);
			}
		}	
		return par1;
	}
	
	public int addRequest(TileEntityThermalMachines te, int request)
	{
		int id = this.getAvailableRequestId();
		if(id != -1)
		{
			int[] set = new int[] {id, request, te.xCoord, te.yCoord, te.zCoord, te.getWorldObj().provider.dimensionId};
			ids.add(set);
		}
		return id;
	}
	
	public int[] getRequestData(int id)
	{
		for(int[] dat : ids)
		{
			if(dat != null && dat[0] == id)
			{
				return dat;
			}
		}
		return null;
	}
	
	public boolean hasTileEntityRequests(TileEntityThermalMachines te)
	{
		for(int[] id : ids)
		{
			if(id != null && id[2] == te.xCoord && id[3] == te.yCoord && id[4] == te.zCoord && id[5] == te.getWorldObj().provider.dimensionId)
			{
				if(this.getNumberOfPipesInQueue(id[0]) <= 0)
				{
					this.removeAll(id[0]);
					return false;
				}
				return true;
			}
		}
		return false;
	}
	
	public void setPipeChecked(TileEntityThermalPipe te, int id)
	{
		int[] set = new int[] {id, te.xCoord, te.yCoord, te.zCoord, te.getWorldObj().provider.dimensionId };
		checked.add(set);
	}
	
	public boolean isPipeChecked(TileEntityThermalPipe te, int id)
	{
		for(int[] check : checked)
		{
			if(check != null && check[0] == id)
			{
				if(te.xCoord == check[1] && te.yCoord == check[2] && te.zCoord == check[3] && te.getWorldObj().provider.dimensionId == check[4])
				{
					return true;
				}
			}
		}
		return false;
	}
	
	private int getDimensionQueue(int dim)
	{
		int count = 0;
		for(int[] dat : queue)
		{
			if(dat != null && dat[4] == dim)
			{
				count++;
			}
		}
		return count;
	}
	
	public boolean addPipeToCheckQueue(int id, TileEntityThermalPipe te)
	{
		if(te != null && te instanceof TileEntityThermalPipe && !this.getIsPipeInQueue(id, te))
		{
			int[] set = new int[] {id, te.xCoord, te.yCoord, te.zCoord, te.getWorldObj().provider.dimensionId};
			queue.add(set);
			return true;
		}
		return false;
	}
	
	public void removePipeFromQueue(int x, int y, int z, int dim, int id)
	{
		for(int i = 0;i < queue.size();i++)
		{
			if(i < queue.size())
			{
				int[] dat = queue.get(i);
				if(dat != null && dat[0] == id && dat[1] == x && dat[2] == y && dat[3] == z && dat[4] == dim)
				{
					queue.remove(i);
					return;
				}
			}
		}
	}
	
	public void removePipeFromQueue(int x, int y, int z, int dim)
	{
		for(int i = 0;i < queue.size();i++)
		{
			if(i < queue.size())
			{
				int[] dat = queue.get(i);
				if(dat != null && dat[1] == x && dat[2] == y && dat[3] == z && dat[4] == dim)
				{
					this.removePipeFromQueue(x, y, z, dim, dat[0]);
				}
			}
		}
	}
	
	public boolean getIsPipeInQueue(int id, TileEntityThermalPipe te)
	{
		for(int i = 0;i < queue.size();i++)
		{
			if(i < queue.size())
			{
				int[] dat = queue.get(i);
				if(dat != null && dat[0] == id && dat[1] == te.xCoord && dat[2] == te.yCoord && dat[3] == te.zCoord && dat[4] == te.getWorldObj().provider.dimensionId)
				{
					return true;
				}
			}
		}
		return false;
	}
	
	public int getNumberOfPipesInQueue(int id)
	{
		int count = 0;
		for(int[] dat : queue)
		{
			if(dat != null && dat[0] == id)
			{
				count++;
			}
		}
		return count;
	}
	
	public void removeAll(int id)
	{
		this.removeQueue(id);
		this.removeChecked(id);
		this.removeRequest(id);
	}
	
	public void removeQueue(int id)
	{
		for(int i = 0;i < queue.size();i++)
		{
			if(i < queue.size())
			{
				int[] dat = queue.get(i);
				if(dat != null && dat[0] == id)
				{
					queue.remove(i);
					this.removeQueue(id);
					return;
				}
			}
		}
	}
	
	public void removeRequest(int id)
	{
		for(int i = 0;i < ids.size();i++)
		{
			if(i < ids.size())
			{
				int[] dat = ids.get(i);
				if(dat != null && dat[0] == id)
				{
					ids.remove(i);
					return;
				}
			}
		}
	}
	
	public void removeChecked(int id)
	{
		for(int i = 0;i < checked.size();i++)
		{
			if(i < checked.size())
			{
				int[] dat = checked.get(i);
				if(dat != null && dat[0] == id)
				{
					checked.remove(i);
					this.removeChecked(id);
					return;
				}
			}
		}
	}

	@SubscribeEvent
	public void onTick(TickEvent.WorldTickEvent evt) 
	{		
		if(evt.side.isServer() && evt.phase == TickEvent.Phase.END)
		{
			World world = evt.world;
			while(INSTANCE.getDimensionQueue(world.provider.dimensionId) > 0)
			{
				for(int i = 0;i < INSTANCE.queue.size();i++)
				{
					if(i < queue.size())
					{
						int[] dat = (int[])INSTANCE.queue.get(i);
						if(dat != null && dat[4] == world.provider.dimensionId)
						{
							TileEntityThermalPipe te = (TileEntityThermalPipe)world.getTileEntity(dat[1], dat[2], dat[3]);
							if(te != null)
							{
								INSTANCE.setPipeChecked(te, dat[0]);
								INSTANCE.queue.remove(i);
								te.check(dat[0]);
							}
							else
							{
								INSTANCE.queue.remove(i);
							}
							break;
						}
					}
				}
			}
		}
	}
	
}
