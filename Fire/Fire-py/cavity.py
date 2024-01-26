import cv2,math
import numpy as np
import pandas as pd

def draw(img, out):    
    i = img.copy()
    
    ret, mbinary = cv2.threshold(out, 0, 255, cv2.THRESH_OTSU)
    contours, hierarchy = cv2.findContours(mbinary, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)

    id = 1
    for idx in range(len(contours)):
        contour = contours[idx]
        area = cv2.contourArea(contour)

        if(area > 2):
            M = cv2.moments(contour)
            cx = int(M['m10']/M['m00']) # 求x坐标
            cy = int(M['m01']/M['m00']) # 求y坐标
            
            cv2.drawContours(i, contours, idx, (0,255,0), 12)
            cv2.putText(i, str(id), (cx, cy), cv2.FONT_HERSHEY_SIMPLEX, 2, (0, 0, 255), 5)
            id = id + 1
    return i

def new_BMMC(img, mask):

    imgray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
    ret, mbinary = cv2.threshold(mask, 0, 255, 0)

    ret, binary = cv2.threshold(imgray, 0, 255, cv2.THRESH_OTSU)

    kernel = cv2.getStructuringElement(shape=cv2.MORPH_ELLIPSE, ksize=(3, 3))
    filling = mbinary - binary

    filling = cv2.erode(filling, kernel, iterations=3)

    filling = cv2.medianBlur(filling, 13)

    return filling


def nnew_BMMC(img, mask):

    imgray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
    ret, mbinary = cv2.threshold(mask, 0, 255, 0)

    ret, binary = cv2.threshold(imgray, 0, 255, cv2.THRESH_OTSU)
    kernel = cv2.getStructuringElement(shape=cv2.MORPH_ELLIPSE, ksize=(3, 3))
    
    filling = mbinary & binary

    filling2 = cv2.dilate(filling, kernel, iterations=5)

    res = np.zeros(img.shape)
    contours, hierarchy = cv2.findContours(filling2, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)
    cv2.drawContours(res, contours, -1, (255,255,255), -1)

    rbinary = res[...,0] 

    final = rbinary - filling2
    final = final.astype(np.uint8) 
    final = cv2.medianBlur(final, 19)

    return final

img_path = "./Fire-py/origin.jpg"
mask_path = "./Fire-py/mask.jpg"
factor = 0.25

ori_img = cv2.imread(img_path)

mask = cv2.imread(mask_path, 0)

m = np.stack((mask, mask, mask),axis=2)
img = ori_img * m

# try:
width, height = mask.shape

img_re = cv2.resize(img, (int(height * factor), int(width * factor))) 
mask_re = cv2.resize(mask, (int(height * factor), int(width * factor)))

out1 = new_BMMC(img_re, mask_re)
width, height = out1.shape

out2 = nnew_BMMC(img, mask)

out1 = cv2.resize(out1, (out2.shape[1], out2.shape[0]))
out_final = out2 | out1
cv2.imwrite("./Fire-py/cavity.jpg", out_final)

cavity = draw(ori_img, out_final)
cv2.imwrite("./Fire-py/cavity_vis.jpg", cavity)