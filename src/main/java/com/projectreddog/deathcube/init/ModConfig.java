package com.projectreddog.deathcube.init;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import net.minecraft.util.BlockPos;

import com.projectreddog.deathcube.reference.Reference;
import com.projectreddog.deathcube.utility.Log;

public class ModConfig {

	private static File configFile;

	private static FileOutputStream fos = null;
	private static DataOutputStream dos = null;

	private static FileInputStream fis = null;
	private static DataInputStream dis = null;

	public static void init() {
		/**
		 * Get list of files from root. 
		 * - Search for "world" and "saves"
		 * - World means Server
		 * - Saves means Local
		 */
		String dir;
		File folder = new File(".");
		File[] listOfFiles = folder.listFiles();
		Log.info("Root path:"+ folder.getPath());

		for (int i = 0; i < listOfFiles.length; i++) 
		{
			if (listOfFiles[i].isDirectory()) 
			{
				if(listOfFiles[i].getName().equals("saves")) {
					dir = listOfFiles[i].getName();
					
					Log.info("Found saves dir.");
				} else if(listOfFiles[i].getName().equals("World")) {
					dir = listOfFiles[i].getName();
					
					Log.info("Found World dir.");
				}
			}
		}
		
		Log.info(Reference.CONFIG_FILE_PATH);
		configFile = new File(Reference.CONFIG_FILE_PATH + "\\deathcube.config");
		Log.info(configFile.toString());
	}

	public static void updateConfig(BlockPos pos) {
		/**
		 * Only saving Lobby position for now.
		 * - Wipe config file clean
		 * - Write lobby position as int's xyz
		 */
		try {
			if (configFile.exists()) {
				configFile.delete();
				configFile.createNewFile();
			} else
				configFile.createNewFile();

			fos = new FileOutputStream(configFile);
			if (fos != null) {
				dos = new DataOutputStream(fos);
				/**
				 * Write Lobby Position
				 */
				dos.writeInt(pos.getX());
				dos.writeInt(pos.getY());
				dos.writeInt(pos.getZ());

				dos.close();
				fos.close();
			} else {
				Log.warn("Config file could not be opened.");
			}

		} catch (FileNotFoundException e) {
			//e.printStackTrace();
			Log.warn("Write Config - File not Found.");
		} catch (IOException e) {
			//e.printStackTrace();
			Log.warn("Write Config - File could not close.");
		}
	}

	public static BlockPos readConfig() {
		int x = 0, y = 0, z = 0;
		BlockPos lobbyPos = null;

		if (configFile.exists() && configFile.canRead()) {
			try {
				fis = new FileInputStream(configFile);
				if (fos != null) {
					dis = new DataInputStream(fis);
					/**
					 * Read Lobby Position
					 */
					x = dis.readInt();
					y = dis.readInt();
					z = dis.readInt();

					if (x != 0 && y != 0 && z != 0) {
						lobbyPos = new BlockPos(x, y, z);
					}

					dis.close();
					fis.close();
				} else {
					Log.warn("Config file could not be opened.");
				}

			} catch (FileNotFoundException e) {
				//e.printStackTrace();
				Log.warn("Read Config - File not Found.");
			} catch (IOException e) {
				//e.printStackTrace();
				Log.warn("Read Config - File could not close.");
			}
		} else
			Log.warn("Config file does not exist or cannot be read.");

		return lobbyPos;
	}
}
