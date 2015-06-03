package com.projectreddog.deathcube.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings;

import com.projectreddog.deathcube.DeathCube;
import com.projectreddog.deathcube.entity.EntityWaypoint;
import com.projectreddog.deathcube.reference.Reference;
import com.projectreddog.deathcube.tileentity.TileEntityCapturePoint;
import com.projectreddog.deathcube.tileentity.TileEntityStartingGearConfig;
import com.projectreddog.deathcube.utility.Log;

/**
 * Original DeathCube used this Class for:
 * - Spawn & DeathPenalty Locations
 * - Base Force Field
 * - Chat and Armor Color
 * - List of capture points, active point needed
 * - Team player count
 */

public class GameTeam {

	private List<EntityPlayer> playerList = new ArrayList<EntityPlayer>();
	private List<BlockPos> spawnPointsList = new ArrayList<BlockPos>();
	private List<BlockPos> capturePointsList = new ArrayList<BlockPos>();
	private List<BlockPos> pointsCapturedList = new ArrayList<BlockPos>();

	private String teamColor;
	private World worldObj;
	private int teamHandicap = 0;
	private int currentCaptureIndex = 0;
	private int nextCaptureIndex = 0;
	
	private EntityWaypoint waypoint;
	private boolean needsSpawned = true;

	public GameTeam(String teamColor, World inWorldObj) {
		this.teamColor = teamColor;
		this.worldObj = inWorldObj;
		this.waypoint = new EntityWaypoint(inWorldObj,teamColor);
	}

	public String getTeamColor() {
		return teamColor;
	}

	public int getTeamSize() {
		return playerList.size();
	}

	/**
	 * If teams are uneven, enhance smaller team's equipment?
	 * - Return to normal if teams are no longer uneven.
	 */
	public int getTeamHandicap() {
		return teamHandicap;
	}

	public void setTeamHandicap(int newHandicap) {
		this.teamHandicap = newHandicap;
	}

	/**
	 * Add a player to this team.
	 */
	public void addPlayer(EntityPlayer player) {
		playerList.add(player);
	}

	/**
	 * Remove a player from this team.
	 */
	public void removePlayer(EntityPlayer player) {
		if (playerList.contains(player)) {
			playerList.remove(player);
		}
	}

	/**
	 * Check if Player is on this Team. Used when a player comes into range of a Capture Point.
	 * - TODO: Check: Do EntityPlayers change over time? Will the current player equal the one originally added to the list?
	 */
	public boolean isPlayerOnTeam(EntityPlayer player) {
		if (playerList.contains(player)) {
			return true;
		} else {
			return false;
		}
	}
	
	public int getPlayerCount() {
		return playerList.size();
	}

	/**
	 * Just make these public static instead of these methods? Especially if there will ever be
	 * a need to clear and re-populate the list?
	 */
	public void addSpawnPoint(BlockPos spawnPoint) {
		this.spawnPointsList.add(spawnPoint);
	}

	public BlockPos getSpawnLocation() {
		int numSpawnPoints = spawnPointsList.size();
		if (numSpawnPoints > 1) {
			Log.info("Multiple spawn points found.");
			Random rand = new Random();
			return spawnPointsList.get(rand.nextInt(numSpawnPoints));
		} else {
			Log.info("One spawn point found.");
			return spawnPointsList.get(numSpawnPoints - 1);
		}
	}
	
	public int getNumSpawnPoints() {
		return spawnPointsList.size();
	}
	
	public void addCapturePoint(BlockPos capturePoint) {
		this.capturePointsList.add(capturePoint);
	}
	
	public int getNumCapturePoints() {
		return capturePointsList.size();
	}
	
	/**
	 * TODO:  Is there a better way to get the worldObj here?
	 */
	public void setNextCapturePointActive() {
		if(DeathCube.isOrderedCapture) {
			if(nextCaptureIndex < capturePointsList.size()) {
				Log.info("Setting a capture point as active for Team: " + teamColor);
				BlockPos capturePos = capturePointsList.get(nextCaptureIndex);
				TileEntityCapturePoint captureTE = (TileEntityCapturePoint) this.worldObj.getTileEntity(capturePos);
				captureTE.setIsActive(true);
				currentCaptureIndex = nextCaptureIndex;
				nextCaptureIndex++;
			}
		} else {
			/**
			 * Unordered captures allowed.
			 */
			setAllPointsActive(true);
		}
	}
	
	public int getCurrentCaptureIndex() {
		return currentCaptureIndex;
	}
	
	public BlockPos getCurrentPointPos() {
		if(capturePointsList != null && capturePointsList.size() != 0)
			return capturePointsList.get(currentCaptureIndex);
		else
			return null;
	}

	public boolean hasCapturedAllPoints() {
		boolean capturedAll = true;
		
		for(BlockPos pos : capturePointsList) {
			TileEntityCapturePoint captureTE = (TileEntityCapturePoint) this.worldObj.getTileEntity(pos);
			
			if(!captureTE.getIsCaptured()) {
				capturedAll = false;
			}
		}
		
		return capturedAll;
	}
	
	public void setAllPointsActive(boolean active) {
		for(BlockPos pos : capturePointsList) {
			TileEntityCapturePoint captureTE = (TileEntityCapturePoint) this.worldObj.getTileEntity(pos);
			captureTE.setIsActive(active);
			if(!active) {
				captureTE.setIsBeingCaptured(false);
				captureTE.setIsCaptured(false);
			}
		}
	}
	
