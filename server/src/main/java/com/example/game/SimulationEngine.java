package com.example.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Main simulation engine that manages creatures, environment, and game loop.
 */
public class SimulationEngine {
    private static final Random RANDOM = new Random();
    
    private List<Creature> creatures;
    private Environment environment;
    private int turn;
    private SimulationStats stats;
    
    public SimulationEngine(Environment environment) {
        this.environment = environment;
        this.creatures = new ArrayList<>();
        this.turn = 0;
        this.stats = new SimulationStats();
    }
    
    /**
     * Initialize the simulation with a population of creatures
     */
    public void initialize(int herbivoreCount, int carnivoreCount, int omnivoreCount) {
        creatures.clear();
        
        // Create herbivores
        for (int i = 0; i < herbivoreCount; i++) {
            creatures.add(new Creature("Herb-" + i, Creature.CreatureType.HERBIVORE, 0));
        }
        
        // Create carnivores
        for (int i = 0; i < carnivoreCount; i++) {
            creatures.add(new Creature("Carn-" + i, Creature.CreatureType.CARNIVORE, 0));
        }
        
        // Create omnivores
        for (int i = 0; i < omnivoreCount; i++) {
            creatures.add(new Creature("Omni-" + i, Creature.CreatureType.OMNIVORE, 0));
        }
        
        stats.reset();
        updateStats();
    }
    
    /**
     * Run one turn of the simulation
     */
    public void simulateTurn() {
        turn++;
        
        // Update environment
        environment.update();
        
        // Get alive creatures
        List<Creature> aliveCreatures = creatures.stream()
            .filter(Creature::isAlive)
            .collect(Collectors.toList());
        
        // Each creature takes an action
        List<Creature> newborns = new ArrayList<>();
        
        for (Creature creature : aliveCreatures) {
            // Apply environmental pressure
            environment.applyEnvironmentalPressure(creature);
            
            if (!creature.isAlive()) continue;
            
            // Decide and perform action
            Creature.Action action = creature.decideAction(environment, aliveCreatures.toArray(new Creature[0]));
            
            switch (action) {
                case HUNT:
                    performHunt(creature, aliveCreatures);
                    break;
                case FORAGE:
                    creature.forage(environment);
                    break;
                case MOVE:
                    creature.move();
                    break;
                case REST:
                    creature.rest();
                    break;
                case REPRODUCE:
                    Creature child = performReproduction(creature, aliveCreatures);
                    if (child != null) {
                        newborns.add(child);
                        stats.incrementBirths();
                    }
                    break;
            }
        }
        
        // Add newborn creatures
        creatures.addAll(newborns);
        
        // Update statistics
        updateStats();
    }
    
    private void performHunt(Creature predator, List<Creature> aliveCreatures) {
        // Find potential prey
        List<Creature> potentialPrey = aliveCreatures.stream()
            .filter(c -> c != predator && c.isAlive())
            .filter(c -> c.getType() != Creature.CreatureType.CARNIVORE || predator.getStrength() > c.getStrength())
            .collect(Collectors.toList());
        
        if (!potentialPrey.isEmpty()) {
            Creature prey = potentialPrey.get(RANDOM.nextInt(potentialPrey.size()));
            if (predator.hunt(prey)) {
                stats.incrementDeaths();
            }
        }
    }
    
    private Creature performReproduction(Creature creature, List<Creature> aliveCreatures) {
        // Find suitable partner
        List<Creature> potentialPartners = aliveCreatures.stream()
            .filter(c -> c != creature && c.isAlive())
            .filter(c -> c.getType() == creature.getType())
            .filter(c -> c.getEnergy() > c.getMaxEnergy() * 0.5)
            .collect(Collectors.toList());
        
        if (!potentialPartners.isEmpty()) {
            Creature partner = potentialPartners.get(RANDOM.nextInt(potentialPartners.size()));
            return creature.reproduce(partner);
        }
        
        return null;
    }
    
    private void updateStats() {
        stats.setTurn(turn);
        stats.setTotalCreatures(creatures.size());
        
        long aliveCount = creatures.stream().filter(Creature::isAlive).count();
        stats.setAliveCreatures((int) aliveCount);
        
        long herbivores = creatures.stream()
            .filter(Creature::isAlive)
            .filter(c -> c.getType() == Creature.CreatureType.HERBIVORE)
            .count();
        stats.setHerbivoreCount((int) herbivores);
        
        long carnivores = creatures.stream()
            .filter(Creature::isAlive)
            .filter(c -> c.getType() == Creature.CreatureType.CARNIVORE)
            .count();
        stats.setCarnivoreCount((int) carnivores);
        
        long omnivores = creatures.stream()
            .filter(Creature::isAlive)
            .filter(c -> c.getType() == Creature.CreatureType.OMNIVORE)
            .count();
        stats.setOmnivoreCount((int) omnivores);
        
        double avgFitness = creatures.stream()
            .filter(Creature::isAlive)
            .mapToDouble(Creature::getFitness)
            .average()
            .orElse(0.0);
        stats.setAverageFitness(avgFitness);
        
        int maxGen = creatures.stream()
            .filter(Creature::isAlive)
            .mapToInt(Creature::getGeneration)
            .max()
            .orElse(0);
        stats.setMaxGeneration(maxGen);
    }
    
    /**
     * Run simulation for multiple turns
     */
    public void simulate(int turns) {
        for (int i = 0; i < turns; i++) {
            simulateTurn();
            
            // Stop if all creatures are dead
            if (creatures.stream().noneMatch(Creature::isAlive)) {
                break;
            }
        }
    }
    
    public List<Creature> getCreatures() {
        return new ArrayList<>(creatures);
    }
    
    public List<Creature> getAliveCreatures() {
        return creatures.stream()
            .filter(Creature::isAlive)
            .collect(Collectors.toList());
    }
    
    public Environment getEnvironment() {
        return environment;
    }
    
    public int getTurn() {
        return turn;
    }
    
    public SimulationStats getStats() {
        return stats;
    }
}
