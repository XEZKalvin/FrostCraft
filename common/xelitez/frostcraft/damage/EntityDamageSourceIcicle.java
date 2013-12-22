package xelitez.frostcraft.damage;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.EntityDamageSource;
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
    public ChatMessageComponent getDeathMessage(EntityLivingBase par1EntityLivingBase)
    {
        return ChatMessageComponent.createFromText(par1EntityLivingBase.getTranslatedEntityName() + " got killed by an icicle " + (this.indirectEntity != null ? this.indirectEntity instanceof EntityPlayer ? "from " + ((EntityPlayer)this.indirectEntity).username : "from " + StatCollector.translateToLocal(this.indirectEntity.getEntityName()) : ""));
    }
}
