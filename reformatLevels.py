filename = input("Enter level file name: ")

lines = open(filename, "r").readlines()

open("old"+filename, "w").writelines(lines)

# print(lines)
# steps:
# indexof layer component = 4
# indexof solid component = 3

# 0 0 0 0 l 0 0 0 0 0 0

def isSolid(o):
	if int(o) in (0,3,7,8,9,13,14,15,16,17,18,19,20):
		return "1"
	return "0"

# samo fencovi, cobweb
def layerOf(o):
	if int(o) in (6,8,9,18,19,20):
		return "1"
	return "0"


newLines = [["texture group layer x y"]]

realNewLines = []

for fullLine in lines:
    fullLine = fullLine[:-1]
    line = fullLine.split(" ")
    if not line[0].isdigit():
        # print(line)
        fullLine = ""
        for s in line:
            fullLine += s+" "
        print(fullLine)
        fullLine+="\n"
        realNewLines.append(fullLine)
        continue
    # step 1
    # ovaj if je opcionalan
    # line[3] = isSolid(line[0])
    if int(line[2])>0:
    	line[4] = layerOf(line[0])
    # step 2: merge all components
    fullLine = ""
    for s in line:
        fullLine += s+" "
    fullLine
    print(fullLine)
    fullLine+="\n"
    realNewLines.append(fullLine)

with open(filename, "w") as f:
    for line in realNewLines:
        f.write(line)

