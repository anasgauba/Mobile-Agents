Mobile Agents App: 
- This program, first reads in a map. The default map is in the resources. 
- Then it creates a map based on those locations using the 
node and Basestation class. Then, it creates an agent and puts it in the basestation. The agent will move in the graph untill it finds a yellow node.
Note that the node on fire is red and the ones beside those on fire are yellow. After 
finding the yellow node, the agent will stay there and clone itself to neighbor nodes that are either blue or yellow.
The yellow nodes die every three seconds and the agents clone themselves to keep the property.
Whenever an agent is created it will send its id and location to the basestation. The id and location will be 
printed in the TERMINAL.
- To use the program just run the MobileAgents.java (Or the jar file). Note 
that the location and id of the agents are printed in the terminal.
The green node is basestation. The blue nodes are the normal nodes. The yellow nodes are the ones beside fire.
The red nodes are the ones that are on fire. The purple border on the nodes is the agent.

Wo did which part?
- It is hard to explain this because many of the functions were created with 
the contribution from both of us. The functions written by both of us are:
MobileAgents.java and Agent.java. We also designed the diagram doc as well.
Farhang:
-I did the basic design and layout of the program and what the functions 
would be.
-I wrote all the functions in the Node, except for the cloneAgent, passAgent 
and recieveAgent that we wrote together.
And I wrote BaseStation.java.

Anas:
-In addition to the functions that we wrote together, I did the design 
diagram doc, worked on some functions in node, MobileAgents, and Agent.
-I also did most of the testing. I found errors and we tried to fix the error
 together.

NOTE:
This program will first create one agent in the base station. This agent will walk through the 
graph until it finds a yellow node. Then it will clone itself to its yellow and blue neighbors.
This is EXACTLY as was described in the class and assignment.
If there are other yellow nodes that the node doesn't have access to them (They are not 
its neighbors), then it won't (obviously) clone itself to that node, and we will end up with some
yellow nodes that don't have agents. Note that this is NOT an error based on the specification. According to
the assignment details, this is the exact thing that should be done and this will not be able to fully handle
cases that yellow nodes don't have any edges to other yellow nodes at the start of the program.
 We could fix this problem easily by doing more than one walk,
but this would violate the program specification. The code perfectly runs on any graph that doesn't 
have this issue (like the provided sample graph and the one in the lecture).