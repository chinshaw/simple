#include "radapter.h"



void testRunScript() {
	const char *command = "cat(\"12\"); cat(\"13\"); cat(\"\n\")";

	eval_script(command);
}


int main(int argc, char **argv) {
	initR(0, NULL);
	
	testRunScript();




	stopR();
}
