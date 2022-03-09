package com.striketru.bc2akeneo.model;
/*
public class StringManipulations {
	public static void main(String[] args) {
		
		final StringBuilder stringBuilder = new StringBuilder("Vicki");
		stringBuilder.append(" ");
		stringBuilder.append("problems");
		System.out.println(stringBuilder);
		
	}

}*/
class StringManipulations{    
public static void main(String args[]){    
StringBuilder sb=new StringBuilder();    
System.out.println(sb.capacity());//default 16    
sb.append("Hello");    
System.out.println(sb.capacity());//now 16    
sb.append("Java is my favourite language shirt");    
System.out.println(sb.capacity());//now (16*2)+2=34 i.e (oldcapacity*2)+2    
sb.ensureCapacity(10);//now no change    
System.out.println(sb.capacity());//now 34    
sb.ensureCapacity(50);//now (34*2)+2    
System.out.println(sb.capacity()); //now 70    
sb.ensureCapacity(70);
System.out.println(sb.capacity());
}    
}   