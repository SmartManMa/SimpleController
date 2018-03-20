package sc.ustc.dao;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import sc.ustc.bean.ClassBean;
import sc.ustc.bean.PropertyBean;

public class Conversion {
	/** 
	  * getObject TODO :
	  * @param t
	  * @return
	  * @author zhiman
	  * @date 2017/12/26 ����3:44:57 
	  */
	public static  Object getObject(Object obj){
		Class<?> cls = obj.getClass();
		Configuration config = new Configuration();
		ClassBean clsBean = null;
		List<ClassBean> classBeanList = config.getClassBeanList();
		//�ҵ�ӳ���UserBean
		for ( ClassBean cb : classBeanList ) {
			System.out.println(cb.getBeanName());
			System.out.println(cls.getName());
			String str = cls.getName();
			String clsname = str.substring(str.lastIndexOf(".")+1);
			System.out.println(clsname);
			if ( cb.getBeanName().equals( clsname ) ) {
				clsBean = cb;
				break;
			}
		}
		if ( clsBean == null ) {
			System.out.println("ӳ�����");
			return null;
		}
		//��ӳ���ϵ�еõ�ʹ�õı�
		String table = clsBean.getTableName();
		System.out.println(table);
		//����Ķ������еĳ�Ա����������filedList��
		List<PropertyBean> propertyList = clsBean.getPropertyList();
		//�洢��Ӧ�ĳ�Ա��������ֵ
		Map<String,String> map = new HashMap<String, String>();
		String columnName = null;
		for (PropertyBean property : propertyList) {
			//�õ�fieldName��Ӧ��get����
			String fieldName =  property.getFiledName();
			System.out.println(fieldName);
			char ch = fieldName.charAt(0);
			String rear = fieldName.replace(ch, Character.toUpperCase(ch));
			String methodName = "get"+rear;
			System.out.println(methodName);
			//�õ���Ա��������
			String type = property.getType();
			String result = null;
			//���ݷ������������ҵ���Ӧ��get����
			try {
				Method method = cls.getMethod(methodName);
				if ( type instanceof String) {
					//����ת��,�õ�fieldName��Ա������ֵ
					result = (String) method.invoke(obj);
					//�������û�б���ʼ��
					if ( result == null ) {
						continue;
					} else {
						columnName = property.getColumnName();
						map.put(columnName, result);
						break;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		String querySql = null;
		if (map.size() != 0) {
			Set< Entry< String,String> > ss = map.entrySet();
			for (Entry< String,String> entry : ss) {
				StringBuilder sb = new StringBuilder("select * from ");
				sb.append(table);
				sb.append(" where ");
				sb.append(entry.getKey());
				sb.append("=");
				sb.append("\"");
				sb.append(entry.getValue());
				sb.append("\";");
				querySql = sb.toString();
				System.out.println(querySql);
			}
		}
		return new BaseDAO().query(querySql);
	}
}
