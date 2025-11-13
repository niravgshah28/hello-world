<%@ page import="com.example.game.*" %>
<%
    GameController controller = (GameController) application.getAttribute("gameController");
    if (controller == null) {
        controller = new GameController();
        application.setAttribute("gameController", controller);
    }
    
    String action = request.getParameter("action");
    String message = "";
    
    if ("start".equals(action)) {
        String biome = request.getParameter("biome");
        int herbivores = Integer.parseInt(request.getParameter("herbivores"));
        int carnivores = Integer.parseInt(request.getParameter("carnivores"));
        int omnivores = Integer.parseInt(request.getParameter("omnivores"));
        message = controller.startNewSimulation(biome, herbivores, carnivores, omnivores);
    } else if ("simulate".equals(action)) {
        int turns = Integer.parseInt(request.getParameter("turns"));
        message = controller.runTurns(turns);
    }
%>
<!DOCTYPE html>
<html>
<head>
    <title>Evolution Simulation Game - Generative AI Creature Evolution</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: #333;
            min-height: 100vh;
            padding: 20px;
        }
        
        .container {
            max-width: 1400px;
            margin: 0 auto;
            background: white;
            border-radius: 15px;
            box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
            overflow: hidden;
        }
        
        header {
            background: linear-gradient(135deg, #2c3e50 0%, #34495e 100%);
            color: white;
            padding: 30px;
            text-align: center;
        }
        
        header h1 {
            font-size: 2.5em;
            margin-bottom: 10px;
            text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.3);
        }
        
        header p {
            font-size: 1.1em;
            opacity: 0.9;
        }
        
        .message {
            background: #4CAF50;
            color: white;
            padding: 15px;
            margin: 20px;
            border-radius: 5px;
            text-align: center;
            font-weight: bold;
        }
        
        .controls {
            background: #f5f5f5;
            padding: 25px;
            margin: 20px;
            border-radius: 10px;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
        }
        
        .control-section {
            margin-bottom: 20px;
        }
        
        .control-section h3 {
            margin-bottom: 15px;
            color: #2c3e50;
            border-bottom: 2px solid #667eea;
            padding-bottom: 10px;
        }
        
        .form-group {
            margin: 10px 0;
            display: inline-block;
            margin-right: 15px;
        }
        
        label {
            display: inline-block;
            width: 120px;
            font-weight: bold;
            color: #555;
        }
        
        select, input[type="number"] {
            padding: 8px 12px;
            border: 2px solid #ddd;
            border-radius: 5px;
            font-size: 14px;
            width: 150px;
            transition: border-color 0.3s;
        }
        
        select:focus, input[type="number"]:focus {
            outline: none;
            border-color: #667eea;
        }
        
        button {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 12px 30px;
            border: none;
            border-radius: 5px;
            font-size: 16px;
            font-weight: bold;
            cursor: pointer;
            transition: transform 0.2s, box-shadow 0.2s;
            margin: 5px;
        }
        
        button:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(102, 126, 234, 0.4);
        }
        
        button:active {
            transform: translateY(0);
        }
        
        .simulation-display {
            padding: 20px;
        }
        
        .stats-panel, .env-panel, .creatures-panel {
            background: white;
            padding: 20px;
            margin: 20px;
            border-radius: 10px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
        }
        
        .stats-panel h2, .env-panel h2, .creatures-panel h2 {
            color: #2c3e50;
            margin-bottom: 20px;
            font-size: 1.5em;
            border-bottom: 3px solid #667eea;
            padding-bottom: 10px;
        }
        
        .stat-row {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 10px 0;
            border-bottom: 1px solid #eee;
        }
        
        .stat-row:last-child {
            border-bottom: none;
        }
        
        .stat-row .label {
            font-weight: bold;
            color: #555;
        }
        
        .stat-row .value {
            color: #2c3e50;
            font-weight: bold;
        }
        
        .value.herbivore {
            color: #4CAF50;
        }
        
        .value.carnivore {
            color: #f44336;
        }
        
        .value.omnivore {
            color: #FF9800;
        }
        
        .progress-bar {
            width: 200px;
            height: 20px;
            background: #eee;
            border-radius: 10px;
            overflow: hidden;
            display: inline-block;
            margin-left: 10px;
        }
        
        .progress-fill {
            height: 100%;
            background: linear-gradient(90deg, #4CAF50 0%, #8BC34A 100%);
            transition: width 0.3s;
        }
        
        .creature-table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 15px;
        }
        
        .creature-table th {
            background: #2c3e50;
            color: white;
            padding: 12px;
            text-align: left;
            font-weight: bold;
        }
        
        .creature-table td {
            padding: 10px;
            border-bottom: 1px solid #eee;
        }
        
        .creature-table tr.herbivore {
            background: #E8F5E9;
        }
        
        .creature-table tr.carnivore {
            background: #FFEBEE;
        }
        
        .creature-table tr.omnivore {
            background: #FFF3E0;
        }
        
        .creature-table tr:hover {
            background: #f5f5f5;
            cursor: pointer;
        }
        
        .alert {
            background: #ff9800;
            color: white;
            padding: 30px;
            margin: 20px;
            border-radius: 10px;
            text-align: center;
            font-size: 1.2em;
            font-weight: bold;
        }
        
        .info-box {
            background: #e3f2fd;
            border-left: 4px solid #2196F3;
            padding: 15px;
            margin: 20px;
            border-radius: 5px;
        }
        
        .info-box h4 {
            color: #1976D2;
            margin-bottom: 10px;
        }
        
        .info-box ul {
            margin-left: 20px;
        }
        
        .info-box li {
            margin: 5px 0;
            color: #555;
        }
    </style>
