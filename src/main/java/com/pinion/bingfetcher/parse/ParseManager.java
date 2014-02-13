package com.pinion.bingfetcher.parse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ParseManager
{
	private static Log logger = LogFactory.getLog(ParseManager.class.getName());
	
	public static void getLinks(String html)
	{
		if(html == null) return;
		Document doc = Jsoup.parse(html);
		int results = 0;
		
		Elements links = doc.getElementsByTag("a");
		for (Element link : links) 
		{
			
			  String linkHref = link.attr("href");
			  String linkText = link.text();
			  
			  if(select(linkText,linkHref))
			  {
				  if(linkHref.contains("freebase.com")) break;
				  System.out.println("Link:"+linkHref);
				  System.out.println("Text:"+linkText);
				  results++;
			  }
			  
		}
		System.out.println("Total Results:"+results);
		
	}
	
	private static Boolean select(String linkText, String linkHref)
	{
		if(!linkHref.isEmpty() && !linkText.isEmpty() && !linkHref.startsWith("/") 
				&& !linkHref.contains("#") && !linkHref.contains("bingads") && !linkHref.contains("=") 
				&& linkHref.contains("http") && isEnglish(linkText))
		   return true;
		else 
			return false;
	}
	
	private static Boolean isEnglish(String linkText)
	{
		char[] charArray = linkText.toCharArray();
		int length = linkText.length();
		int nonEnglish = 0;
		for(int i=0; i<length; i++)
		{
			if(charArray[i] > 128) nonEnglish++;
		}
		if(nonEnglish > length/2) 
		{
			logger.info("NON ENGLISH DETECTED "+linkText);
			return false;
		}
		return true;
	}

}





