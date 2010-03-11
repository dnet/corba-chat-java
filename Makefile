JAVAC = javac

all: Client.class Server.class

%.class: %.java chat
	$(JAVAC) $<

chat: chat.idl
	idlj -fall chat.idl
	$(JAVAC) chat/*.java

clean:
	rm -rf *.class chat/

.PHONY: all clean
