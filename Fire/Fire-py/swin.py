import torch
from PIL import Image
from torchvision import transforms

model = torch.load("model/model-swin.pth")
model.to('cuda')

img = Image.open("./data/new_data/train/primary/primary_1.jpg")
trans = transforms.Compose([transforms.Resize((224, 224)),
                               transforms.ToTensor(),
                               transforms.Normalize([0.485, 0.456, 0.406], [0.229, 0.224, 0.225])])
img_trans = trans(img)

img_test = torch.unsqueeze(img_trans, 0).to('cuda')

model.eval()

outputs = model(img_test)
                
predict = torch.max(outputs, dim=1)[1]

print(predict.cpu().numpy()[0])