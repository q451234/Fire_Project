from cellpose import models, io
import numpy as np
import cv2, math
import pandas as pd

from cellpose.io import logger_setup
logger_setup()

file = "./Fire-py/corp_grain.jpg"
img = io.imread(file)
model = models.Cellpose(gpu=True, model_type='cyto')
masks, flows, styles, diams = model.eval(img, diameter=None, channels=[0,0])
# io.save_masks(img, masks, flows, file, save_txt=False)

res = {"Id": [],"Area": [], "Circumference": [], "Roundness": []}
id = 1

img = img[..., [2,1,0]].copy()
n = np.max(masks)

# for i in range(1, n + 1):
#     tmp = np.where(masks == i)
#     x = np.expand_dims(tmp[0],0)
#     y = np.expand_dims(tmp[1],0)
#     coord = np.concatenate((x, y),axis=0).transpose(1, 0)

#     M = cv2.moments(coord)
#     cx = int(M['m10']/M['m00']) # 求x坐标
#     cy = int(M['m01']/M['m00']) # 求y坐标

#     print(cx, cy)
#     break



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
        
            cv2.drawContours(img, contours, idx, (0,255,0), 1)
            cv2.putText(img, str(id), (cx, cy), cv2.FONT_HERSHEY_SIMPLEX, 0.3, (0, 0, 255), 1)
    

cv2.imwrite("./Fire-py/grain.jpg", img)
data = pd.DataFrame(res)
data.to_excel("../Fire-web/public/grain.xlsx", index=None)