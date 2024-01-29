import numpy as np
import cv2

masks = cv2.imread("grain_mask.png", -1)

img = cv2.imread("grain_area.png")
n = np.max(masks)
for i in range(1, n + 1):
    tmp = masks.copy()
    tmp[np.where(tmp != i)] = 0
    tmp[np.where(tmp == i)] = 255
    tmp = tmp.astype(np.uint8)
    contours, hierarchy = cv2.findContours(tmp, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)

    cv2.drawContours(img, contours, -1, (0,255,0), 1)

cv2.imwrite("test.jpg", img)