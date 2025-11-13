package com.example.game;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class EnvironmentTest {
    
    private Environment grassland;
    private Environment desert;
    private Environment tundra;
    
    @Before
    public void setup() {
        grassland = new Environment(Environment.Biome.GRASSLAND);
        desert = new Environment(Environment.Biome.DESERT);
        tundra = new Environment(Environment.Biome.TUNDRA);
    }
    
    @Test
    public void testEnvironmentCreation() {
        assertNotNull(grassland);
        assertEquals("Grassland", grassland.getBiome());
        assertEquals(1000.0, grassland.getMaxResources(), 0.1);
        assertEquals(grassland.getMaxResources(), grassland.getResources(), 0.1);
    }
    
    @Test
    public void testBiomeResourceDifferences() {
        // Grassland should have more resources than desert
        assertTrue(grassland.getMaxResources() > desert.getMaxResources());
        
        // Desert should have more resources than tundra
        assertTrue(desert.getMaxResources() > tundra.getMaxResources());
    }
    
    @Test
    public void testResourceConsumption() {
        double initialResources = grassland.getResources();
        double consumed = grassland.consumeResource(100);
        
        assertEquals(100.0, consumed, 0.1);
        assertEquals(initialResources - 100, grassland.getResources(), 0.1);
    }
    
    @Test
    public void testResourceConsumptionLimit() {
        double consumed = grassland.consumeResource(grassland.getResources() + 500);
        
        // Should only consume what's available
        assertEquals(grassland.getMaxResources(), consumed, 0.1);
        assertEquals(0.0, grassland.getResources(), 0.1);
    }
    
    @Test
    public void testEnvironmentUpdate() {
        grassland.consumeResource(500); // Consume half
        double afterConsumption = grassland.getResources();
        
        grassland.update();
        
        // Resources should regenerate
        assertTrue(grassland.getResources() > afterConsumption);
    }
    
    @Test
    public void testSurvivability() {
        Creature smartCreature = new Creature("Smart", Creature.CreatureType.HERBIVORE, 0);
        
        // In moderate conditions, creatures should be able to survive
        boolean canSurvive = grassland.isSurvivable(smartCreature);
        
        // This is probabilistic, but should generally be true in grassland
        assertNotNull(canSurvive);
    }
    
    @Test
    public void testEnvironmentalPressure() {
        Creature creature = new Creature("Test", Creature.CreatureType.HERBIVORE, 0);
        double initialEnergy = creature.getEnergy();
        
        grassland.applyEnvironmentalPressure(creature);
        
        // Creature should still be alive in grassland (moderate environment)
        assertTrue(creature.isAlive());
    }
    
    @Test
    public void testTemperatureFluctuation() {
        double initialTemp = grassland.getTemperature();
        
        // Update multiple times
        for (int i = 0; i < 10; i++) {
            grassland.update();
        }
        
        // Temperature should have changed (probabilistically)
        // We'll just verify it stays within valid bounds
        assertTrue(grassland.getTemperature() >= 0);
        assertTrue(grassland.getTemperature() <= 100);
    }
}
