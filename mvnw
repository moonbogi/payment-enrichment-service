#!/bin/sh
# Maven wrapper script
# This allows running Maven without having it installed globally

MVN_VERSION=3.9.5
MVN_HOME="$HOME/.m2/wrapper/maven-$MVN_VERSION"
MVN_BIN="$MVN_HOME/bin/mvn"

if [ ! -f "$MVN_BIN" ]; then
    echo "Installing Maven $MVN_VERSION..."
    mkdir -p "$MVN_HOME"
    curl -fsSL "https://archive.apache.org/dist/maven/maven-3/$MVN_VERSION/binaries/apache-maven-$MVN_VERSION-bin.tar.gz" | tar xz -C "$HOME/.m2/wrapper" --strip-components=1 -f -
fi

"$MVN_BIN" "$@"
