package ru.otus.container.aop;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AspectDefinition {

    private AspectType type;

    private Class<?> aspect;
}
