package ru.otus.equation.services;

import ru.otus.equation.model.Equation;

import java.util.List;

public interface EquationPreparer {
    List<Equation> prepareEquationsFor(int base);
}
