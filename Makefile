all: build/GWar.class
build/GWar.class: 
	javac -d build -cp build *.java
run:
	java -cp build GWar
