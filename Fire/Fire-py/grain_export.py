import numpy as np
import cv2, math
import pandas as pd

masks = cv2.imread("./Fire-py/grain_mask.png", -1)

res = {"Id": [],"Area": [], "Circumference": [], "Roundness": []}

id = 1

n = np.max(masks)

for i in range(1, n + 1):
    tmp = masks.copy()
    tmp[np.where(tmp != i)] = 0
    tmp[np.where(tmp == i)] = 255
    tmp = tmp.astype(np.uint8)
    contours, hierarchy = cv2.findContours(tmp, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)


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

            M = cv2.moments(contour)
            cx = int(M['m10']/M['m00']) # 求x坐标
            cy = int(M['m01']/M['m00']) # 求y坐标
        

data = pd.DataFrame(res)
data.to_csv("../Fire-web/public/grain.csv", index=None)