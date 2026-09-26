JAVAC = javac
JAVA = java
MAIN = DigestCalculator

JAVAC_FLAGS = -encoding UTF-8

SRC_DIR = src
BUILD_DIR = bin

SOURCES = $(wildcard $(SRC_DIR)/*.java)

ALGORITHM ?= SHA256
DIGEST_LIST ?= digestsList.xml
MONITORED_DIR ?= monitoredFiles

.PHONY: all compile run clean

all: compile

compile:
	powershell -NoProfile -Command "New-Item -ItemType Directory -Force '$(BUILD_DIR)' | Out-Null"
	$(JAVAC) $(JAVAC_FLAGS) -d $(BUILD_DIR) $(SOURCES)

run: compile
	$(JAVA) -cp $(BUILD_DIR) $(MAIN) $(ALGORITHM) "$(DIGEST_LIST)" "$(MONITORED_DIR)"

clean:
	powershell -NoProfile -Command "Get-ChildItem -Path . -Filter '*.class' -Recurse -ErrorAction SilentlyContinue | Remove-Item -Force"