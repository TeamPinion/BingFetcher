package com.pinion.bingfetcher.manager;

import java.io.IOException;
import java.net.URL;

import com.pinion.bingfetcher.pageget.PageGetManager;
import com.pinion.bingfetcher.parse.ParseManager;

public class FetchManager
{
	public static void main(String[] args) throws IOException
	{
		
		URL url = new URL("http://bing.com/search?q=Amazon+language:en&n=30");
		String webPage = "";
		int count = 0;
		int i=20;
		while(i--!=0)
		{
			webPage = PageGetManager.getPage(url);
			if(webPage != null)
				count++;	
			System.out.println(count);
			ParseManager.getLinks(webPage);
		}
		
		
		
		System.out.println("Done"+count);
		/* Use exit(0) other wise it doesnot exit */
		System.exit(0);
		return;
		// Need to check differences in returned values Sizes are different!

	}

}
