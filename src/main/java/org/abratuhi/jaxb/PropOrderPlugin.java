package org.abratuhi.jaxb;

import com.sun.codemodel.JAnnotationArrayMember;
import com.sun.codemodel.JAnnotationUse;
import com.sun.codemodel.JDefinedClass;
import com.sun.tools.xjc.Options;
import com.sun.tools.xjc.Plugin;
import com.sun.tools.xjc.outline.ClassOutline;
import com.sun.tools.xjc.outline.Outline;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;

import javax.xml.bind.annotation.XmlType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PropOrderPlugin extends Plugin {

    public static final String OPTION_NAME = "XpropOrder";

    private Map<String, List<String>> propOrderMap = new HashMap<>();

    @Override
    public int parseArgument(Options opt, String[] args, int i) {
        String arg = args[i].trim();
        System.out.println(arg);
        return 0;
    }

    @Override
    public String getOptionName() {
        return OPTION_NAME;
    }

    @Override
    public String getUsage() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("  -").append(OPTION_NAME).append(":  change propOrder in XmlType annotation \n");
        return stringBuilder.toString();
    }

    @Override
    public boolean run(Outline outline, Options options, ErrorHandler errorHandler) throws SAXException {
        for (ClassOutline generatedClassOutline : outline.getClasses()) {
            JDefinedClass generatedClass = generatedClassOutline.implClass;
            adjustPropOrder(generatedClass, propOrderMap);
        }
        return true;
    }

    protected void adjustPropOrder(JDefinedClass generatedClass, Map<String, List<String>> propOrderMap) {
        for (JAnnotationUse jAnnotationUse: generatedClass.annotations()) {
            if (jAnnotationUse.getAnnotationClass().binaryName().equals(XmlType.class.getName())) {
                String className = generatedClass.name();
                if (propOrderMap.containsKey(className)) {
                    JAnnotationArrayMember propOrderAnnotationValue = jAnnotationUse.paramArray("propOrder");
                    for (String property: propOrderMap.get(className)) {
                        propOrderAnnotationValue = propOrderAnnotationValue.param(property);
                    }
                }
            }
        }
    }
}
