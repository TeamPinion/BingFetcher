package com.pinion.bingfetcher.pageget;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.Callable;

public class ProxyTest implements Callable<Boolean>
{
	InetSocketAddress address;
	
	public ProxyTest(InetSocketAddress address)
	{
		this.address = address;
	}
	
	@Override
	public Boolean call()
	{
		Socket socket = new Socket();
		try
		{
			socket.connect(address, 1000);
		} catch (Exception e)
		{
			try
			{
				socket.close();
			} catch (IOException e1)
			{
				return false;
			}
			return false;
		}
		try
		{
			socket.close();
		} catch (IOException e)
		{
			return false;
		}
		
		try
		{
			if( address.getAddress().isReachable(1000)) 
				return true;
			else
				return false;
		} catch (IOException e)
		{
			return false;
		}
	}

}
