package xelitez.frostcraft.network;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import org.apache.logging.log4j.Level;

import xelitez.frostcraft.FCLog;
import xelitez.frostcraft.entity.EntityFrostArrow;
import xelitez.frostcraft.tileentity.TileEntityThermalMachines;

import com.google.common.io.ByteArrayDataInput;


public class PacketManagerClient
{
	public static PacketManagerClient INSTANCE = new PacketManagerClient();
	
	public void onPacketData(ByteArrayDataInput dat, short ID, EntityPlayer player) 
	{
        if(ID != 0)
        {
            EntityPlayer thePlayer = (EntityPlayer)player;
            World world = thePlayer.worldObj;
            switch(ID)
            {
            case 1:
                int coords[]  = new int[3];

                for (int var1 = 0; var1 < 3; var1++)
                {
                    coords[var1] = dat.readInt();
                }
                int dimension = dat.readInt();
                if(world.provider.dimensionId == dimension)
                {
                	TileEntity te = world.getTileEntity(coords[0], coords[1], coords[2]);
                	if(te != null && te instanceof TileEntityThermalMachines)
                	{
                		TileEntityThermalMachines tet = (TileEntityThermalMachines)te;
                		tet.front = dat.readInt();
                		tet.isActive = dat.readBoolean();
                		tet.setHasData();
                	}
                	world.markBlockForUpdate(coords[0], coords[1], coords[2]);
                	return;
                }
            case 2:
            	Entity entity = world.getEntityByID(dat.readInt());
            	if(entity != null)
            	{
	            	entity.posX = dat.readDouble();
	            	entity.posY = dat.readDouble();
	            	entity.posZ = dat.readDouble();
	            	entity.prevPosX = dat.readDouble();
	            	entity.prevPosY = dat.readDouble();
	            	entity.prevPosZ = dat.readDouble();
	            	entity.motionX = dat.readDouble();
	            	entity.motionY = dat.readDouble();
	            	entity.motionZ = dat.readDouble();
	            	entity.rotationYaw = dat.readFloat();
	            	entity.rotationPitch = dat.readFloat();
	            	entity.prevRotationYaw = dat.readFloat();
	            	entity.prevRotationPitch = dat.readFloat();
	            	entity.setPositionAndRotation(entity.posX, entity.posY, entity.posZ, entity.rotationYaw, entity.rotationPitch);
            	}
            	return;
            case 3:
            	EntityFrostArrow arrow = (EntityFrostArrow) world.getEntityByID(dat.readInt());
            	if(arrow != null)
            	{
            		arrow.canFreeze = dat.readBoolean();
            	}
            default:
            	return;
            }
        }
        else
        {
        	FCLog.log(Level.INFO, "Frostcraft packet recieved with invalid id");
        }
	}

}
