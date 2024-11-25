package br.com.alura.core;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.enterprise.inject.spi.Extension;
import jakarta.enterprise.inject.spi.ProcessBean;

import br.com.alura.util.Functions;
import lombok.extern.log4j.Log4j2;
import org.jboss.weld.environment.se.events.ContainerInitialized;
import org.jboss.weld.environment.se.events.ContainerShutdown;

@Log4j2
class AppInitializerComplement implements Extension {
    private static final Set<Class<? extends Annotation>> annotations = Set.of(Eager.class, ApplicationScoped.class);

    private final List<Bean<?>> eagerBeansList = new ArrayList<>();

    private <T> void init(@Observes ProcessBean<T> bean) {
        if (isAnnotationsPresentOn(bean)) {
            eagerBeansList.add(bean.getBean());
        }
    }

    private static <T> boolean isAnnotationsPresentOn(ProcessBean<T> bean) {
        return annotations.stream()
                .allMatch(ann ->
                        bean.getAnnotated().isAnnotationPresent(ann));
    }

    private void load(@Observes ContainerInitialized event, BeanManager beanManager) {
        sortByHighestPrecedence();
        initEagerBeans(beanManager);
    }

    private void onApplicationDestroyed(@Observes ContainerShutdown event) {
        Functions.println("Até mais!");
    }

    private void initEagerBeans(BeanManager beanManager) {
        for (Bean<?> bean : eagerBeansList) {
            // note: toString() is important to instantiate the bean
            beanManager.getReference(bean, bean.getBeanClass(), beanManager.createCreationalContext(bean)).toString();
        }
    }

    private void sortByHighestPrecedence() {
        eagerBeansList.sort(Comparator.comparingInt(bean ->
                bean.getBeanClass().getAnnotation(Eager.class).priority()));
    }
}