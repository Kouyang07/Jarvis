from interpreter import interpreter
import sys

interpreter.auto_run = True
interpreter.llm.api_key = "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"
interpreter.chat(sys.argv[1])
