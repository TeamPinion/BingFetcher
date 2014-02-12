package com.pinion.bingfetcher.manager;

import java.io.IOException;
import java.net.URL;

import com.pinion.bingfetcher.pageget.PageGetManager;

public class FetchManager
{
	public static void main(String[] args) throws IOException
	{
		
		URL url = new URL("http://bing.com/search?q=Kejriwal");
		String webPage = "";
		int count = 0;
		int i=20;
		while(i--!=0)
		{
			webPage = PageGetManager.getPage(url);
			if(webPage != null)
				count++;	
			System.out.println(count);
		}
		
		
		
		System.out.println("Done"+count);
		/* Use exit(0) other wise it doesnot exit */
		System.exit(0);
		return;
		// Need to check differences in returned values Sizes are different!

	}

}
