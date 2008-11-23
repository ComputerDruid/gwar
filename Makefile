JFLAGS = -g -d build -cp build
JC = javac
.SUFFIXES: .java .class
.java.class:
	        $(JC) $(JFLAGS) $*.java

CLASSES = \
	GWar.java \
	Hero.java \
	Bumper.java \
	SpeedBumper.java \
	Item.java \
	MainMenu.java

default: classes

classes: $(CLASSES:.java=.class)

init:
	$(JC) $(JFLAGS) *.java

run: 
	cd build; java GWar
debug:
	cd build; java GWar -d

clean:
	$(RM) build/*.class
