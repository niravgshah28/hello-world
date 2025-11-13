package com.example.game;

/**
 * Statistics for tracking simulation progress and outcomes.
 */
public class SimulationStats {
    private int turn;
    private int totalCreatures;
    private int aliveCreatures;
    private int herbivoreCount;
    private int carnivoreCount;
    private int omnivoreCount;
    private int births;
    private int deaths;
    private double averageFitness;
    private int maxGeneration;
    
    public SimulationStats() {
        reset();
    }
    
    public void reset() {
        this.turn = 0;
        this.totalCreatures = 0;
        this.aliveCreatures = 0;
        this.herbivoreCount = 0;
        this.carnivoreCount = 0;
        this.omnivoreCount = 0;
        this.births = 0;
        this.deaths = 0;
        this.averageFitness = 0.0;
        this.maxGeneration = 0;
    }
    
    public void incrementBirths() {
        births++;
    }
    
    public void incrementDeaths() {
        deaths++;
    }
    
    // Getters and Setters
    public int getTurn() { return turn; }
    public void setTurn(int turn) { this.turn = turn; }
    
    public int getTotalCreatures() { return totalCreatures; }
    public void setTotalCreatures(int totalCreatures) { this.totalCreatures = totalCreatures; }
    
    public int getAliveCreatures() { return aliveCreatures; }
    public void setAliveCreatures(int aliveCreatures) { this.aliveCreatures = aliveCreatures; }
    
    public int getHerbivoreCount() { return herbivoreCount; }
    public void setHerbivoreCount(int herbivoreCount) { this.herbivoreCount = herbivoreCount; }
    
    public int getCarnivoreCount() { return carnivoreCount; }
    public void setCarnivoreCount(int carnivoreCount) { this.carnivoreCount = carnivoreCount; }
    
    public int getOmnivoreCount() { return omnivoreCount; }
    public void setOmnivoreCount(int omnivoreCount) { this.omnivoreCount = omnivoreCount; }
    
    public int getBirths() { return births; }
    public void setBirths(int births) { this.births = births; }
    
    public int getDeaths() { return deaths; }
    public void setDeaths(int deaths) { this.deaths = deaths; }
    
    public double getAverageFitness() { return averageFitness; }
    public void setAverageFitness(double averageFitness) { this.averageFitness = averageFitness; }
    
    public int getMaxGeneration() { return maxGeneration; }
    public void setMaxGeneration(int maxGeneration) { this.maxGeneration = maxGeneration; }
    
    @Override
    public String toString() {
        return String.format(
            "Turn: %d | Alive: %d/%d | H:%d C:%d O:%d | Births:%d Deaths:%d | Avg Fitness:%.2f | Max Gen:%d",
            turn, aliveCreatures, totalCreatures, herbivoreCount, carnivoreCount, omnivoreCount,
            births, deaths, averageFitness, maxGeneration
        );
    }
}
