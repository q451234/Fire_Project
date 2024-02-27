<template>
  <div>
    <el-upload
      class="upload-demo"
      action="https://jsonplaceholder.typicode.com/posts/"
      :on-preview="handlePreview"
      :on-change="onChange"
      :on-remove="onRemove"
      :file-list = "fileList"
      :limit = 3
      :http-request="picUpload"
      list-type="picture">
      <el-button size="medium" type="primary" icon="el-icon-upload2">点击上传</el-button>
      <el-tag v-show="showClassfiy" type="danger" style="margin-left: 100px;" ref="classify" :key="res">{{res}}</el-tag>
    </el-upload> 

    <div v-show="showScale">    
      <el-input v-model="scale" placeholder="请输入比例尺长度" oninput="value=value.replace(/\D|^0/g, '')" style="width: 150px; margin-top: 10px;"></el-input>
      um

      <el-upload
        class="upload-demo"
        action="https://jsonplaceholder.typicode.com/posts/"
        :on-change="handleChangeScale"
        :on-remove="handleRemoveScale"
        :limit="3"
        :file-list="fileListScale"
        :http-request="scaleUpload"
        style="margin-top: 10px;">
        <el-button size="small" type="primary">点击上传比例尺图片</el-button>
        <div slot="tip" class="el-upload__tip">比例尺图片宽为比例尺长</div>
      </el-upload>
    </div>

    <div style="padding-top: 15px;">
      <el-button n v-if = "showButton" size="medium" type="danger" @click="classify" icon = "el-icon-camera">类型识别</el-button>
      <el-button n v-if = "showButton" size="medium" type="info" @click="segment" icon = "el-icon-scissors">熔化区分割</el-button>
      <el-button n v-if = "showButton" size="medium" type="success" @click="cavity" icon = "el-icon-thumb">孔洞提取</el-button>
      <el-button n v-if = "showButton" size="medium" type="warning" @click="grain" icon = "el-icon-crop">晶粒提取</el-button>
    </div>

    <el-dialog :visible.sync="originVisible">
        <img width="100%" :src="originImageUrl" alt="">
    </el-dialog>

    <el-dialog :visible.sync="segVisible" style="top:-100px" :before-close="close">
      <div @mousewheel="bbimg(this)" class = "img-display">
        <div class="img-footer">
          <el-button icon = "el-icon-refresh-right" @click="rotate()" type="info">旋转</el-button>
          <el-button icon = "el-icon-refresh" @click="imgOut()" type="info">还原</el-button>
          <el-button icon = "el-icon-download" @click="exportMelting()" type="info">导出特征</el-button>
			  </div>
        <img width="100%" :src="require('../../../../Fire/Fire-py/img/res.jpg')" alt="" class="imgclass" :style="test " @mousedown="imgMove">
      </div>
    </el-dialog>

    <el-dialog :visible.sync="cavityVisible" style="top:-100px" :before-close="close">
      <div @mousewheel="bbimg(this)" class = "img-display">
        <div class="img-footer">
          <el-button icon = "el-icon-refresh-right" @click="rotate()" type="info">旋转</el-button>
          <el-button icon = "el-icon-refresh" @click="imgOut()" type="info">还原</el-button>
          <el-button icon="el-icon-download" @click="exportCavity()" type="info">导出特征</el-button>
			  </div>
        <img width="100%" :src="require('../../../../Fire/Fire-py/img/cavity_vis.jpg')" alt="" class="imgclass" :style="test " @mousedown="imgMove">
      </div>
    </el-dialog>

    <el-dialog :visible.sync="grainAreaVisible" style="top:-100px" :before-close="closeGrain">
      <div @mousewheel="bbimg(this)" class = "img-display">
        <div class="img-footer">
          <el-button icon = "el-icon-refresh-right" @click="rotate()" type="info">旋转</el-button>
          <el-button icon = "el-icon-refresh" @click="imgOut()" type="info">还原</el-button>
          <el-button icon = "el-icon-download" @click="exportGrain()" type="info">导出特征</el-button>
			  </div>
        <img width="100%" :src="require('../../../../Fire/Fire-py/img/grain.jpg')" alt="" class="imgclass" :style="test " @mousedown="imgMove">
      </div>
    </el-dialog>

    <el-dialog :visible.sync="grainVisible" style="top:-100px" :before-close="close" >
      <div id="test" style="user-select: none;">
        <div class="img-footer" style="margin-bottom: 20px;">
          <el-button icon = "el-icon-zoom-in" @click="magnify">放大</el-button>
          <el-button icon = "el-icon-zoom-out" @click="shrink">缩小</el-button>
          <el-button icon = "el-icon-edit" @click="changeMode" v-if="mode">添加</el-button>
          <el-button icon = "el-icon-thumb" @click="changeMode" v-if="!mode">移动</el-button>
          <el-button icon = "el-icon-refresh" @click="reposition">复位</el-button>
          <el-button icon = "el-icon-crop" @click="grain_area">截取</el-button>         
        </div>

        <div class="content">
          <div
            :style="{
              transform: 'scale(' + zoom + ')',
              position: 'relative',
              width: '100%',
              height: '100%',
            }"
            @mousedown="moveMouse"
            ref = "box"
          >
            <div
              :class="
                'mark' + index == 'mark' + b_i
                  ? 'mark b_border'
                  : 'mark'
              "
              :ref="'mark' + index"
              @mousedown.stop="move"
              @click="handelClick(index)"
              v-for="(item, index) in boxArray"
              :key="index"
              :style="{
                width: item.width + 'px',
                height: item.height + 'px',
                position: 'absolute',
                left: item.left + 'px',
                top: item.top + 'px',
                background: 'rgba(43,100,206,0.3)',
                border: 'none',
              }"
            >
            </div>
            <div
              :style="{
                height: markHeight + 'px',
                width: markWidth + 'px',
                top: markTop + 'px',
                left: markLeft + 'px',
                position: 'absolute',
                background: 'rgba(43,100,206,0.3)',
              }"
            ></div>
            <img
              style="width: 100%; pointer-events: none;"
              :src="require('../../../../Fire/Fire-py/img/origin.jpg')"
              alt=""
              @mousedown="isTrue ? null : move"
              ref = "img"
            />
          </div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import imgApi from "@/api/imgManage";

