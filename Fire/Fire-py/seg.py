import numpy as np
import torch,sys
from networks.vit_seg_modeling import VisionTransformer as ViT_seg
from networks.vit_seg_modeling import CONFIGS as CONFIGS_ViT_seg
import cv2
from PIL import Image

scale = int(sys.argv[1])
scaleImg = cv2.imread("./Fire-py/img/scale.jpg", 0)
h,w = scaleImg.shape
s = int(scale) / int(w)

def resize_(image, output_size):
    w = output_size[0]
    h = output_size[1]

    iw, ih= image.size

    scale   = min(w/iw, h/ih)
    nw      = int(iw*scale)
    nh      = int(ih*scale)

    image       = image.resize((nw,nh), Image.BICUBIC)
    new_image   = Image.new('RGB', [w, h], (0, 0, 0))
    new_image.paste(image, ((w-nw)//2, (h-nh)//2))
    
    return new_image,nw,nh

config_vit = CONFIGS_ViT_seg["R50-ViT-B_16"]

config_vit.n_classes = 2
config_vit.n_skip = 3
config_vit.patches.grid = (int(512 / 16), int(512 / 16))
    
model = ViT_seg(config_vit, img_size=512, num_classes=2).cuda()

model.load_state_dict(torch.load("./Fire-py/model/best.pth"))

model.eval()

img_path = "./Fire-py/img/origin.jpg"

image = Image.open(img_path).convert('RGB')

origin_img = image.copy()
original_h  = np.array(image).shape[0]
original_w  = np.array(image).shape[1]

image, nw, nh = resize_(image, [512,512]) 

image = np.array(image)
image = image.transpose((2,0,1))
image = np.expand_dims(image, 0)
image = torch.from_numpy(image.astype(np.float32))  

image = image.cuda()   
outputs = model(image)

out = torch.argmax(torch.softmax(outputs, dim=1), dim=1).squeeze(0)
out = out.cpu().detach().numpy()

img_mask = out[int((512 - nh) // 2) : int((512 - nh) // 2 + nh), \
            int((512 - nw) // 2) : int((512 - nw) // 2 + nw)]

img_mask = cv2.resize(img_mask.astype("uint8"), (original_w, original_h), interpolation = cv2.INTER_LINEAR)

cv2.imwrite("./Fire-py/img/mask.jpg", img_mask)

ret, mbinary = cv2.threshold(img_mask, 0, 255, 0)

contours, hierarchy = cv2.findContours(mbinary, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)

contours = sorted(contours, key = cv2.contourArea, reverse = True)

img = cv2.cvtColor(np.asarray(origin_img),cv2.COLOR_RGB2BGR)
cv2.drawContours(img, contours, 0, (0,0,255), 15)

cv2.imwrite("./Fire-py/img/res.jpg", img)

mask = [img_mask, img_mask, img_mask]
mask = np.transpose(mask, [1,2,0])
cv2.imwrite("./Fire-py/img/seg.jpg", mask * origin_img)

import pandas as pd
import math

res = {"Area": [], "Circumference": [], "Roundness": []}

contour = contours[0]
area = cv2.contourArea(contour)
circumference = cv2.arcLength(contour, True)
roundness = (4 * math.pi * area) / (circumference * circumference)

res["Area"].append(area * s * s)
res["Circumference"].append(circumference * s)
res["Roundness"].append(roundness)

data = pd.DataFrame(res)
data.to_csv("./Fire-py/feature/melting.csv", index=None)