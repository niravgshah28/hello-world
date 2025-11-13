# Evolution Simulation Game - Generative AI Creature Evolution

## Overview

This project implements a sophisticated **creature evolution and survival simulation game** powered by generative AI algorithms. Watch as creatures evolve, compete, hunt, and reproduce in a dynamic ecosystem with realistic environmental pressures.

## Features

### 🧬 Generative AI & Evolution
- **Genetic Traits**: Each creature has 4 core traits (Speed, Strength, Intelligence, Size)
- **Inheritance & Mutation**: Children inherit traits from parents with random mutations
- **Natural Selection**: Only the fittest creatures survive to reproduce
- **Multi-generational Evolution**: Track evolution across dozens of generations

### 🎮 Game Mechanics
- **Creature Types**:
  - **Herbivores**: Forage for plants, flee from predators
  - **Carnivores**: Hunt other creatures for food
  - **Omnivores**: Can eat both plants and creatures

- **Intelligent Behavior**:
  - AI-driven decision making based on creature traits
  - Dynamic actions: hunting, foraging, resting, reproducing
  - Energy management and survival instincts

### 🌍 Complex Environment
- **Multiple Biomes**:
  - Grassland (Balanced)
  - Forest (Rich Resources)
  - Desert (Harsh Conditions)
  - Tundra (Very Harsh)
  - Jungle (Abundant Resources)

- **Environmental Pressures**:
  - Resource scarcity and competition
  - Temperature fluctuations
  - Random hazards and disasters
  - Dynamic resource regeneration

### 🎯 Predator-Prey Relationships
- Realistic hunting mechanics based on creature stats
- Escape chances influenced by speed and intelligence
- Energy costs for failed hunts
- Food chain dynamics

## How to Build and Run

### Prerequisites
- Java 8 or higher
- Maven 3.0+

### Build
```bash
mvn clean package
```

### Run Tests
```bash
mvn test
```

### Deploy
```bash
# Deploy the WAR file to a servlet container (Tomcat, Jetty, etc.)
mvn jetty:run
```

Then open your browser to `http://localhost:8080/webapp`

## How to Play

1. **Start a New Simulation**:
   - Choose a biome (Grassland, Forest, Desert, etc.)
   - Set initial population (Herbivores, Carnivores, Omnivores)
   - Click "Start New Simulation"

2. **Run the Simulation**:
   - Choose number of turns to simulate
   - Click "Run Simulation"
   - Watch statistics and see which creatures survive!

3. **Analyze Results**:
   - View population trends
   - Track average fitness over time
   - See which generations are most successful
   - Monitor resource levels and environmental conditions

## Game Statistics Tracked

- **Population**: Total creatures, alive vs dead
- **Species Distribution**: Herbivore, Carnivore, Omnivore counts
- **Births & Deaths**: Track population changes
- **Average Fitness**: See evolution in action
- **Max Generation**: How far has evolution progressed?
- **Environment**: Resources, temperature, hazards
- **Top Creatures**: View the fittest creatures and their traits

## Technical Implementation

### Core Classes

- **Creature**: Represents individual creatures with genetic traits and behaviors
- **Environment**: Manages resources, temperature, and environmental pressures
- **SimulationEngine**: Orchestrates the game loop and creature interactions
- **GameController**: Handles web interface and user interactions
- **SimulationStats**: Tracks and reports simulation metrics

### Testing

Comprehensive test coverage with 31+ unit tests covering:
- Creature behavior (hunting, foraging, reproduction)
- Environment dynamics
- Simulation mechanics
- Statistical tracking

## Educational Value

This game demonstrates key concepts in:
- **Evolutionary Biology**: Natural selection, genetic drift, adaptation
- **Artificial Intelligence**: Decision trees, behavior modeling
- **Complex Systems**: Emergent behavior, ecosystem dynamics
- **Game Theory**: Resource competition, predator-prey dynamics

## Future Enhancements

Potential features for expansion:
- Visual creature representation
- Advanced trait systems (camouflage, speed bursts)
- Migration between biomes
- Disease and immunity systems
- User-designed creatures
- Replay and analysis tools

## Course Reference

Find complete course details here:

[![Image](https://github.com/yankils/Simple-DevOps-Project/blob/master/Devops_course.PNG "DevOps Project - CI/CD with Jenkins Ansible Docker Kubernetes ")](https://www.udemy.com/course/valaxy-devops/?referralCode=8147A5CF4C8C7D9E253F)

