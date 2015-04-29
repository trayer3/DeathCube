package com.projectreddog.deathcube.entity;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntityWaypoint extends Entity {
	  
	
	public EntityWaypoint(World world)
	    {
	        super(world);	        
	        this.setSize(0.0F, 0.0F);
	        this.ignoreFrustumCheck = true;
	        this.onUpdate();
	    }
	
	
	
	
    /**
     * Checks if the entity is in range to render by using the past in distance and comparing it to its average edge
     * length * 64 * renderDistanceWeight Args: distance
     */
    public boolean isInRangeToRenderDist(double d)
    {
        return true;
    }




	@Override
	protected void entityInit() {
		
	}




	@Override
	protected void readEntityFromNBT(NBTTagCompound tagCompund) {
		
	}




	@Override
	protected void writeEntityToNBT(NBTTagCompound tagCompound) {
		
	}
}
