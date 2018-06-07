import cv2

def convertArray(array):
    newArray = []
    for num in array:
        newArray.append(int(num))
    return newArray

def sameArray(array, array2):
    if len(array) != len(array2):
        return False
    for i in range(len(array)):
        if array[i] != array2[i]:
            return False
    return True

def similarColor(color1, color2):
    return abs(color1[0] - color2[0]) < 10 and \
            abs(color1[0] - color2[0]) < 10 and \
            abs(color1[0] - color2[0]) < 10

def changeBackground(img, newColor):
    x = cv2.imread('images/' + img)
    
    checkedLocations = set()
    firstColor = x[5, 5]
    checkLocations = {(5, 5)}
    toChange = set()

    while len(checkLocations) > 0:
        newLocations = set()
        for location in checkLocations:
            try:
                if location in checkedLocations:
                    pass
                elif sameArray( x[location], firstColor ):
                    newLocations.add(location)
                    toChange.add(location)
            except:
                pass
            checkedLocations.add(location)

        checkLocations = set()
        for location in newLocations:
            try:    checkLocations.add((location[0]-1, location[1]))
            except: pass
            try:    checkLocations.add((location[0], location[1]-1))
            except: pass
            try:    checkLocations.add((location[0]+1, location[1]))
            except: pass
            try:    checkLocations.add((location[0], location[1]+1))
            except: pass

    for location in toChange:
        x[location] = newColor

    cv2.imwrite('images/' + img, x)

imgs = ['BK.jpg', 'BQ.jpg', 'BB.jpg', 'BR.jpg', 'BN.jpg', 'BP.jpg',
        'WK.jpg', 'WQ.jpg', 'WB.jpg', 'WR.jpg', 'WN.jpg', 'WP.jpg']

for img in imgs:
    changeBackground(img, [225, 225, 225])

