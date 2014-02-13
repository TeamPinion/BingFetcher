package com.pinion.bingfetcher.parse;

import org.apache.log4j.Logger;
import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class ParseManager
{
	private static Logger logger = Logger.getLogger(ParseManager.class);
	
	public static void getLinks(String html)
	{
		if(html == null) return;
		Document doc = Jsoup.parse(html);
		Element content = doc.getElementById("content");

		int results = 0;
		
		Elements links = content.getElementsByTag("a");
		for (Element link : links) 
		{
			
			  String linkHref = link.attr("href");
			  String linkText = link.text();
			  
			  if(!linkHref.isEmpty() && !linkText.isEmpty() && !linkHref.startsWith("/") && !linkHref.contains("#") && !linkHref.contains("=") && linkHref.contains("http") && isEnglish(linkText))
			  {
				  if(linkHref.contains("freebase.com")) break;
				  System.out.println("Link:"+linkHref);
				  System.out.println("Text:"+linkText);
				  results++;
			  }
			  
		}
		System.out.println("Total Results:"+results);
		
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





