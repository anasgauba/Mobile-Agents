This program, first reads in a map. The default map is in the resources. Then it creates a map based on those locations using the 
node and Basestation class. Then, it creates an agent and puts it in the basestation. The agent will move in the graph untill it finds a yellow node.
Note that the node on fire is red and the ones beside those on fire are yellow. After 
finding the yellow node, the agent will stay there and clone itself to neighbor nodes that are either blue or yellow.
The yellow nodes die every three seconds and the agents clone themselves to keep the property.
Whenever an agent is created it will send its id and location to the basestation. The id and location will be 
printed in the TERMINAL.
To use the program just run the MobileAgents.java (Or the jar file). Note that the location and id of the agents are printed in the terminal.
The green node is basestation. The blue nodes are the normal nodes. The yellow nodes are the ones beside fire.
The red nodes are the ones that are on fire. The purple border on the nodes is the agent.

Wo did which part?
It is hard to explain this because many of the functions were created with the contribution from both of us. The functions written by both of use are:
In BaseStation:
start()
Agent()
We also thought about the design diagram as well.
Farhang:
I did the basic design and layout of the program and what the functions would be.
I wrote all the functions in the Node, except for the cloneAgent that we wrote together.

Anas:
Anas, In addition to the functions that we wrote together, wrote the design diagram.
He also did most of the testing. He found errors and we tried to fix the error together.