	public void updateWaypoint() {
		if(getCurrentPointPos() != null) {
			double x = getCurrentPointPos().getX() + 0.5d;
			double y = getCurrentPointPos().getY() + 1.5d;
			double z = getCurrentPointPos().getZ() + 0.5d;
			
			waypoint.setPosition(x,y,z);
			
			if(needsSpawned) {
				boolean result = this.worldObj.spawnEntityInWorld(this.waypoint);
				if(result) {
					Log.info("Team " + teamColor + " Waypoint Spawned Successfully!");
				} else {
					Log.warn("Team " + teamColor + " Waypoint Failed to Spawn ...");
				}
				needsSpawned = false;
			}
		}
	}
	
	public void removeWaypoint() {
		waypoint.setDead();
	}
	
	public static void sendPlayerToTeamSpawn(EntityPlayer inPlayer) {
		/**
		 * Teleport Players to Team Spawn Location(s).
		 */
		preparePlayerToSpawn(inPlayer);
		givePlayerGear(inPlayer);
		Log.info("Player attributes set.  Gear given.");

		String teamColor = DeathCube.playerToTeamColor.get(inPlayer.getName());
		int teamIndex = DeathCube.teamColorToIndex.get(teamColor);
		BlockPos spawnLocation = DeathCube.gameTeams[teamIndex].getSpawnLocation();
		inPlayer.setPositionAndUpdate(spawnLocation.getX() + 0.5d, spawnLocation.getY() + 1, spawnLocation.getZ() + 0.5d);
	}

	public static void preparePlayerToSpawn(EntityPlayer inPlayer) {
		/**
		 * Prepare Player to Spawn in Game or Lobby:
		 * - Set velocity to zero to avoid death falling.
		 * - Clear any potion effects from Lobby time.
		 * - Set to full health and saturation to help hunger.
		 */
		inPlayer.setGameType(WorldSettings.GameType.SURVIVAL);
		inPlayer.motionX = 0;
		inPlayer.motionY = 0;
		inPlayer.motionZ = 0;
		inPlayer.fallDistance = 0;
		inPlayer.clearActivePotions();
		inPlayer.extinguish();
		inPlayer.setHealth(inPlayer.getMaxHealth());
		inPlayer.addPotionEffect(new PotionEffect(Potion.saturation.getId(), 10));
	}

	public static void givePlayerGear(EntityPlayer inPlayer) {
		/**
		 * Give Player Gear
		 * - Clear inventory. Happens automatically with assignment of new inventory?
		 * - Stone Sword
		 * - Leather Armor (in team color)
		 * - Bow
		 * - Arrows
		 * - Splash Potion of Poison
		 * 
		 **** --This should be customizable. Use TE with inventory. Copy inventory to player on respawn.
		 */
		Log.info("Give Gear - Start");
		World world = MinecraftServer.getServer().worldServers[0];
		String playerInventoryClass = Reference.GEAR_CLASS_WARRIOR;  // Get from player somehow.
		
		for (BlockPos pointPos : DeathCube.gearTEPos) {
			TileEntityStartingGearConfig lookupTE = (TileEntityStartingGearConfig) world.getTileEntity(pointPos);
			Log.info("Gear TE Location - " + pointPos.toString());
			if (lookupTE.getInventoryClass().equals(playerInventoryClass)) {
				ItemStack[] configInvClone = lookupTE.getInventory();
				
				if(configInvClone != null) {
					for(int i = 0; i < 4; i++) {
						/**
						 * Color Armor if Leather
						 */
						int color = 0;
						String teamColor = DeathCube.playerToTeamColor.get(inPlayer.getName());
						
						if(teamColor.equals(Reference.TEAM_RED)) {
							color = ItemDye.dyeColors[EnumDyeColor.RED.getDyeDamage()];
						} else if(teamColor.equals(Reference.TEAM_BLUE)) {
							color = ItemDye.dyeColors[EnumDyeColor.BLUE.getDyeDamage()];
						} else if(teamColor.equals(Reference.TEAM_GREEN)) {
							color = ItemDye.dyeColors[EnumDyeColor.GREEN.getDyeDamage()];
						} else if(teamColor.equals(Reference.TEAM_YELLOW)) {
							color = ItemDye.dyeColors[EnumDyeColor.YELLOW.getDyeDamage()];
						}
						
						ItemStack armorPiece = null;
						
						if(configInvClone[Reference.GEAR_INVENTORY_SIZE - 1 - i] != null) {
							armorPiece = configInvClone[Reference.GEAR_INVENTORY_SIZE - 1 - i].copy();
						}
						
						if(armorPiece != null && armorPiece.getItem().equals(Items.leather_helmet)) {
							Items.leather_helmet.setColor(armorPiece, color);
						} else if(armorPiece != null && armorPiece.getItem().equals(Items.leather_chestplate)) {
							Items.leather_chestplate.setColor(armorPiece, color);
						} else if(armorPiece != null && armorPiece.getItem().equals(Items.leather_leggings)) {
							Items.leather_leggings.setColor(armorPiece, color);
						} else if(armorPiece != null && armorPiece.getItem().equals(Items.leather_boots)) {
							Items.leather_boots.setColor(armorPiece, color);
						}
						
						inPlayer.inventory.armorInventory[i] = armorPiece;
					}
					
					for(int i = 0; i < (Reference.GEAR_INVENTORY_SIZE - 4); i++) {
						if(configInvClone[i] != null) {
							inPlayer.inventory.mainInventory[i] = configInvClone[i].copy();
						} else {
							inPlayer.inventory.mainInventory[i] = null;
						}
					}
				}
			}
		}
		Log.info("Give Gear - Success");
	}
}
