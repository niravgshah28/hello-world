package com.example.game;

/**
 * Standalone demo program to showcase the Evolution Simulation Game
 */
public class GameDemo {
    
    public static void main(String[] args) {
        System.out.println("=".repeat(80));
        System.out.println("EVOLUTION SIMULATION GAME - Generative AI Creature Evolution");
        System.out.println("=".repeat(80));
        System.out.println();
        
        // Create environment
        System.out.println("🌍 Creating a Grassland environment...");
        Environment environment = new Environment(Environment.Biome.GRASSLAND);
        
        // Create simulation engine
        SimulationEngine engine = new SimulationEngine(environment);
        
        // Initialize with creatures
        System.out.println("🧬 Initializing population: 20 Herbivores, 10 Carnivores, 10 Omnivores");
        engine.initialize(20, 10, 10);
        
        System.out.println();
        System.out.println("Starting simulation...");
        System.out.println("-".repeat(80));
        
        // Run simulation for 100 turns
        for (int turn = 1; turn <= 100; turn++) {
            engine.simulateTurn();
            
            // Print stats every 10 turns
            if (turn % 10 == 0) {
                SimulationStats stats = engine.getStats();
                System.out.println(stats);
                
                // Stop if all creatures died
                if (stats.getAliveCreatures() == 0) {
                    System.out.println();
                    System.out.println("❌ Simulation ended - all creatures extinct!");
                    break;
                }
            }
        }
        
        System.out.println();
        System.out.println("-".repeat(80));
        System.out.println("FINAL RESULTS:");
        System.out.println("-".repeat(80));
        
        SimulationStats finalStats = engine.getStats();
        System.out.println(finalStats);
        
        System.out.println();
        System.out.println("Environment Status:");
        System.out.printf("  Resources: %.1f / %.1f (%.1f%%)%n", 
            environment.getResources(), 
            environment.getMaxResources(),
            (environment.getResources() / environment.getMaxResources() * 100));
        System.out.printf("  Temperature: %.1f°C%n", environment.getTemperature());
        System.out.printf("  Hazard Level: %.1f%%%n", environment.getHazardLevel());
        
        System.out.println();
        System.out.println("Top 5 Fittest Creatures:");
        System.out.println("-".repeat(80));
        System.out.printf("%-20s %-12s %-5s %-7s %-9s %-13s %-6s %-10s%n",
            "Name", "Type", "Gen", "Speed", "Strength", "Intelligence", "Size", "Fitness");
        System.out.println("-".repeat(80));
        
        engine.getAliveCreatures().stream()
            .sorted((c1, c2) -> Double.compare(c2.getFitness(), c1.getFitness()))
            .limit(5)
            .forEach(c -> {
                System.out.printf("%-20s %-12s %-5d %-7.1f %-9.1f %-13.1f %-6.1f %-10.2f%n",
                    c.getName(), 
                    c.getType(), 
                    c.getGeneration(),
                    c.getSpeed(),
                    c.getStrength(),
                    c.getIntelligence(),
                    c.getSize(),
                    c.getFitness());
            });
        
        System.out.println();
        System.out.println("=".repeat(80));
        System.out.println("Key Features Demonstrated:");
        System.out.println("  ✓ Generative AI: Creatures evolve with inherited + mutated traits");
        System.out.println("  ✓ Natural Selection: Fittest creatures survive and reproduce");
        System.out.println("  ✓ Predator-Prey: Carnivores hunt, herbivores forage");
        System.out.println("  ✓ Environmental Pressures: Resources, temperature, hazards");
        System.out.println("  ✓ Multi-generational Evolution: Track progress across generations");
        System.out.println("=".repeat(80));
    }
}
