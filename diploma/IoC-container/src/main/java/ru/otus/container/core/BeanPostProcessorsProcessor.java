package ru.otus.container.core;

import java.util.List;

public class BeanPostProcessorsProcessor {
    private final Context context;

    private final List<BeanPostProcessor> beanPostProcessors;

    public BeanPostProcessorsProcessor(Context context, List<BeanPostProcessor> beanPostProcessors) {
        this.context = context;
        this.beanPostProcessors = beanPostProcessors;
    }

    public void processBeanPostProcessors() {
        context.getAllBeans()
                .forEach((name, bean) -> {
                    Class<?> beanClass = bean.getClass();
                    beanPostProcessors.forEach(bpp -> context.addBean(name, beanClass, bpp.postProcess(bean)));
                });
    }
}
