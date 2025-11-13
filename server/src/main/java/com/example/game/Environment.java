package com.example.game;

import java.util.Random;

/**
 * Represents the game environment with resources and environmental pressures.
 */
public class Environment {
    private static final Random RANDOM = new Random();
    
    private double resources;           // Available food resources
    private double maxResources;        // Maximum resource capacity
    private double resourceGrowthRate;  // How fast resources regenerate
    private double temperature;         // Environmental temperature (affects creature survival)
    private double hazardLevel;         // Random hazards/disasters
    private String biome;               // Type of environment
    
    public enum Biome {
        GRASSLAND("Grassland", 1.0, 50),
        FOREST("Forest", 1.2, 40),
        DESERT("Desert", 0.5, 70),
        TUNDRA("Tundra", 0.3, 10),
        JUNGLE("Jungle", 1.5, 60);
        
        private final String name;
        private final double resourceMultiplier;
        private final double baseTemperature;
        
        Biome(String name, double resourceMultiplier, double baseTemperature) {
            this.name = name;
            this.resourceMultiplier = resourceMultiplier;
            this.baseTemperature = baseTemperature;
        }
        
        public String getName() { return name; }
        public double getResourceMultiplier() { return resourceMultiplier; }
        public double getBaseTemperature() { return baseTemperature; }
    }
    
    private Biome biomeType;
    
    public Environment(Biome biome) {
        this.biomeType = biome;
        this.biome = biome.getName();
        this.maxResources = 1000 * biome.getResourceMultiplier();
        this.resources = maxResources;
        this.resourceGrowthRate = 10 * biome.getResourceMultiplier();
        this.temperature = biome.getBaseTemperature();
        this.hazardLevel = 0;
    }
    
    /**
     * Update the environment state each turn
     */
    public void update() {
        // Regenerate resources
        resources = Math.min(maxResources, resources + resourceGrowthRate);
        
        // Random temperature fluctuations
        temperature += (RANDOM.nextDouble() - 0.5) * 5;
        temperature = Math.max(0, Math.min(100, temperature));
        
        // Random hazards
        if (RANDOM.nextDouble() < 0.05) {
            hazardLevel = RANDOM.nextDouble() * 50;
        } else {
            hazardLevel = Math.max(0, hazardLevel - 5);
        }
    }
    
    /**
     * Consume resources from the environment
     */
    public double consumeResource(double amount) {
        double consumed = Math.min(amount, resources);
        resources -= consumed;
        return consumed;
    }
    
    /**
     * Check if creature can survive in current conditions
     */
    public boolean isSurvivable(Creature creature) {
        // Extreme temperatures or high hazards are dangerous
        double optimalTemp = 50;
        double tempDiff = Math.abs(temperature - optimalTemp);
        
        // Creatures with higher intelligence adapt better
        double adaptability = creature.getIntelligence();
        
        return tempDiff < (30 + adaptability / 2.0) && hazardLevel < (50 + adaptability / 2.0);
    }
    
    /**
     * Apply environmental pressure to creature
     */
    public void applyEnvironmentalPressure(Creature creature) {
        if (!creature.isAlive()) return;
        
        if (!isSurvivable(creature)) {
            // Harsh conditions drain energy faster
            double energyLoss = (hazardLevel / 10.0) + Math.abs(temperature - 50) / 10.0;
            creature.move(); // Additional energy cost
        }
    }
    
    public double getResources() { return resources; }
    public double getMaxResources() { return maxResources; }
    public double getResourceGrowthRate() { return resourceGrowthRate; }
    public double getTemperature() { return temperature; }
    public double getHazardLevel() { return hazardLevel; }
    public String getBiome() { return biome; }
    public Biome getBiomeType() { return biomeType; }
}
