package xelitez.frostcraft;

import xelitez.frostcraft.world.WorldAccess;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderSurface;
import net.minecraftforge.event.Event;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.IEventListener;
import net.minecraftforge.event.world.WorldEvent;

public class EventListener implements IEventListener
{
	@Override
	@ForgeSubscribe
	public void invoke(Event event) 
	{
		if(event instanceof WorldEvent.Load)
		{
			World world = ((WorldEvent.Load)event).world;
			if(world.isRemote)
			{
				WorldAccess.instance().setWorldAndLoadRenderers((WorldClient)world);
			}
			if(world.provider instanceof WorldProviderSurface)
			{
				SaveHandler.getTagCompound(world, true);
			}
		}
		if(event instanceof WorldEvent.Save)
		{
			SaveHandler.saveTagCompound(((WorldEvent.Save)event).world);
		}	
	}

}
