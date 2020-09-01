JFLAGS = -g
JC = javac
JVM = java

CLASSPATH = java:java/lib/jna-5.6.0.jar
MAIN = GoCaller
CLASSES = java/GoCaller.java 

.SUFFIXES: .java .class
.java.class:
		$(JC) $(JFLAGS) -classpath $(CLASSPATH) $*.java

default: classes

classes: $(CLASSES:.java=.class)

run : classes
		$(JVM) -cp "java:java/lib/jna-5.6.0.jar" $(MAIN)

build-go: 
	go build -buildmode=c-shared -o bin/main.so go/src/main.go

build: clean build-go classes

clean-go:
	$(RM) ./bin/*

clean-java:
	$(RM) java/*.class

clean: clean-go	clean-java