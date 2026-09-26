JAVAC = javac
JAVA = java
MAIN = DigestCalculator

SOURCES = DigestCalculator.java DigestAlgorithm.java

ALGORITHM ?= SHA256
DIGEST_LIST ?= digestsList.xml
MONITORED_DIR ?= monitoredFiles

.PHONY: all compile run clean

all: compile

compile:
	$(JAVAC) $(SOURCES)

run: compile
	$(JAVA) $(MAIN) $(ALGORITHM) "$(DIGEST_LIST)" "$(MONITORED_DIR)"

clean:
	$(RM) *.class