all: build/GWar.class
build/GWar.class: 
	javac -d build -cp build *.java
run: build/GWar.class
	cd build; java GWar
clean:
	rm -v build/*.class