export default {
    data() {
      return {
        fileList : [],
        originImageUrl: '',
        originVisible: false,
        showButton: false,

        segVisible: false,
        cavityVisible: false,
        grainVisible: false,
        grainAreaVisible: false,
        ifSeg:false,

        showClassfiy: false,
        res: "",
        type: "",

        scale: '',
        showScale: false,
        fileListScale: [],
        noScaleImg: false,
        noScale: false,

        deg: 0,
				test: '',
				zoomInShow: false,
				params: {
					zoom: 1,
					left: 0,
					top: 0,
					currentX: 0,
					currentY: 0,
					flag: false,
				},


        //grain
        zoom: 1,
        boxArray: [],
        mode: true,
        width: "",
        height: "",
        left: "",
        top: "",
        b_i: "",
        markHeight: 0,
        markWidth: 0,
        markTop: 0,
        markLeft: 0,
      };
    },
    methods: {
      scaleUpload(f){
        let params = new FormData()
        // //注意在这里一个坑f.file
        params.append("file", f.file);
        imgApi.uploadScale(params).then(response => {
          this.$message({
            message: response.message,
            type: 'success',
            customClass:'mzindex'
          });
        })
        this.showButton = true
      },
      handleChangeScale(file, fileList){
        if(fileList.length > 0){
          this.fileListScale = [file]
        }
      },
      handleRemoveScale(file, fileList){
        this.showButton = false
      },
      classify(){
        let dic = {0 : "火烧熔痕", 1 : "一次短路熔痕", 2 : "二次短路熔痕"}
        this.$message("处理中")
        imgApi.classify().then(response => {
          let r = response.data;
          this.$message({
              message: response.message,
              type: 'success',
              customClass:'mzindex'
          });
          
          this.showClassfiy = true
          this.res = "参考类型 : " + dic[r]
          this.type = r
        })
      },
      exportMelting(){
        if(this.scale == ''){
          this.$message({
              message: "请输入比例尺长度",
              type: 'error',
              customClass:'mzindex'
          });
        }
        else if(this.fileListScale.length == 0){
          this.$message({
              message: "请上传比例尺图片",
              type: 'error',
              customClass:'mzindex'
          });
        }
        else{
          this.$message("处理中")
          imgApi.exportMelting().then(response => {
            // 生成示例表格数据
            const tableData = response.data

            // 将表格数据转换为CSV格式
            const csvContent = tableData.map(row => row.join(',')).join('\n');
            
            // 创建一个Blob对象，用于存储CSV内容
            const blob = new Blob(['\uFEFF' + csvContent], { type: 'text/csv' });

            // 创建下载链接
            const link = document.createElement('a');
            link.href = window.URL.createObjectURL(blob);
            link.download = '熔化区特征.csv';

            // 将链接添加到DOM中，并模拟点击以触发下载
            document.body.appendChild(link);
            link.click();

            // 清理创建的链接
            document.body.removeChild(link);
          });          
        }
      },
      close(){
        this.segVisible = false,
        this.cavityVisible = false,
        this.grainVisible = false,
        this.grainAreaVisible = false,

        this.deg = 0,
				this.test = '',
				this.zoomInShow = false,
				this.params = {
					zoom: 1,
					left: 0,
					top: 0,
					currentX: 0,
					currentY: 0,
					flag: false,
				},


        //grain
        this.zoom = 1,
        this.boxArray = [],
        this.mode = true,
        this.width = "",
        this.height = "",
        this.left = "",
        this.top = "",
        this.b_i = "",
        this.markHeight = 0,
        this.markWidth = 0,
        this.markTop = 0,
        this.markLeft = 0
      },
      closeGrain(){
        this.grainAreaVisible = false

        this.deg = 0,
				this.test = '',
				this.zoomInShow = false,
				this.params = {
					zoom: 1,
					left: 0,
					top: 0,
					currentX: 0,
					currentY: 0,
					flag: false,
				}
      },
      grain(){
        this.grainVisible = true;
      },
      exportGrain(){
        if(this.scale == ''){
          this.$message({
              message: "请输入比例尺长度",
              type: 'error',
              customClass:'mzindex'
          });
        }
        else if(this.fileListScale.length == 0){
          this.$message({
              message: "请上传比例尺图片",
              type: 'error',
              customClass:'mzindex'
          });
        }
        else{
          this.$message("处理中")
          imgApi.exportGrain(this.type, this.scale).then(response => {
            // 生成示例表格数据
            const tableData = response.data

            // 将表格数据转换为CSV格式
            const csvContent = tableData.map(row => row.join(',')).join('\n');
            
            // 创建一个Blob对象，用于存储CSV内容
            const blob = new Blob(['\uFEFF' + csvContent], { type: 'text/csv' });

            // 创建下载链接
            const link = document.createElement('a');
            link.href = window.URL.createObjectURL(blob);
            link.download = '晶粒特征.csv';

            // 将链接添加到DOM中，并模拟点击以触发下载
            document.body.appendChild(link);
            link.click();

            // 清理创建的链接
            document.body.removeChild(link);
          });          
        }
      },
      grain_area(){
        if(this.boxArray.length == 0){
          this.$message({
              message: "无晶粒区域, 请重新框选",
              type: 'error',
              customClass:'mzindex'
          });
        }else{
          // var offWidth = this.$refs.img.width;
          var offHeight = this.$refs.img.height;

          // var oriWidth = this.$refs.img.naturalWidth;
          var oriHeight = this.$refs.img.naturalHeight;

          var offset = offHeight / oriHeight

          var imgTop = this.boxArray[0].top / offset;
          var imgLeft = this.boxArray[0].left / offset;
          var imgWidth = this.boxArray[0].width / offset;
          var imgHeight = this.boxArray[0].height / offset;
          
          var box = [imgTop, imgLeft, imgWidth, imgHeight]

          imgApi.corpGrain(box).then(response => {
            this.$message({
              message : "处理中",
              // message: response.message,
              // type: 'success',
              customClass:'mzindex'
            })
          })
          imgApi.grain().then(response => {
            this.$message({
              message: response.message,
              type: 'success',
              customClass:'mzindex'
            });
            this.grainAreaVisible = true;
          })
        }
      },
      reposition(){
        this.boxArray = [];
        this.$refs.box.style.left = "0px";
        this.$refs.box.style.top = "0px";
        this.zoom = 1;
      },
      changeMode() {
        this.mode = !this.mode;
      },
      moveMouse(e) {
        let odiv = e.target; //获取目标元素
  
        //算出鼠标相对元素的位置
        let disX = e.clientX - odiv.offsetLeft;
        let disY = e.clientY - odiv.offsetTop;

        if (this.mode) {
          // 拖动
          document.onmousemove = (e) => {
            //鼠标按下并移动的事件
            //用鼠标的位置减去鼠标相对元素的位置，得到元素的位置
            let left = e.clientX - disX;
            let top = e.clientY - disY;
  
            //绑定元素位置到positionX和positionY上面
            this.positionX = top;
            this.positionY = left;
  
            //移动当前元素
            odiv.style.left = left + "px";
            odiv.style.top = top + "px";
          };
          document.onmouseup = (e) => {
            document.onmousemove = null;
            document.onmouseup = null;
          };
        } else {
          this.boxArray = [];
          // 添加div
          document.onmousemove = (e) => {
            //鼠标按下并移动的事件
            //用鼠标的位置减去鼠标相对元素的位置，得到元素的位置
  
            let left = disX - odiv.getBoundingClientRect().x;
            let top = disY - odiv.getBoundingClientRect().y;
  
            this.width = (e.clientX - disX) / this.zoom;
            this.height = (e.clientY - disY) / this.zoom;
            this.markWidth = this.width;
            this.markHeight = this.height;
            this.markLeft = left;
            this.markTop = top;
            document.onmouseup = (e) => {
              let left = disX - odiv.getBoundingClientRect().x;
              let top = disY - odiv.getBoundingClientRect().y;
              this.width = (e.clientX - disX) / this.zoom;
              this.height = (e.clientY - disY) / this.zoom;
  

              if (this.width > 5 && this.height > 5) {
                this.boxArray.push({
                  width: this.width,
                  height: this.height,
                  left: left,
                  top: top,
                });
              }
  
              this.markWidth = 0;
              this.markHeight = 0;
              this.markLeft = 0;
              this.markTop = 0;
              document.onmousemove = null;
              document.onmouseup = null;
            };
          };
        }
      },
      move(e) {
        let odiv = e.target; //获取目标元素
        //算出鼠标相对元素的位置
        let disX = e.clientX - odiv.offsetLeft;
        let disY = e.clientY - odiv.offsetTop;
        document.onmousemove = (e) => {
          //鼠标按下并移动的事件
          //用鼠标的位置减去鼠标相对元素的位置，得到元素的位置
          let left = e.clientX - disX;
          let top = e.clientY - disY;
  
          //绑定元素位置到positionX和positionY上面
          this.positionX = top;
          this.positionY = left;
  
          //移动当前元素
          odiv.style.left = left + "px";
          odiv.style.top = top + "px";

          if(this.boxArray.length > 0){
            this.boxArray[0].left = left;
            this.boxArray[0].top = top;
          }
        };
        document.onmouseup = (e) => {
          document.onmousemove = null;
          document.onmouseup = null;
        };
      },
  
      magnify() {
        this.zoom += 0.1;
      },
      shrink() {
        this.zoom -= 0.1;
      },
      handelClick(i) {
        this.b_i = i;
      },

      segment() {
        this.$message("处理中")
        imgApi.seg(this.scale).then(response => {
          this.$message({
            message: response.message,
            type: 'success',
            customClass:'mzindex'
          });
          this.segVisible = true;
          this.ifSeg = true
        })
      },
      cavity() {
        this.$message("处理中")
        if(!this.ifSeg){
          imgApi.seg(this.scale).then(response => {
            imgApi.cavity().then(response => {
              this.$message({
                message: response.message,
                type: 'success',
                customClass:'mzindex'
              });
              this.cavityVisible = true;
            })
          })
        }else{
          imgApi.cavity().then(response => {
            this.$message({
              message: response.message,
              type: 'success',
              customClass:'mzindex'
            });
            this.cavityVisible = true;
          })          
        }
        this.ifSeg = true
      },
      exportCavity(){
        if(this.scale == ''){
          this.$message({
              message: "请输入比例尺长度",
              type: 'error',
              customClass:'mzindex'
          });
        }
        else if(this.fileListScale.length == 0){
          this.$message({
              message: "请上传比例尺图片",
              type: 'error',
              customClass:'mzindex'
          });
        }
        else{
          this.$message("处理中")
          imgApi.exportCavity(this.scale).then(response => {
            // 生成示例表格数据
            const tableData = response.data

            // 将表格数据转换为CSV格式
            const csvContent = tableData.map(row => row.join(',')).join('\n');
            
            // 创建一个Blob对象，用于存储CSV内容
            const blob = new Blob(['\uFEFF' + csvContent], { type: 'text/csv' });

            // 创建下载链接
            const link = document.createElement('a');
            link.href = window.URL.createObjectURL(blob);
            link.download = '孔洞特征.csv';

            // 将链接添加到DOM中，并模拟点击以触发下载
            document.body.appendChild(link);
            link.click();

            // 清理创建的链接
            document.body.removeChild(link);
          });          
        }
      },

      onChange(file, fileList) {
        if (fileList.length > 0) {
          this.fileList = [file]//这一步，是 展示最后一次选择文件
          this.ifSeg = false
          this.showClassfiy = false
          this.res = ""
          this.type = ""
        }
      },
      onRemove(file, fileList){
        this.showScale = false
        this.ifSeg = false
        this.showClassfiy = false
        this.res = ""
        this.type = ""
        this.scale = ""
      },
      handlePreview(file) {
        this.originImageUrl = file.url;
        this.originVisible = true;
      },
      picUpload(f) {
        let params = new FormData()
        // //注意在这里一个坑f.file
        params.append("file", f.file);
        imgApi.uploadImg(params).then(response => {
          this.$message({
            message: response.message,
            type: 'success',
            customClass:'mzindex'
          });
        })
        this.showScale = true
      },

      // 鼠标滚轮滚动实现图片放大缩小 
			bbimg(ev) {
				let e = ev || window.event
				this.params.zoom += e.wheelDelta / 1200
				if (this.params.zoom >= 0.2) {
					this.test = `transform:scale(${this.params.zoom}) rotateZ(${this.deg}deg);`
				} else {
					this.params.zoom = 0.2
					this.test = `transform:scale(${this.params.zoom}) rotateZ(${this.deg}deg);`
					return false
				}
			},
			// 实现图片拖拽
			imgMove(e) {
				let oImg = e.target
				let disX = e.clientX - oImg.offsetLeft
				let disY = e.clientY - oImg.offsetTop
				document.onmousemove = (e) => {
					e.preventDefault()
					let left = e.clientX - disX
					let top = e.clientY - disY
					this.test = this.test + `left: ${left}px;top: ${top}px;`
				}
				document.onmouseup = (e) => {
					document.onmousemove = null
					document.onmouseup = null
				}
			},
			// 旋转
			rotate() {
				this.deg += 90
				if (this.deg >= 360) {
					this.deg = 0
				}
				this.test = `transform: rotateZ(${this.deg}deg)`
			},
			// 还原
			imgOut() {
				this.deg = 0
				this.params = {
					zoom: 1,
					left: 0,
					top: 0,
					currentX: 0,
					currentY: 0,
					flag: false,
				}
				this.test = `transform: rotateZ(0deg)`
			},
    }
}

</script>

<style lang = "less">
 #test {
  .mzindex{ 
        z-index:9000 !important; 
    }
  .content {
    // cursor: move;
    // width: 800px;
    // height: 500px;
    // background: red;
    margin: 0 auto;
    overflow: hidden;
    position: relative;
    .b_border {
      border: 1px solid red !important;
    }
    .mark {
      z-index: 9999999;
    }
  }
 }
  .img-footer {
    display: flex;
		align-content: center;
		justify-content: center;
    z-index: 10;
	}
  
  .img-display {
		display: flex;
		align-content: center;
		justify-content: center;
	}
  .imgclass {
		cursor: move;
    position: absolute;
    margin-top: 70px;
	}
</style>