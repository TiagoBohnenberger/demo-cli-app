package br.com.alura.util;

import java.lang.annotation.Annotation;
import jakarta.enterprise.context.spi.CreationalContext;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.enterprise.inject.spi.CDI;

import br.com.alura.core.UnresolvedBeanException;
import lombok.experimental.UtilityClass;

@UtilityClass
public class BeanUtils {

    private static final BeanManager manager = CDI.current().getBeanManager();

    public static <T> T getInstanceByType(Class<T> type, Annotation... bindings) {
        final Bean<?> bean = manager.resolve(manager.getBeans(type, bindings));
        if (bean == null) {
            throw new UnresolvedBeanException("Bean type \"" + type.getName() + "\" not resolvable");
        }
        CreationalContext<?> cc = manager.createCreationalContext(bean);
        return type.cast(manager.getReference(bean, type, cc));
    }
}