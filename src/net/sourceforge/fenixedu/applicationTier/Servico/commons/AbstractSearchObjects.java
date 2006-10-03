package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.StringNormalizer;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.beanutils.PropertyUtils;

public abstract class AbstractSearchObjects extends Service implements AutoCompleteSearchService {

    protected List<DomainObject> process(Collection<DomainObject> objects, String value, int limit,
            Map<String, String> arguments) {
        List<DomainObject> result = new ArrayList<DomainObject>();

        String slotName = arguments.get("slot");

        if (value == null) {
            result.addAll(objects);
        } else {
            String[] values = StringNormalizer.normalize(value).toLowerCase().split("\\p{Space}+");

            outter: for (DomainObject object : objects) {
                try {
                    String objectValue = (String) PropertyUtils.getProperty(object, slotName);

                    if (objectValue == null) {
                        continue;
                    }

                    String normalizedValue = StringNormalizer.normalize(objectValue).toLowerCase();

                    for (int i = 0; i < values.length; i++) {
                        String part = values[i];

                        if (!normalizedValue.contains(part)) {
                            continue outter;
                        }
                    }

                    result.add(object);

                    if (result.size() >= limit) {
                        break;
                    }

                } catch (IllegalAccessException e) {
                    throw new DomainException("searchObject.type.notFound", e);
                } catch (InvocationTargetException e) {
                    throw new DomainException("searchObject.failed.read", e);
                } catch (NoSuchMethodException e) {
                    throw new DomainException("searchObject.failed.read", e);
                }
            }
        }

        Collections.sort(result, new BeanComparator(slotName));
        return result;
    }
}
