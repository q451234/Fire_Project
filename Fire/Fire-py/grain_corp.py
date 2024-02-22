import cv2, sys

top = int(sys.argv[1])
left = int(sys.argv[2])
width = int(sys.argv[3])
height = int(sys.argv[4])

img = cv2.imread("./Fire-py/img/origin.jpg")

corp = img[top: top + height, left : left + width]

cv2.imwrite("./Fire-py/img/corp_grain.jpg", corp)
