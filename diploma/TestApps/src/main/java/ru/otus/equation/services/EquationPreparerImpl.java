package ru.otus.equation.services;

import ru.otus.container.annotation.Component;
import ru.otus.container.annotation.Transactional;
import ru.otus.equation.model.DivisionEquation;
import ru.otus.equation.model.Equation;
import ru.otus.equation.model.MultiplicationEquation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component(name = "equationPreparer")
public class EquationPreparerImpl implements EquationPreparer {
    @Override
    public List<Equation> prepareEquationsFor(int base) {
        List<Equation> equations = new ArrayList<>();
        for (int i = 1; i < 11; i++) {
            var multiplicationEquation = new MultiplicationEquation(base, i);
            var divisionEquation = new DivisionEquation(multiplicationEquation.getResult(), base);
            equations.add(multiplicationEquation);
            equations.add(divisionEquation);

        }
        Collections.shuffle(equations);
        return equations;
    }
}
