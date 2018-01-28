package org.abratuhi.jaxb;

import com.sun.codemodel.*;
import org.junit.Test;

import javax.xml.bind.annotation.XmlType;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class PropOrderPluginTest {

    private final PropOrderPlugin plugin = new PropOrderPlugin();

    @Test
    public void testNoXmlType() throws JClassAlreadyExistsException {
        JCodeModel aModel = new JCodeModel();
        JPackage aPackage = aModel._package("test");
        JDefinedClass aClass = aPackage._class("AClass");
        plugin.adjustPropOrder(aClass, new HashMap<>());
        for (JAnnotationUse jAnnotationUse: aClass.annotations()) {
            if (jAnnotationUse.getAnnotationClass().getClass().equals(XmlType.class)) {
                fail();
            }
        }
        // if we're here, then no XmlType annotation has been added -> OK
        assertTrue("", true);
    }

    @Test
    public void textXmlTypeNoPropOrder() throws JClassAlreadyExistsException {
        JCodeModel aModel = new JCodeModel();
        JPackage aPackage = aModel._package("test");
        JDefinedClass aClass = aPackage._class("AClass");
        aClass.annotate(XmlType.class);
        plugin.adjustPropOrder(aClass, new HashMap<String, List<String>>(){{put("AClass", new ArrayList<String>(){{add("prop2"); add("prop1");}});}});
        for (JAnnotationUse jAnnotationUse: aClass.annotations()) {
            if (jAnnotationUse.getAnnotationClass().binaryName().equals(XmlType.class.getName())) {
                StringWriter sw = new StringWriter();
                JFormatter jFormatter = new JFormatter(sw);
                jAnnotationUse.getAnnotationMembers().get("propOrder").generate(jFormatter);
                assertEquals("", "{\n" +
                        "    \"prop2\",\n" +
                        "    \"prop1\"\n" +
                        "}", sw.toString().replaceAll("\\r\\n", "\n"));
            }
        }
    }

    @Test
    public void testXmlTypeOverwritePropOrder() throws JClassAlreadyExistsException {
        JCodeModel aModel = new JCodeModel();
        JPackage aPackage = aModel._package("test");
        JDefinedClass aClass = aPackage._class("AClass");
        JAnnotationUse xmlTypeAnnotation = aClass.annotate(XmlType.class);
        xmlTypeAnnotation.paramArray("propOrder").param("prop1").param("prop2");
        plugin.adjustPropOrder(aClass, new HashMap<String, List<String>>(){{put("AClass", new ArrayList<String>(){{add("prop2"); add("prop1");}});}});
        for (JAnnotationUse jAnnotationUse: aClass.annotations()) {
            if (jAnnotationUse.getAnnotationClass().binaryName().equals(XmlType.class.getName())) {
                StringWriter sw = new StringWriter();
                JFormatter jFormatter = new JFormatter(sw);
                jAnnotationUse.getAnnotationMembers().get("propOrder").generate(jFormatter);
                assertEquals("", "{\n" +
                        "    \"prop2\",\n" +
                        "    \"prop1\"\n" +
                        "}", sw.toString().replaceAll("\\r\\n", "\n"));
            }
        }
    }
}
