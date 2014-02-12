package com.pinion.bingfetcher.pageget;

import java.io.IOException;
import java.net.Proxy;
import java.net.URL;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class PageGetManager
{
	private static final List<Proxy> proxyList;
	private static final Integer numProxies;
	private static final Random rand;
	private static final ExecutorService executor;
	private static final Integer NumAttemps = 3;
	private static final Integer ThreadPool = 1000;
	/* Thread Timeout = 2 mins */
	
	static 
	{
		 proxyList = ProxyList.getProxyList();
		 numProxies = proxyList.size();
		 rand = new Random();
		 executor = Executors.newFixedThreadPool(ThreadPool);
	}
	
	protected void finalize() throws Throwable 
	{
		executor.shutdown();
	    super.finalize();
	}
	
	/*
	 * Returns null if unsuccessful
	 */
	public static String getPage(URL url)
	{
		String result = null;
		for(int i=0; i<NumAttemps; i++)
		{
			try
			{
				Callable<String> callable = new PageGetter(getRandProxy(),url);
				Future<String> future = executor.submit(callable);
				result = future.get(1, TimeUnit.MINUTES);
				if(result != null) break;
				
			} catch (IOException | InterruptedException | ExecutionException | TimeoutException e)
			{		
				System.err.println("Exception "+e.toString()+" for URL:"+url);
			}
		}
		if( result != null)
			return result;
		else
			return null;
	}

	
	private static Proxy getRandProxy()
	{
		Proxy randProxy;
		
		/* Take care if proxyList is empty */
		try
		{
			randProxy= proxyList.get(rand.nextInt(numProxies));
		}
		catch(IllegalArgumentException e)
		{
			randProxy=Proxy.NO_PROXY;
		}
		return randProxy;
	}

}
