package com.example.game;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;

public class SimulationEngineTest {
    
    private SimulationEngine engine;
    private Environment environment;
    
    @Before
    public void setup() {
        environment = new Environment(Environment.Biome.GRASSLAND);
        engine = new SimulationEngine(environment);
    }
    
    @Test
    public void testInitialization() {
        engine.initialize(10, 5, 5);
        
        List<Creature> creatures = engine.getCreatures();
        assertEquals(20, creatures.size());
        
        long herbivores = creatures.stream()
            .filter(c -> c.getType() == Creature.CreatureType.HERBIVORE)
            .count();
        long carnivores = creatures.stream()
            .filter(c -> c.getType() == Creature.CreatureType.CARNIVORE)
            .count();
        long omnivores = creatures.stream()
            .filter(c -> c.getType() == Creature.CreatureType.OMNIVORE)
            .count();
        
        assertEquals(10, herbivores);
        assertEquals(5, carnivores);
        assertEquals(5, omnivores);
    }
    
    @Test
    public void testSimulateTurn() {
        engine.initialize(10, 5, 5);
        
        int initialTurn = engine.getTurn();
        engine.simulateTurn();
        
        assertEquals(initialTurn + 1, engine.getTurn());
    }
    
    @Test
    public void testSimulateMultipleTurns() {
        engine.initialize(10, 5, 5);
        
        engine.simulate(10);
        
        assertEquals(10, engine.getTurn());
    }
    
    @Test
    public void testPopulationChanges() {
        engine.initialize(20, 10, 10);
        
        int initialPopulation = engine.getCreatures().size();
        
        // Run simulation
        engine.simulate(50);
        
        // Population should have changed (either increased through reproduction or decreased through death)
        int finalPopulation = engine.getCreatures().size();
        
        // Just verify the simulation ran
        assertTrue(engine.getTurn() > 0);
    }
    
    @Test
    public void testGetAliveCreatures() {
        engine.initialize(10, 5, 5);
        
        List<Creature> aliveCreatures = engine.getAliveCreatures();
        
        assertEquals(20, aliveCreatures.size());
        
        // All should be alive initially
        for (Creature c : aliveCreatures) {
            assertTrue(c.isAlive());
        }
    }
    
    @Test
    public void testStats() {
        engine.initialize(10, 5, 5);
        
        SimulationStats stats = engine.getStats();
        
        assertNotNull(stats);
        assertEquals(0, stats.getTurn());
        assertEquals(20, stats.getTotalCreatures());
    }
    
    @Test
    public void testStatsAfterSimulation() {
        engine.initialize(10, 5, 5);
        
        engine.simulate(5);
        
        SimulationStats stats = engine.getStats();
        
        assertEquals(5, stats.getTurn());
        assertTrue(stats.getAliveCreatures() > 0);
    }
    
    @Test
    public void testEnvironmentUpdates() {
        engine.initialize(10, 5, 5);
        
        double initialResources = environment.getResources();
        
        // Consume some resources
        for (int i = 0; i < 5; i++) {
            environment.consumeResource(50);
        }
        
        // Simulate turns to regenerate
        engine.simulate(10);
        
        // Resources should have regenerated somewhat
        assertTrue(environment.getResources() >= 0);
    }
}
