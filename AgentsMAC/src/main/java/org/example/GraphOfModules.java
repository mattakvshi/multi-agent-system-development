package org.example;

import java.util.ArrayList;
import java.util.List;

public class GraphOfModules {
    private List<Module> listOfModules;

    public GraphOfModules(int m, float a, float b) {
        this.listOfModules = new ArrayList<>();
        for (int i = 0; i < m; i++) {
            listOfModules.add(new Module(i + 1, a, b));
        }
    }

    public void creatingDependencies(Module moduleMain, Module moduleDependent) {
        if (!moduleDependent.getDependencies().contains(moduleMain) && !moduleMain.getDependencies().contains(moduleDependent)) {
            moduleDependent.addDependency(moduleMain);
            System.out.println("Модуль id = " + moduleDependent.getId() + " зависим от модуля id = " + moduleMain.getId());
        } else {
            System.out.println("Не удалось установить зависимость между модулями id = " + moduleMain.getId() + " и id = " + moduleDependent.getId());
        }
    }

    // Getter for listOfModules
    public List<Module> getListOfModules() {
        return listOfModules;
    }
}