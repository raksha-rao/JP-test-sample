package com.jptest.payments.fulfillment.testonia.business.vo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.text.StrSubstitutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.jptest.payments.fulfillment.testonia.core.exception.TestExecutionException;
import com.jptest.payments.fulfillment.testonia.core.util.VoHelper;
import com.jptest.vo.ValueObject;


/**
 * 
 * TemplateVOBuilder allows building VOs using templated files and a map of values
 * 
 * @JP Inc.
 *
 * @param <T> Type of ValueObject to be built
 */
public abstract class TemplateVOBuilder<T extends ValueObject> extends ListWrappedVOBuilder<T> {
    
    private static Logger logger = LoggerFactory.getLogger(TemplateVOBuilder.class);

    private Class<T> clz;
    
    @SuppressWarnings("unchecked")
    public TemplateVOBuilder() {
        ParameterizedType type = (ParameterizedType) getClass().getGenericSuperclass();
        this.clz = (Class<T>) type.getActualTypeArguments()[0];
    }
    
    public abstract String getResource();
    
    /**
     * 
     * getValueMap() is used to fill in placehoders in template.  Extending class
     * must set all placeholders and their values.
     * 
     * @return map of placeholders and their values to be filled in the template
     */
    public abstract Map<String,Object>getValueMap();
    
    @Override
    public T build() {
        String resourceName = getResource();
        try {
            String template = loadTemplateFromResource(resourceName);
            StrSubstitutor sub = new StrSubstitutor(getValueMap());
            String voString = sub.replace(template);
            logger.debug("vo from {} after replacement {}", resourceName, voString);
            return (T)VoHelper.deserializeByteArray2VO(clz, voString.getBytes("UTF-8"));
        }
        catch (IOException e) {
            throw new TestExecutionException("Error while building vo from template", e);
        }

    }
    
    private String loadTemplateFromResource(String resourceName) throws IOException {
        InputStream is = ClassLoader.class.getResourceAsStream(resourceName);
        if (is == null) {
            throw new FileNotFoundException(resourceName + " does not exist");
        }
        return IOUtils.toString(is, "UTF-8");
    }

    @Override
    public List<T> buildList() {
        return null;
    }

}
