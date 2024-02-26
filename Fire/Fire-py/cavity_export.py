import cv2,math,sys
import pandas as pd

scale = int(sys.argv[1])
scaleImg = cv2.imread("./Fire-py/img/scale.jpg", 0)
h,w = scaleImg.shape
s = int(scale) / int(w)

cavity_path = "./Fire-py/img/cavity.jpg"

cavity = cv2.imread(cavity_path, 0)

res = {"编号": [],"面积(um²)": [], "周长(um)": [], "圆度": []}

ret, mbinary = cv2.threshold(cavity, 0, 255, cv2.THRESH_OTSU)
contours, hierarchy = cv2.findContours(mbinary, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)

id = 1
for idx in range(len(contours)):
    contour = contours[idx]
    area = cv2.contourArea(contour)

    if(area > 2):
        circumference = cv2.arcLength(contour, True)
        roundness = (4 * math.pi * area) / (circumference * circumference)

        res["编号"].append(id)
        res["面积(um²)"].append(area * s * s)
        res["周长(um)"].append(circumference * s)
        res["圆度"].append(roundness)

        id = id + 1

data = pd.DataFrame(res)
data.to_csv("./Fire-py/feature/cavity.csv", index=None)