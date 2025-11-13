package com.example.game;

import java.util.Random;

/**
 * Represents a creature in the evolution simulation with genetic traits.
 * Creatures can evolve, hunt, flee, and reproduce based on their traits.
 */
public class Creature {
    private static final Random RANDOM = new Random();
    private static int nextId = 1;
    
    private final int id;
    private String name;
    private double speed;        // 0-100: affects movement and fleeing
    private double strength;     // 0-100: affects hunting and combat
    private double intelligence; // 0-100: affects decision making
    private double size;         // 0-100: affects energy consumption and combat
    private double energy;       // Current energy level
    private double maxEnergy;    // Maximum energy capacity
    private boolean isAlive;
    private CreatureType type;
    private int generation;
    
    public enum CreatureType {
        HERBIVORE,  // Eats plants, flees from predators
        CARNIVORE,  // Hunts other creatures
        OMNIVORE    // Can eat both plants and creatures
    }
    
    public Creature(String name, CreatureType type, int generation) {
        this.id = nextId++;
        this.name = name;
        this.type = type;
        this.generation = generation;
        this.isAlive = true;
        
        // Initialize random traits
        this.speed = 30 + RANDOM.nextDouble() * 40;
        this.strength = 30 + RANDOM.nextDouble() * 40;
        this.intelligence = 30 + RANDOM.nextDouble() * 40;
        this.size = 30 + RANDOM.nextDouble() * 40;
        this.maxEnergy = 100 + (size * 2);
        this.energy = maxEnergy;
    }
    
    /**
     * Creates a child creature with inherited and mutated traits
     */
    public Creature reproduce(Creature partner) {
        if (!isAlive || !partner.isAlive || energy < maxEnergy * 0.5) {
            return null;
        }
        
        Creature child = new Creature(
            generateChildName(this, partner),
            this.type,
            Math.max(this.generation, partner.generation) + 1
        );
        
        // Inherit traits from parents with mutation
        child.speed = inheritTrait(this.speed, partner.speed, 10);
        child.strength = inheritTrait(this.strength, partner.strength, 10);
        child.intelligence = inheritTrait(this.intelligence, partner.intelligence, 10);
        child.size = inheritTrait(this.size, partner.size, 10);
        child.maxEnergy = 100 + (child.size * 2);
        child.energy = child.maxEnergy;
        
        // Reproduction costs energy
        this.energy -= maxEnergy * 0.3;
        partner.energy -= partner.maxEnergy * 0.3;
        
        return child;
    }
    
    private double inheritTrait(double parent1Trait, double parent2Trait, double mutationRate) {
        double inherited = (parent1Trait + parent2Trait) / 2.0;
        double mutation = (RANDOM.nextDouble() - 0.5) * mutationRate;
        return Math.max(0, Math.min(100, inherited + mutation));
    }
    
    private String generateChildName(Creature parent1, Creature parent2) {
        return parent1.getName() + "-" + parent2.getName() + "-G" + (generation);
    }
    
    /**
     * Attempt to hunt another creature
     */
    public boolean hunt(Creature prey) {
        if (!isAlive || !prey.isAlive || type == CreatureType.HERBIVORE) {
            return false;
        }
        
        double huntChance = (this.strength + this.speed + this.intelligence) / 3.0;
        double escapeChance = (prey.speed + prey.intelligence) / 2.0;
        double huntSuccess = huntChance - escapeChance + (RANDOM.nextDouble() * 30 - 15);
        
        if (huntSuccess > 0) {
            prey.die();
            this.energy = Math.min(maxEnergy, this.energy + prey.size * 0.8);
            return true;
        }
        
        // Failed hunt costs energy
        this.energy -= 5;
        return false;
    }
    
    /**
     * Forage for food in the environment
     */
    public void forage(Environment environment) {
        if (!isAlive || type == CreatureType.CARNIVORE) {
            return;
        }
        
        double forageAmount = environment.consumeResource(this.intelligence / 10.0);
        this.energy = Math.min(maxEnergy, this.energy + forageAmount);
    }
    
    /**
     * Move in the environment, consuming energy
     */
    public void move() {
        if (!isAlive) return;
        
        double energyCost = (size / 20.0) + (speed / 50.0);
        this.energy -= energyCost;
        
        if (this.energy <= 0) {
            die();
        }
    }
    
    /**
     * Rest to recover some energy
     */
    public void rest() {
        if (!isAlive) return;
        
        double recovery = intelligence / 20.0;
        this.energy = Math.min(maxEnergy, this.energy + recovery);
    }
    
    /**
     * Make a decision based on current state
     */
    public Action decideAction(Environment environment, Creature[] nearbyCreatures) {
        if (!isAlive) return Action.REST;
        
        // Low energy? Find food or rest
        if (energy < maxEnergy * 0.3) {
            if (type == CreatureType.CARNIVORE) {
                return Action.HUNT;
            } else {
                return Action.FORAGE;
            }
        }
        
        // High energy? Maybe reproduce
        if (energy > maxEnergy * 0.7 && RANDOM.nextDouble() < 0.1) {
            return Action.REPRODUCE;
        }
        
        // Moderate energy? Balance between survival and growth
        double decision = RANDOM.nextDouble() * intelligence;
        if (decision < 20) {
            return Action.MOVE;
        } else if (decision < 40) {
            return type == CreatureType.CARNIVORE ? Action.HUNT : Action.FORAGE;
        } else {
            return Action.REST;
        }
    }
    
    public void die() {
        this.isAlive = false;
        this.energy = 0;
    }
    
    public double getFitness() {
        return (speed + strength + intelligence + size) / 4.0;
    }
    
    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public double getSpeed() { return speed; }
    public double getStrength() { return strength; }
    public double getIntelligence() { return intelligence; }
    public double getSize() { return size; }
    public double getEnergy() { return energy; }
    public double getMaxEnergy() { return maxEnergy; }
    public boolean isAlive() { return isAlive; }
    public CreatureType getType() { return type; }
    public int getGeneration() { return generation; }
    
    public enum Action {
        HUNT, FORAGE, MOVE, REST, REPRODUCE
    }
}
