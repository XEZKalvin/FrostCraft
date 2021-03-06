package xelitez.frostcraft.damage;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.StatCollector;

public class EntityDamageSourceIcicle extends EntityDamageSource
{
    private Entity indirectEntity;
    
    public EntityDamageSourceIcicle(Entity par2Entity, Entity par3Entity)
    {
        super("icicle", par2Entity);
        this.indirectEntity = par3Entity;
    }

    public Entity getSourceOfDamage()
    {
        return this.damageSourceEntity;
    }
    
    public Entity getEntity()
    {
        return this.indirectEntity;
    }

    /**
     * Returns the message to be displayed on player death.
     */
    @Override
    public IChatComponent func_151519_b(EntityLivingBase par1EntityLivingBase)
    {
        return new ChatComponentText(par1EntityLivingBase.getCommandSenderName() + " got killed by an icicle " + (this.indirectEntity != null ? this.indirectEntity instanceof EntityPlayer ? "from " + this.indirectEntity.getCommandSenderName() : "from " + StatCollector.translateToLocal(this.indirectEntity.getCommandSenderName()) : ""));
    }
}
