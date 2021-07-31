package ru.otus.container.core;

import ru.otus.container.aop.AspectBeanPostProcessor;

import java.util.ArrayList;
import java.util.List;

public class BeanPostProcessorsProcessor {
    private final Context context;

    private final List<BeanPostProcessor> beanPostProcessors = new ArrayList<>();

    public BeanPostProcessorsProcessor(Context context) {
        this.context = context;
        addDefaultBeanPostProcessor();
        checkForBeanPostProcessors();
    }

    private void addDefaultBeanPostProcessor() {
        beanPostProcessors.add(new PostConstructBeanPostProcessor());
        beanPostProcessors.add(new AspectBeanPostProcessor());
    }

    private void checkForBeanPostProcessors() {
        context.getAllBeans().values().forEach(b -> {
            if (BeanPostProcessor.class.isAssignableFrom(b.getClass())) beanPostProcessors.add((BeanPostProcessor) b);
        });
    }

    public void processBeanPostProcessors() {
        context.getAllBeans()
                .forEach((name, bean) -> {
                    Class<?> beanClass = bean.getClass();
                    beanPostProcessors.forEach(bpp -> context.addBean(name, beanClass, bpp.postProcess(bean)));
                });
    }
}
