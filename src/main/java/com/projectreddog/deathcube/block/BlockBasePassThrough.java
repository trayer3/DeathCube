package com.projectreddog.deathcube.block;

import java.util.Random;

import com.projectreddog.deathcube.creativetab.CreativeTabDeathCube;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class BlockBasePassThrough extends BlockDeathCube
{

    protected BlockBasePassThrough(Material materialIn)
    {
        super(materialIn);
        this.setCreativeTab(CreativeTabDeathCube.DEATHCUBE_TAB);
    }
    
    protected BlockBasePassThrough()
    {
        super(Material.rock);
    }

    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos)
    {
    	boolean flag = true; //this.getRedstoneStrength(worldIn.getBlockState(pos)) > 0;
        float x_z_border = 0.0625F;
        float y_state1 = 0.5F; //0.03125F;
        float y_state2 = 0.0625F;
        

        if (flag)
        {
            this.setBlockBounds(x_z_border, 0.0F, 0.f, 1.0F - x_z_border, y_state1, 1.0F - x_z_border);
        }
        else
        {
            this.setBlockBounds(x_z_border, 0.0F, 0.f, 1.0F - x_z_border, y_state2, 1.0F - x_z_border);
        }	
    }

    /**
     * How many world ticks before ticking
     */
    public int tickRate(World worldIn)
    {
        return 20;
    }

    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state)
    {
        return null;
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    public boolean isFullCube()
    {
        return false;
    }

    public boolean isPassable(IBlockAccess worldIn, BlockPos pos)
    {
        return true;
    }

    /**
     * Called When an Entity Collided with the Block
     */
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn)
    {
        if (!worldIn.isRemote)
        {
            //entityIn.addVelocity(0.0d, 1.0d, 0.0d);
        }
    }

    /**
     * Returns the cubic AABB inset by 1/8 on all sides
     */
    protected AxisAlignedBB getSensitiveAABB(BlockPos pos)
    {
        float f = 0.125F;
        return new AxisAlignedBB((double)((float)pos.getX() + 0.125F), (double)pos.getY(), (double)((float)pos.getZ() + 0.125F), (double)((float)(pos.getX() + 1) - 0.125F), (double)pos.getY() + 0.25D, (double)((float)(pos.getZ() + 1) - 0.125F));
    }

    /**
     * Sets the block's bounds for rendering it as an item
     */
    public void setBlockBoundsForItemRender()
    {
        float f = 0.5F;
        float f1 = 0.125F;
        float f2 = 0.5F;
        this.setBlockBounds(0.0F, 0.375F, 0.0F, 1.0F, 0.625F, 1.0F);
    }
}
