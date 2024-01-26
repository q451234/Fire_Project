import cv2,math
import pandas as pd

cavity_path = "./Fire-py/cavity.jpg"

cavity = cv2.imread(cavity_path, 0)

res = {"Id": [],"Area": [], "Circumference": [], "Roundness": []}

ret, mbinary = cv2.threshold(cavity, 0, 255, cv2.THRESH_OTSU)
contours, hierarchy = cv2.findContours(mbinary, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)

id = 1
for idx in range(len(contours)):
    contour = contours[idx]
    area = cv2.contourArea(contour)

    if(area > 2):
        circumference = cv2.arcLength(contour, True)
        roundness = (4 * math.pi * area) / (circumference * circumference)

        res["Id"].append(id)
        res["Area"].append(area)
        res["Circumference"].append(circumference)
        res["Roundness"].append(roundness)

        id = id + 1

data = pd.DataFrame(res)
data.to_excel("./Fire-py/cavity.xlsx", index=None)