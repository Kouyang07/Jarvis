import sys
from elevenlabs import generate, play, set_api_key

set_api_key("a05183ad1bd9aaa45549273a2be331ff")

print("SAYING: " + sys.argv[1])

audio = generate(
  text=sys.argv[1],
  voice="Rachel",
  model="eleven_multilingual_v2"
)

play(audio)