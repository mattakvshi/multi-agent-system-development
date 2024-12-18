package org.example;

import java.util.List;

public class Artifact {
    private String name;
    private List<Effect> effects;

    public Artifact(String name, List<Effect> effects) {
        this.name = name;
        this.effects = effects;
    }

    public void applyArtifact(Colony colony, List<String> log) {
        for (Effect effect : effects) {
            colony.getEffects().add(effect);
        }
        log.add(colony.getName() + ": Артефакт '" + name + "' применён.");
    }

    public String getName() {
        return name;
    }
}
