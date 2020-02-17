package com.amirnadiv.project.utils.common.bean.introspector;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;

import com.amirnadiv.project.utils.common.CollectionUtil;
import com.amirnadiv.project.utils.common.ObjectUtil;
import com.amirnadiv.project.utils.common.ReflectionUtil;

public class Fields {

    protected final ClassDescriptor classDescriptor;
    protected final Map<String, FieldDescriptor> fieldsMap;

    // cache
    private FieldDescriptor[] allFields;

    public Fields(ClassDescriptor classDescriptor) {
        this.classDescriptor = classDescriptor;
        this.fieldsMap = inspectFields();
    }

    protected Map<String, FieldDescriptor> inspectFields() {
        boolean scanAccessible = classDescriptor.isScanAccessible();
        Class<?> type = classDescriptor.getType();

        Field[] fields =
                scanAccessible ? ReflectionUtil.getAccessibleFields(type) : ReflectionUtil.getAllFieldsOfClass(type);

        Map<String, FieldDescriptor> map = CollectionUtil.createHashMap(fields.length);

        for (Field field : fields) {
            String fieldName = field.getName();

            if (fieldName.equals(ObjectUtil.SERIAL_VERSION_UID)) {
                continue;
            }

            map.put(fieldName, createFieldDescriptor(field));
        }

        return map;
    }

    protected FieldDescriptor createFieldDescriptor(Field field) {
        return new FieldDescriptor(classDescriptor, field);
    }

    public FieldDescriptor getFieldDescriptor(String name) {
        return fieldsMap.get(name);
    }

    public FieldDescriptor[] getAllFieldDescriptors() {
        if (allFields == null) {
            FieldDescriptor[] allFields = new FieldDescriptor[fieldsMap.size()];

            int index = 0;
            for (FieldDescriptor fieldDescriptor : fieldsMap.values()) {
                allFields[index] = fieldDescriptor;
                index++;
            }

            Arrays.sort(allFields, new Comparator<FieldDescriptor>() {
                public int compare(FieldDescriptor fd1, FieldDescriptor fd2) {
                    return fd1.getField().getName().compareTo(fd2.getField().getName());
                }
            });

            this.allFields = allFields;
        }

        return allFields;
    }

}
