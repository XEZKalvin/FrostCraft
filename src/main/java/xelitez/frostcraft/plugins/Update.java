package xelitez.frostcraft.plugins;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import xelitez.frostcraft.Frostcraft;
import xelitez.frostcraft.Version;
import xelitez.frostcraft.registry.Settings;
import xelitez.updateutility.XEZLog;
import xelitez.updateutility.XEZUpdateBase;
import xelitez.updateutility.twitter.TwitterInstance;

public class Update extends XEZUpdateBase
{

	@Override
	public String getCurrentVersion() 
	{
		return Version.getVersion() + " for " + Version.MC;
	}

	@Override
	public String getNewVersion() 
	{
		return Version.newVersion;
	}

	@Override
	public void checkForUpdates() 
	{
		Frostcraft.instance.version.checkForUpdates();
	}

	@Override
	public boolean doesModCheckForUpdates() 
	{
		return Settings.checkForUpdates;
	}

	@Override
	public boolean isUpdateAvailable() 
	{
		return Version.available;
	}

	@Override
	public String getModIcon() 
	{
		return "frostcraft:textures/xezmods.png";
	}

	@Override
	public String getUpdateUrl() 
	{
		return "http://minecraft.curseforge.com/mc-mods/221210";
	}

	@Override
	public String getDownloadUrl() 
	{
		List<String> strings = new ArrayList<String>();
		
		try
		{
			URL url = new URL("https://raw.githubusercontent.com/XEZKalvin/UpdateUtility/master/updateURLstorage.txt");
			URLConnection connect = url.openConnection();
			connect.setConnectTimeout(5000);
			connect.setReadTimeout(5000);
			BufferedReader in = new BufferedReader(new InputStreamReader(connect.getInputStream()));
			String str;
			
			while ((str = in.readLine()) != null)
			{
				strings.add(str);
			}
			
			in.close();
		}
		catch(Exception E)
		{
			XEZLog.severe("Unable to obtain download URL");
		}
		for (int i = 0; i < strings.size(); i++)
		{
			String line = "";
			
			if (strings.get(i) != null)
			{
				line = (String)strings.get(i);
			}
			if(line.contains("<frostcraft>"))
			{
				return line.substring(line.indexOf("<frostcraft>") + 12, line.indexOf("</frostcraft>"));
			}
		}
		return null;
	}

	@Override
	public String[] stringsToDelete() 
	{
		return new String[] {"Frostcraft"};
	}
	
	public static TwitterInstance TwitterHandler = new TwitterInstance(0, "#Frostcraft", "KalvinFrosted").addUserID(415813796);
	
	public TwitterInstance getTInstance()
	{
		return TwitterHandler;
	}

}
