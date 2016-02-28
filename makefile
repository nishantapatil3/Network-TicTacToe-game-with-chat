all:
	javac TTTServer.java
	javac NetworkTTT.java

clean:
	rm TTTServer.class
	rm NetworkTTT.class
	rm NetworkTTTFrame.class
	rm NewGameHandler.class