</head>
<body>
    <div class="container">
        <header>
            <h1>🧬 Evolution Simulation Game 🎮</h1>
            <p>Generative AI-Powered Creature Evolution & Survival Simulation</p>
        </header>
        
        <% if (!message.isEmpty()) { %>
        <div class="message"><%= message %></div>
        <% } %>
        
        <div class="info-box">
            <h4>About This Game</h4>
            <ul>
                <li><strong>Generative AI Features:</strong> Creatures evolve with genetic traits (speed, strength, intelligence, size) that are inherited and mutated across generations</li>
                <li><strong>Complex Ecosystem:</strong> Predator-prey relationships, resource competition, and environmental pressures</li>
                <li><strong>Natural Selection:</strong> Only the fittest creatures survive and reproduce, leading to evolution over time</li>
                <li><strong>Dynamic Environment:</strong> Multiple biomes with different resources, temperatures, and hazards</li>
                <li><strong>Emergent Behavior:</strong> Watch as creatures make intelligent decisions to hunt, forage, flee, or reproduce based on their traits</li>
            </ul>
        </div>
        
        <div class="controls">
            <div class="control-section">
                <h3>🎮 Start New Simulation</h3>
                <form method="post">
                    <input type="hidden" name="action" value="start">
                    <div class="form-group">
                        <label>Biome:</label>
                        <select name="biome">
                            <option value="GRASSLAND">Grassland (Balanced)</option>
                            <option value="FOREST">Forest (Rich Resources)</option>
                            <option value="DESERT">Desert (Harsh)</option>
                            <option value="TUNDRA">Tundra (Very Harsh)</option>
                            <option value="JUNGLE">Jungle (Abundant)</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <label>Herbivores:</label>
                        <input type="number" name="herbivores" value="20" min="0" max="100">
                    </div>
                    <div class="form-group">
                        <label>Carnivores:</label>
                        <input type="number" name="carnivores" value="10" min="0" max="100">
                    </div>
                    <div class="form-group">
                        <label>Omnivores:</label>
                        <input type="number" name="omnivores" value="10" min="0" max="100">
                    </div>
                    <br>
                    <button type="submit">🚀 Start New Simulation</button>
                </form>
            </div>
            
            <% if (controller.isRunning()) { %>
            <div class="control-section">
                <h3>⏭️ Simulate Turns</h3>
                <form method="post">
                    <input type="hidden" name="action" value="simulate">
                    <div class="form-group">
                        <label>Number of Turns:</label>
                        <input type="number" name="turns" value="10" min="1" max="1000">
                    </div>
                    <button type="submit">▶️ Run Simulation</button>
                </form>
            </div>
            <% } %>
        </div>
        
        <div class="simulation-display">
            <%= controller.getSimulationHTML() %>
        </div>
    </div>
</body>
</html>
