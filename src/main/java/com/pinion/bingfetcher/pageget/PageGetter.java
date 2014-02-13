package com.pinion.bingfetcher.pageget;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.util.concurrent.Callable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class PageGetter implements Callable<String>
{
	private static Log logger = LogFactory.getLog(PageGetter.class.getName());

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
			logger.warn("Proxy: "+proxy+" failed for URL: "+url);
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
			logger.warn("Proxy: "+proxy+" failed for URL: "+url);
			return null;
		}
		
		return page.toString();
		
	}

}
