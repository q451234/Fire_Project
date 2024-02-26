import numpy as np
import cv2, math, sys
import pandas as pd

type = int(sys.argv[1])
scale = int(sys.argv[2])
scaleImg = cv2.imread("./Fire-py/img/scale.jpg", 0)
h,w = scaleImg.shape
s = int(scale) / int(w)

masks = cv2.imread("./Fire-py/img/grain_mask.png", -1)

res = {"编号": [],"面积(um²)": [], "周长(um)": [], "圆度": [], "参考类型" : []}

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


            res["编号"].append(id)
            res["面积(um²)"].append(area * s * s)
            res["周长(um)"].append(circumference * s)
            res["圆度"].append(roundness)

            id = id + 1

            if(type == 0):
                res["参考类型"].append("等轴晶")

            elif(type == 1):
                if(roundness >= 0.48):
                    res["参考类型"].append("胞状晶")
                else:
                    res["参考类型"].append("树枝晶")

            elif(type == 2):
                rect = cv2.minAreaRect(contour)
                if(rect[1][0] <= rect[1][1]):
                    aspect_ratio = rect[1][0] / rect[1][1]
                else:
                    aspect_ratio = rect[1][1] / rect[1][0]    

                if(aspect_ratio >= 0.55):
                    res["参考类型"].append("等轴晶")
                else:
                    res["参考类型"].append("柱状晶")

        
data = pd.DataFrame(res)
data.to_csv("./Fire-py/feature/grain.csv", index=None)