
This is a set of basic commands to compile and execute the code:

Linux & Mac:
- To compile: javac -cp .:jade.jar *.java
- To execute: java -cp .:jade.jar jade.Boot -notmp -gui -agents "MainAgent:MainAgent;RandomAgent:RandomAgent;"


Windows (do not use the PowerShell):
- To compile: javac -cp .;jade.jar *.java
- To execute: java -cp .;jade.jar jade.Boot -notmp -gui -agents "MainAgent:MainAgent;RandomAgent:RandomAgent;"


* Note that you need to have the "jade.jar" library in the present folder