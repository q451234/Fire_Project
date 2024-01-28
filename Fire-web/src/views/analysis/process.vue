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
    </el-upload> 

    <div>
      <el-button n v-if = "showButton" size="medium" type="success" @click="segment" icon = "el-icon-scissors">熔化区分割</el-button>
      <el-button n v-if = "showButton" size="medium" type="success" @click="cavity" icon = "el-icon-thumb">孔洞提取</el-button>
      <el-button n v-if = "showButton" size="medium" type="success" @click="segment" icon = "el-icon-crop">晶粒提取</el-button>
    </div>


    <el-dialog :visible.sync="originVisible">
        <img width="100%" :src="originImageUrl" alt="">
    </el-dialog>

    <el-dialog :visible.sync="segVisible" style="top:-100px">
      <div @mousewheel="bbimg(this)" class = "img-display">
        <div class="img-footer">
          <el-button icon = "el-icon-refresh-right" @click="rotate()" type="info">旋转</el-button>
          <el-button icon = "el-icon-refresh" @click="imgOut()" type="info">还原</el-button>
			  </div>
        <img width="100%" :src="require('../../../../Fire/Fire-py/res.jpg')" alt="" class="imgclass" :style="test " @mousedown="imgMove">
      </div>
    </el-dialog>

    <el-dialog :visible.sync="cavityVisible" style="top:-100px">
      <div @mousewheel="bbimg(this)" class = "img-display">
        <div class="img-footer">
          <el-button icon = "el-icon-refresh-right" @click="rotate()" type="info">旋转</el-button>
          <el-button icon = "el-icon-refresh" @click="imgOut()" type="info">还原</el-button>
          <el-button icon="el-icon-download" @click="exportCavity()" type="info">导出特征</el-button>
			  </div>
        <img width="100%" :src="require('../../../../Fire/Fire-py/cavity_vis.jpg')" alt="" class="imgclass" :style="test " @mousedown="imgMove">
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

        ifSeg:false,


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
				}
      };
    },
    methods: {
      segment() {
        this.$message("处理中")
        imgApi.seg().then(response => {
          this.$message({
            message: response.message,
            type: 'success'
          });
          this.segVisible = true;
          this.ifSeg = true
        })
      },
      cavity() {
        this.$message("处理中")
        if(!this.ifSeg){
          imgApi.seg().then(response => {
            imgApi.cavity().then(response => {
              this.$message({
                message: response.message,
                type: 'success'
              });
              this.cavityVisible = true;
            })
          })
        }else{
          imgApi.cavity().then(response => {
            this.$message({
              message: response.message,
              type: 'success'
            });
            this.cavityVisible = true;
            console.log(response)
          })          
        }
        this.ifSeg = true
      },
      exportCavity(){
        this.$message("处理中")
        imgApi.exportCavity().then(response => {
          window.location.href="./cavity.xlsx"
        });
      },

      onChange(file, fileList) {
        if (fileList.length > 0) {
          this.fileList = [file]//这一步，是 展示最后一次选择文件
          this.ifSeg = false
        }
      },
      onRemove(file, fileList){
        this.showButton = false
        this.ifSeg = false
      },
      handlePreview(file) {
        this.originImageUrl = file.url;
        this.originVisible = true;
      },
      picUpload(f) {
        let params = new FormData()
        // //注意在这里一个坑f.file
        params.append("file", f.file);
        imgApi.upload(params).then(response => {
          this.$message({
            message: response.message,
            type: 'success'
          });
        })
        this.showButton = true
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

<style>
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