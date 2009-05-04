default: gwar
gwar: bumper.o display.o gwar.o hero.o item.o
	g++ -lSDL -lSDL_image bumper.o display.o gwar.o hero.o item.o -o gwar
hero.o: hero.cpp
	g++ -c hero.cpp
display.o: display.cpp
	g++ -c display.cpp
item.o: item.cpp
	g++ -c item.cpp
bumper.o: bumper.cpp
	g++ -c bumper.cpp
gwar.o: gwar.cpp
	g++ -c gwar.cpp
run: gwar
	./gwar
clean:
	rm gwar *.o
