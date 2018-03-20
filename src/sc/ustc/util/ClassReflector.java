package sc.ustc.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ClassReflector {
	/** 
	  * gainClass TODO :���������õ�Class����
	  * @param className ����
	  * @return Class����
	  * @throws ClassNotFoundException
	  * @author zhiman
	  * @date 2017/12/07 ����11:40:31 
	  */
	public static Class<?> gainClass(String className) throws ClassNotFoundException {
		Class<?> cls = Class.forName(className);
		return cls;
	}
	
	/** 
	  * executeMethod TODO : ����Reflectִ���޲��ҷ����ַ����ķ���
	  * @param cls Class����
	  * @param method ����
	  * @return �������ص��ַ���
	  * @throws IllegalAccessException
	  * @throws IllegalArgumentException
	  * @throws InvocationTargetException
	  * @throws InstantiationException
	  * @author zhiman
	  * @date 2017/12/07 ����11:41:17 
	  */
	public static String executeMethod(Class<?> cls, Method method) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException {
		String result = null;
		Object obj = method.invoke(cls.newInstance());
		if (obj instanceof String) {
			result = (String)obj;
		} 
		return result;
	}
	/** 
	  * executeMethod TODO :����Reflectִ���޲��ҷ����ַ����ķ���
	  * @param cls Class����
	  * @param methodName ������
	  * @return ��ִ�еķ������ص��ַ���
	  * @throws NoSuchMethodException
	  * @throws SecurityException
	  * @throws IllegalAccessException
	  * @throws IllegalArgumentException
	  * @throws InvocationTargetException
	  * @throws InstantiationException
	  * @author zhiman
	  * @date 2017/12/07 ����11:43:43 
	  */
	public static String executeMethod(Class<?> cls,String methodName) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException {
		Method method = cls.getMethod(methodName);
		return executeMethod(cls, method);
	}
	/** 
	  * executeMethod TODO :
	  * @param className
	  * @param methodName
	  * @return ��ִ�еķ������ص��ַ���
	  * @throws ClassNotFoundException
	  * @throws NoSuchMethodException
	  * @throws SecurityException
	  * @throws IllegalAccessException
	  * @throws IllegalArgumentException
	  * @throws InvocationTargetException
	  * @throws InstantiationException
	  * @author zhiman
	  * @date 2017/12/07 ����11:44:49 
	  */
	public static String executeMethod(String className,String methodName) throws ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException {
		Class<?> cls = gainClass(className);
		return executeMethod(cls,methodName);
	}
	
/*-----------------------------��дrunMethods����------------------------------------*/	
	public static Object runMethods(Class<?> cls, Method method,Object...args) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException {
		return method.invoke(cls.newInstance(),args);
	}
	public static Object runMethods(Class<?> cls,String methodName,Object...args) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException {
		Method method = cls.getMethod(methodName,HttpServletRequest.class,HttpServletResponse.class);
		return runMethods(cls, method,args);
	}
	public static Object runMethods(String className,String methodName,Object...args) throws ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException {
		Class<?> cls = gainClass(className);
		return runMethods(cls,methodName,args);
	}
}
