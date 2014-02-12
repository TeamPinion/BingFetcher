package com.pinion.bingfetcher.pageget;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.util.concurrent.Callable;

public class PageGetter implements Callable<String>
{
	public PageGetter(Proxy proxy, URL url) throws IOException
	{
		super();
		this.proxy = proxy;
		this.url = url;
		this.uc = (HttpURLConnection)url.openConnection(proxy);
	}
	
	protected void finalize() throws Throwable 
	{
		uc.disconnect();
	    super.finalize();
	}
	
	HttpURLConnection uc;
	Proxy proxy;
	URL url;
	
	public String call()
	{
		StringBuilder page = new StringBuilder();
		String line;
		BufferedReader in;
		try
		{
			in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
		} catch (IOException e)
		{
			System.err.println("Proxy: "+proxy+" failed for URL: "+url);
			return null;
		}
		try
		{
			while ((line = in.readLine()) != null){
			   page.append(line + "\n");
			}
		} 
		catch (IOException e)
		{
			System.err.println("Proxy: "+proxy+" failed for URL: "+url);
			return null;
		}
		
		return page.toString();
		
	}

}
