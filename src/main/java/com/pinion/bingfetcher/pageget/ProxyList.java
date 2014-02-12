package com.pinion.bingfetcher.pageget;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import  java.util.concurrent.CancellationException;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

public class ProxyList
{
	private static final Integer ThreadPoolSize = 1000;
	
	/**
	 * This will read from proxies.txt and remove proxies which
	 * are not working. Please add large amount of proxies to this file
	 * 
	 * @return
	 * @throws InterruptedException
	 */
	public static List<Proxy> getProxyList() 
	{
		List<Proxy> proxyList = new ArrayList<Proxy>();

		try
		{
			Resource resource = new ClassPathResource("/proxies.txt");
			Properties props = PropertiesLoaderUtils.loadProperties(resource);
			for( Enumeration<?> e = props.propertyNames(); e.hasMoreElements();)
			{				
				String Address = (String)e.nextElement();
				String Port = props.getProperty(Address);
								
				proxyList.add(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(Address,Integer.parseInt(Port))));
			}
			
			/* Proxy Test */
			ExecutorService executor = Executors.newFixedThreadPool(ThreadPoolSize);
			Collection<Callable<Boolean>> tasks = new ArrayList<Callable<Boolean>>();
			for(Proxy p : proxyList)
			{
				tasks.add(new ProxyTest((InetSocketAddress)p.address()));
			}
			
			List<Future<Boolean>> results;
			try
			{
				results = executor.invokeAll(tasks, 10, TimeUnit.SECONDS);
			} catch (InterruptedException e1)
			{
				e1.printStackTrace();
				executor.shutdown();
				return new ArrayList<Proxy>();
			}
			
			executor.shutdown();
			
			List<Proxy> toBeRemoved = new ArrayList<Proxy>();
			
			for(int i=0; i<results.size(); i++)
			{
				try
				{
					Boolean b = results.get(i).get();
					if(!b) toBeRemoved.add(proxyList.get(i));
				} catch (ExecutionException | CancellationException | InterruptedException e)
				{
					toBeRemoved.add(proxyList.get(i));
				}
			}
			
			for(int i=0; i<toBeRemoved.size(); i++)
			{
				proxyList.removeAll(toBeRemoved);
			}
			
			writeProxies(proxyList,resource);
			return proxyList;
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		return proxyList;
	}
	
	private static void writeProxies(List<Proxy> proxyList, Resource resource) throws IOException
	{
		File file = resource.getFile();
		System.out.println("File Path:"+file.getAbsolutePath());
		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		
		for(Proxy p: proxyList)
		{
			InetSocketAddress iaddr = (InetSocketAddress) p.address();
			bw.write(iaddr.getHostString()+":"+iaddr.getPort()+'\n');
		}
		
		bw.close();
	}

}
