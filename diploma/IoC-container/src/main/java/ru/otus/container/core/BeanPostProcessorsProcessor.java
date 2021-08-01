package ru.otus.container.core;

import java.util.List;
import java.util.stream.Collectors;

public class BeanPostProcessorsProcessor {

    public void processBeanPostProcessors(Context context) {
        List<BeanPostProcessor> beanPostProcessors = context.getAllBeans().values().stream()
                .filter(b -> (BeanPostProcessor.class.isAssignableFrom(b.getClass())))
                .map(b -> (BeanPostProcessor) b)
                .collect(Collectors.toList());

        context.getAllBeans()
                .forEach((name, bean) -> {
                    Class<?> beanClass = bean.getClass();
                    for (BeanPostProcessor bpp : beanPostProcessors) {
                        bean = bpp.postProcess(bean);
                    }
                    context.addBean(name, beanClass, bean);
                });
    }

}
