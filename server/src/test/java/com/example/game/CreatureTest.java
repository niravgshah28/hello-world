package com.example.game;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CreatureTest {
    
    private Creature herbivore;
    private Creature carnivore;
    private Creature omnivore;
    private Environment environment;
    
    @Before
    public void setup() {
        herbivore = new Creature("TestHerbivore", Creature.CreatureType.HERBIVORE, 0);
        carnivore = new Creature("TestCarnivore", Creature.CreatureType.CARNIVORE, 0);
        omnivore = new Creature("TestOmnivore", Creature.CreatureType.OMNIVORE, 0);
        environment = new Environment(Environment.Biome.GRASSLAND);
    }
    
    @Test
    public void testCreatureCreation() {
        assertNotNull(herbivore);
        assertEquals(Creature.CreatureType.HERBIVORE, herbivore.getType());
        assertTrue(herbivore.isAlive());
        assertTrue(herbivore.getSpeed() >= 30 && herbivore.getSpeed() <= 70);
        assertTrue(herbivore.getStrength() >= 30 && herbivore.getStrength() <= 70);
        assertTrue(herbivore.getIntelligence() >= 30 && herbivore.getIntelligence() <= 70);
        assertTrue(herbivore.getSize() >= 30 && herbivore.getSize() <= 70);
        assertEquals(herbivore.getMaxEnergy(), herbivore.getEnergy(), 0.1);
    }
    
    @Test
    public void testReproduction() {
        Creature partner = new Creature("Partner", Creature.CreatureType.HERBIVORE, 0);
        Creature child = herbivore.reproduce(partner);
        
        assertNotNull(child);
        assertEquals(Creature.CreatureType.HERBIVORE, child.getType());
        assertEquals(1, child.getGeneration());
        assertTrue(child.isAlive());
        
        // Energy should be reduced after reproduction
        assertTrue(herbivore.getEnergy() < herbivore.getMaxEnergy());
        assertTrue(partner.getEnergy() < partner.getMaxEnergy());
    }
    
    @Test
    public void testReproductionLowEnergy() {
        // Drain energy
        while (herbivore.getEnergy() > herbivore.getMaxEnergy() * 0.4) {
            herbivore.move();
        }
        
        Creature partner = new Creature("Partner", Creature.CreatureType.HERBIVORE, 0);
        Creature child = herbivore.reproduce(partner);
        
        // Should fail due to low energy
        assertNull(child);
    }
    
    @Test
    public void testHerbivoreCannotHunt() {
        Creature prey = new Creature("Prey", Creature.CreatureType.HERBIVORE, 0);
        boolean huntSuccess = herbivore.hunt(prey);
        
        assertFalse(huntSuccess);
        assertTrue(prey.isAlive());
    }
    
    @Test
    public void testCarnivoreHunting() {
        Creature prey = new Creature("Prey", Creature.CreatureType.HERBIVORE, 0);
        double initialEnergy = carnivore.getEnergy();
        
        // Try hunting multiple times (it's probabilistic)
        boolean huntedAtLeastOnce = false;
        for (int i = 0; i < 100; i++) {
            Creature testPrey = new Creature("TestPrey", Creature.CreatureType.HERBIVORE, 0);
            if (carnivore.hunt(testPrey)) {
                huntedAtLeastOnce = true;
                assertFalse(testPrey.isAlive());
                break;
            }
        }
        
        // Should succeed at least once in 100 attempts
        assertTrue(huntedAtLeastOnce);
    }
    
    @Test
    public void testForaging() {
        double initialEnergy = herbivore.getEnergy();
        herbivore.move(); // Reduce energy first
        double afterMove = herbivore.getEnergy();
        
        herbivore.forage(environment);
        
        // Energy should increase after foraging (if resources available)
        assertTrue(herbivore.getEnergy() >= afterMove);
    }
    
    @Test
    public void testCarnivoreCannotForage() {
        double initialEnergy = carnivore.getEnergy();
        carnivore.move();
        double afterMove = carnivore.getEnergy();
        
        carnivore.forage(environment);
        
        // Carnivores can't forage, so energy should stay the same
        assertEquals(afterMove, carnivore.getEnergy(), 0.1);
    }
    
    @Test
    public void testMovement() {
        double initialEnergy = herbivore.getEnergy();
        herbivore.move();
        
        // Movement should consume energy
        assertTrue(herbivore.getEnergy() < initialEnergy);
    }
    
    @Test
    public void testRest() {
        herbivore.move(); // Reduce energy first
        double afterMove = herbivore.getEnergy();
        herbivore.rest();
        
        // Rest should recover some energy
        assertTrue(herbivore.getEnergy() >= afterMove);
    }
    
    @Test
    public void testDeath() {
        herbivore.die();
        
        assertFalse(herbivore.isAlive());
        assertEquals(0.0, herbivore.getEnergy(), 0.1);
    }
    
    @Test
    public void testDeathByStarvation() {
        // Drain all energy
        while (herbivore.isAlive() && herbivore.getEnergy() > 0) {
            herbivore.move();
        }
        
        assertFalse(herbivore.isAlive());
    }
    
    @Test
    public void testFitnessCalculation() {
        double fitness = herbivore.getFitness();
        double expectedFitness = (herbivore.getSpeed() + herbivore.getStrength() + 
                                  herbivore.getIntelligence() + herbivore.getSize()) / 4.0;
        
        assertEquals(expectedFitness, fitness, 0.1);
    }
    
    @Test
    public void testDecideAction() {
        Creature.Action action = herbivore.decideAction(environment, new Creature[0]);
        
        assertNotNull(action);
        // Action should be one of the valid actions
        assertTrue(action == Creature.Action.HUNT || 
                  action == Creature.Action.FORAGE || 
                  action == Creature.Action.MOVE || 
                  action == Creature.Action.REST || 
                  action == Creature.Action.REPRODUCE);
    }
}
