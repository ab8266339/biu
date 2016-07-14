package bglutil.common;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import bglutil.main.Biu;

public class Helper {
	
	// Enable the '-h' option for methods which call help.
	public void help(String help, String helpMessage){
		if(help.equals("-h")){
			StackTraceElement element = Thread.currentThread().getStackTrace()[2];
			System.out.println("\n b "+element.getMethodName()+" "+helpMessage+"\n");
			System.exit(0);
		}
	}
	
	// Help those who cannot remember command names.
	public static void searh(String commandPrefix){
		Method[] allMethods = Biu.class.getDeclaredMethods();
		for(Method m:allMethods){
			if(Biu.SKIPPED_METHODS.contains(m.getName())){
				continue;
			}
			if(Modifier.isPublic(m.getModifiers()) && m.getName().startsWith(commandPrefix)){
				System.out.println(m.getName());
			}
		}
	}
	
	public void title(String title){
		StringBuffer line = new StringBuffer("\n");
		for(int c=0;c<title.length();c++){
			line.append('_');
		}
		System.out.println(line+"\\");
		System.out.println(title+":\n");
	}
	
	
}
