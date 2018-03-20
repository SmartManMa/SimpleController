package sc.ustc.dao;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import sc.ustc.bean.ClassBean;
import sc.ustc.bean.PropertyBean;

/**
 * @description Configuration.java��������or_mapping.xml��ȡ�ض���Ϣ
 * @author Administrator
 * @date 2017/12/26
 * @version
 */
public class Configuration {
	private static final String CONFIG_FILE_NAME = "or_mapping.xml";
	// �����ļ�·��
	private String defaultPath;
	private Document document;
	private Element rootElement;

	public Configuration() {
		this.defaultPath = Configuration.class.getClassLoader().getResource(CONFIG_FILE_NAME).getPath();
		File file = new File(defaultPath);
		init(file);
	}

	public Configuration(String path) {
		File file = new File(path);
		init(file);
	}

	public Configuration(File file) {
		init(file);
	};

	private void init(File file) {
		SAXReader reader = new SAXReader();
		try {
			document = reader.read(file);
			rootElement = document.getRootElement();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}

	/**
	 * getElements TODO :����or_mapping.xml�õ����ݿ�������Ϣ
	 * 
	 * @return ���ݿ�������ϢMap
	 * @author zhiman
	 * @date 2017/12/26 ����10:54:29
	 */
	public Map<String, String> getDBConfigMap() {

		Element JDBCElement = rootElement.element("jdbc");
		@SuppressWarnings("unchecked")
		List<Element> propertyElementList = JDBCElement.elements();
		Map<String, String> JDBCConfig = new HashMap<String, String>();
		for (Element property : propertyElementList) {
			// property.attribute("name").getValue();
			String name = property.element("name").getTextTrim();
			String value = property.element("value").getTextTrim();
			JDBCConfig.put(name, value);
		}
		return JDBCConfig;
	}

	/**
	 * getClassBeanSet TODO :����or_mapping.xml�õ����õ�Ԫ�ء�class������µ���Ϣ
	 * @return ����or_mapping.xml�õ�ClassBean������ɵ�List
	 * @author zhiman
	 * @date 2017/12/26 ����1:00:04
	 */
	public List<ClassBean> getClassBeanList() {
		// �洢����or_mapping.xml�õ�ClassBean����
		List<ClassBean> classBeanList = new LinkedList<ClassBean>();
		// ��ȡ���ڵ��µ�����class�ڵ�
		@SuppressWarnings("unchecked")
		List<Element> allClassList = rootElement.elements("class");
		for (Element clsNode : allClassList) {
			ClassBean classBean = new ClassBean();
			classBean.setBeanName(clsNode.element("name").getTextTrim());
			classBean.setTableName(clsNode.element("table").getTextTrim());
			// �洢class�ڵ��½�����������PropertyBean����
			List<PropertyBean> propertyList = new LinkedList<PropertyBean>();
			// �õ�class�ڵ�������property�ڵ�
			@SuppressWarnings("unchecked")
			List<Element> allPropertyList = clsNode.elements("property");
			// �õ�property�ڵ��µ���Ϣ
			for (Element propertyNode : allPropertyList) {
				PropertyBean propertyBean = new PropertyBean();
				propertyBean.setFiledName(propertyNode.element("name").getTextTrim());
				propertyBean.setColumnName(propertyNode.element("column").getTextTrim());
				propertyBean.setType(propertyNode.element("type").getTextTrim());
				propertyBean.setLazy(propertyNode.element("lazy").getTextTrim());
				propertyList.add(propertyBean);
			}
			classBean.setPropertyList(propertyList);
			classBeanList.add(classBean);
		}
		return classBeanList;
	}
}
