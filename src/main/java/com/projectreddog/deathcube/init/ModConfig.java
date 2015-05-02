package com.projectreddog.deathcube.init;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;

import com.projectreddog.deathcube.utility.ConfigReturnValues;
import com.projectreddog.deathcube.utility.Log;

public class ModConfig {

	private static File configFile;

	private static FileOutputStream fos = null;
	private static DataOutputStream dos = null;

	private static FileInputStream fis = null;
	private static DataInputStream dis = null;

	public static void init() {
		if (!MinecraftServer.getServer().getEntityWorld().isRemote) {
			/**
			 * Get list of files from root.
			 * - Search for "world" and "saves"
			 * - World means Server
			 * - Saves means Local
			 */
			String path = "";
			File folder = new File(".");
			File[] listOfFiles = folder.listFiles();
			Log.info("Root path:" + folder.getPath());

			for (int i = 0; i < listOfFiles.length; i++) {
				if (listOfFiles[i].isDirectory()) {
					if (listOfFiles[i].getName().equals("saves")) {
						path = "saves\\" + MinecraftServer.getServer().getWorldName();

						Log.info("Found saves dir.");
					} else if (listOfFiles[i].getName().equals("world")) {
						path = "world";

						Log.info("Assuming a server, root contains world.");
					}
				}
			}

			Log.info(path);
			configFile = new File(path + "\\deathcube.config");
			Log.info(configFile.toString());
		}
	}

	public static void updateConfig(BlockPos lobbyBlockPos) {

		String readMapName = "";
		ConfigReturnValues readValues;
		/**
		 * Only saving Lobby position for now.
		 * - Wipe config file clean
		 * - Write lobby position as int's xyz
		 */
		try {
			if (configFile.exists()) {
				readValues = readConfig();
				if (readValues != null) {
					readMapName = readValues.mapName;
					Log.info("Found map name in config.  Stored for Re-write.");
				}
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
				dos.writeUTF(readMapName);
				dos.writeInt(lobbyBlockPos.getX());
				dos.writeInt(lobbyBlockPos.getY());
				dos.writeInt(lobbyBlockPos.getZ());

				Log.info("Successful lobby pos write.");

				dos.close();
				fos.close();
			} else {
				Log.warn("Config file could not be opened.");
			}

		} catch (FileNotFoundException e) {
			// e.printStackTrace();
			Log.warn("Write Config - File not Found.");
		} catch (IOException e) {
			// e.printStackTrace();
			Log.warn("Write Config - File could not close.");
		}
	}

	public static void updateConfig(String mapName) {

		BlockPos readLobbyPos = new BlockPos(0, 0, 0);
		ConfigReturnValues readValues;
		/**
		 * Only saving Lobby position for now.
		 * - Wipe config file clean
		 * - Write lobby position as int's xyz
		 */
		try {
			if (configFile.exists()) {
				readValues = readConfig();
				if (readValues != null) {
					readLobbyPos = readValues.lobbyPos;
					Log.info("Found lobby pos in config.  Stored for Re-write.");
				}
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
				dos.writeUTF(mapName);
				dos.writeInt(readLobbyPos.getX());
				dos.writeInt(readLobbyPos.getY());
				dos.writeInt(readLobbyPos.getZ());

				Log.info("Successful map name write.");

				dos.close();
				fos.close();
			} else {
				Log.warn("Config file could not be opened.");
			}

		} catch (FileNotFoundException e) {
			// e.printStackTrace();
			Log.warn("Write Config - File not Found.");
		} catch (IOException e) {
			// e.printStackTrace();
			Log.warn("Write Config - File could not close.");
		}
	}

	public static ConfigReturnValues readConfig() {

		int x = 0, y = 0, z = 0;
		String mapName = "";
		BlockPos lobbyPos = new BlockPos(x, y, z);

		if (configFile.exists() && configFile.canRead()) {
			try {
				fis = new FileInputStream(configFile);
				if (fos != null) {
					dis = new DataInputStream(fis);
					/**
					 * Read Lobby Position
					 */
					mapName = dis.readUTF();
					x = dis.readInt();
					y = dis.readInt();
					z = dis.readInt();

					if (x != 0 && y != 0 && z != 0) {
						lobbyPos = new BlockPos(x, y, z);
					} else
						Log.info("Lobby Position from config - 0, 0, 0.");

					Log.info("Successful config read.");

					dis.close();
					fis.close();
				} else {
					Log.warn("Config file could not be opened.");
				}

			} catch (FileNotFoundException e) {
				// e.printStackTrace();
				Log.warn("Read Config - File not Found.");
			} catch (IOException e) {
				// e.printStackTrace();
				Log.warn("Read Config - File could not close.");
			}
		} else
			Log.warn("Config file does not exist or cannot be read.");

		return new ConfigReturnValues(mapName, lobbyPos);
	}
}
