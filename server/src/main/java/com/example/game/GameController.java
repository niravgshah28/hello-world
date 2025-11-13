package com.example.game;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller for managing the evolution game simulation.
 */
public class GameController {
    private SimulationEngine engine;
    private boolean isRunning;
    
    public GameController() {
        this.isRunning = false;
    }
    
    /**
     * Start a new simulation
     */
    public String startNewSimulation(String biomeType, int herbivores, int carnivores, int omnivores) {
        Environment.Biome biome;
        try {
            biome = Environment.Biome.valueOf(biomeType.toUpperCase());
        } catch (IllegalArgumentException e) {
            biome = Environment.Biome.GRASSLAND;
        }
        
        Environment environment = new Environment(biome);
        engine = new SimulationEngine(environment);
        engine.initialize(herbivores, carnivores, omnivores);
        isRunning = true;
        
        return "Simulation started in " + biome.getName() + " with " + 
               (herbivores + carnivores + omnivores) + " creatures";
    }
    
    /**
     * Run simulation for specified number of turns
     */
    public String runTurns(int turns) {
        if (engine == null || !isRunning) {
            return "No active simulation. Please start a new simulation first.";
        }
        
        engine.simulate(turns);
        
        if (engine.getAliveCreatures().isEmpty()) {
            isRunning = false;
            return "Simulation ended - all creatures extinct! " + engine.getStats().toString();
        }
        
        return engine.getStats().toString();
    }
    
    /**
     * Get current simulation state as HTML
     */
    public String getSimulationHTML() {
        if (engine == null) {
            return "<div class='alert'>No simulation running. Start a new simulation to begin!</div>";
        }
        
        StringBuilder html = new StringBuilder();
        SimulationStats stats = engine.getStats();
        Environment env = engine.getEnvironment();
        
        // Stats panel
        html.append("<div class='stats-panel'>");
        html.append("<h2>Simulation Statistics</h2>");
        html.append("<div class='stat-row'><span class='label'>Turn:</span><span class='value'>").append(stats.getTurn()).append("</span></div>");
        html.append("<div class='stat-row'><span class='label'>Alive Creatures:</span><span class='value'>").append(stats.getAliveCreatures()).append(" / ").append(stats.getTotalCreatures()).append("</span></div>");
        html.append("<div class='stat-row'><span class='label'>Herbivores:</span><span class='value herbivore'>").append(stats.getHerbivoreCount()).append("</span></div>");
        html.append("<div class='stat-row'><span class='label'>Carnivores:</span><span class='value carnivore'>").append(stats.getCarnivoreCount()).append("</span></div>");
        html.append("<div class='stat-row'><span class='label'>Omnivores:</span><span class='value omnivore'>").append(stats.getOmnivoreCount()).append("</span></div>");
        html.append("<div class='stat-row'><span class='label'>Total Births:</span><span class='value'>").append(stats.getBirths()).append("</span></div>");
        html.append("<div class='stat-row'><span class='label'>Total Deaths:</span><span class='value'>").append(stats.getDeaths()).append("</span></div>");
        html.append("<div class='stat-row'><span class='label'>Avg Fitness:</span><span class='value'>").append(String.format("%.2f", stats.getAverageFitness())).append("</span></div>");
        html.append("<div class='stat-row'><span class='label'>Max Generation:</span><span class='value'>").append(stats.getMaxGeneration()).append("</span></div>");
        html.append("</div>");
        
        // Environment panel
        html.append("<div class='env-panel'>");
        html.append("<h2>Environment</h2>");
        html.append("<div class='stat-row'><span class='label'>Biome:</span><span class='value'>").append(env.getBiome()).append("</span></div>");
        html.append("<div class='stat-row'><span class='label'>Resources:</span><span class='value'>");
        html.append(String.format("%.1f / %.1f", env.getResources(), env.getMaxResources()));
        html.append(" <div class='progress-bar'><div class='progress-fill' style='width: ").append((env.getResources() / env.getMaxResources() * 100)).append("%'></div></div>");
        html.append("</span></div>");
        html.append("<div class='stat-row'><span class='label'>Temperature:</span><span class='value'>").append(String.format("%.1f°C", env.getTemperature())).append("</span></div>");
        html.append("<div class='stat-row'><span class='label'>Hazard Level:</span><span class='value'>").append(String.format("%.1f%%", env.getHazardLevel())).append("</span></div>");
        html.append("</div>");
        
        // Top creatures
        html.append("<div class='creatures-panel'>");
        html.append("<h2>Top 10 Fittest Creatures</h2>");
        html.append("<table class='creature-table'>");
        html.append("<tr><th>Name</th><th>Type</th><th>Gen</th><th>Speed</th><th>Strength</th><th>Intelligence</th><th>Size</th><th>Energy</th><th>Fitness</th></tr>");
        
        List<Creature> topCreatures = engine.getAliveCreatures().stream()
            .sorted((c1, c2) -> Double.compare(c2.getFitness(), c1.getFitness()))
            .limit(10)
            .collect(Collectors.toList());
        
        for (Creature c : topCreatures) {
            String typeClass = c.getType().name().toLowerCase();
            html.append("<tr class='").append(typeClass).append("'>");
            html.append("<td>").append(c.getName()).append("</td>");
            html.append("<td>").append(c.getType()).append("</td>");
            html.append("<td>").append(c.getGeneration()).append("</td>");
            html.append("<td>").append(String.format("%.1f", c.getSpeed())).append("</td>");
            html.append("<td>").append(String.format("%.1f", c.getStrength())).append("</td>");
            html.append("<td>").append(String.format("%.1f", c.getIntelligence())).append("</td>");
            html.append("<td>").append(String.format("%.1f", c.getSize())).append("</td>");
            html.append("<td>").append(String.format("%.1f/%.1f", c.getEnergy(), c.getMaxEnergy())).append("</td>");
            html.append("<td>").append(String.format("%.2f", c.getFitness())).append("</td>");
            html.append("</tr>");
        }
        
        html.append("</table>");
        html.append("</div>");
        
        return html.toString();
    }
    
    public boolean isRunning() {
        return isRunning;
    }
    
    public SimulationEngine getEngine() {
        return engine;
    }
}
