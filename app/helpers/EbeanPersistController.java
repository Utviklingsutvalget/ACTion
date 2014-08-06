package helpers;

import com.avaje.ebean.event.BeanPersistAdapter;
import com.avaje.ebean.event.BeanPersistRequest;

import javax.annotation.PreDestroy;
import javax.persistence.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

// Masterful credits to benoit-ponsero for creating https://gist.github.com/benoit-ponsero/3761155

public class EbeanPersistController extends BeanPersistAdapter {

    private Map<String, Method> prePersistMap = new TreeMap<>();
    private Map<String, Method> postPersistMap = new TreeMap<>();

    private Map<String, Method> preUpdateMap = new TreeMap<>();
    private Map<String, Method> postUpdateMap = new TreeMap<>();

    private Map<String, Method> preDestroyMap = new TreeMap<>();

    private Map<String, Method> postLoadMap = new TreeMap<>();


    @Override
    public boolean isRegisterFor(Class<?> aClass) {
        if (aClass.getAnnotation(Entity.class) != null) {

            Method[] methods = aClass.getMethods();
            boolean hasListener = false;
            for (Method m : methods) {

                if (m.isAnnotationPresent(PrePersist.class)) {
                    prePersistMap.put(aClass.getName(), m);
                    hasListener = true;
                }

                if (m.isAnnotationPresent(PostPersist.class)) {
                    postPersistMap.put(aClass.getName(), m);
                    hasListener = true;
                }

                if (m.isAnnotationPresent(PreDestroy.class)) {
                    preDestroyMap.put(aClass.getName(), m);
                    hasListener = true;
                }

                if (m.isAnnotationPresent(PreUpdate.class)) {
                    preUpdateMap.put(aClass.getName(), m);
                    hasListener = true;
                }

                if (m.isAnnotationPresent(PostUpdate.class)) {
                    postUpdateMap.put(aClass.getName(), m);
                    hasListener = true;
                }

                if (m.isAnnotationPresent(PostLoad.class)) {
                    postLoadMap.put(aClass.getName(), m);
                    hasListener = true;
                }
            }

            return hasListener;
        }
        return false;
    }

    private void getAndInvokeMethod(Map<String, Method> map, Object o) {

        Method m = map.get(o.getClass().getName());
        if (m != null) {
            try {
                m.invoke(o);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean preInsert(BeanPersistRequest<?> request) {
        getAndInvokeMethod(prePersistMap, request.getBean());
        return super.preInsert(request);
    }

    @Override
    public boolean preDelete(BeanPersistRequest<?> request) {
        getAndInvokeMethod(preDestroyMap, request.getBean());
        return super.preDelete(request);
    }

    @Override
    public boolean preUpdate(BeanPersistRequest<?> request) {
        getAndInvokeMethod(preUpdateMap, request.getBean());
        return super.preUpdate(request);
    }

    @Override
    public void postDelete(BeanPersistRequest<?> request) {
        // there is no @PostDestroy annotation in JPA 2
        super.postDelete(request);
    }

    @Override
    public void postInsert(BeanPersistRequest<?> request) {
        getAndInvokeMethod(postPersistMap, request.getBean());
        super.postInsert(request);
    }

    @Override
    public void postUpdate(BeanPersistRequest<?> request) {
        getAndInvokeMethod(postUpdateMap, request.getBean());
        super.postUpdate(request);
    }

    @Override
    public void postLoad(Object bean, Set<String> includedProperties) {
        getAndInvokeMethod(postLoadMap, bean);
        super.postLoad(bean, includedProperties);
    }
}
