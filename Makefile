all: build/GWar.class
build/GWar.class: 
	javac -d build -cp build *.java
run:
	cd build; java GWar
clean:
	rm build/*.class
