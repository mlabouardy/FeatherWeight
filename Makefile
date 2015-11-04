all: java

sable:
	@sablecc -d . grammar/Grammar.sablecc
	@patch -d checker/parser/ < patches/sable_unchecked.patch

java:
	@mkdir -p build
	@javac -d build $$(find checker -name \*.java) -Xlint:unchecked
	@cp checker/lexer/lexer.dat build/checker/lexer/
	@cp checker/parser/parser.dat build/checker/parser/

test: java
	@java -cp build checker.Test

clean:
	@rm -rf build && rm -f $$(find . -name "*.class") $$(find . -name "*~")
