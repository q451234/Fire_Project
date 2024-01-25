<template>
  <div>
    <el-upload
      class="upload-demo"
      action="https://jsonplaceholder.typicode.com/posts/"
      :on-preview="handlePreview"
      :on-change="onChange"
      :file-list = "fileList"
      :limit = 3
      :http-request="picUpload"
      list-type="picture">
      <el-button size="small" type="primary">点击上传</el-button>
    </el-upload> 

    <el-button size="small" type="success" @click="segment">熔化区分割</el-button>

    <el-dialog :visible.sync="originVisible">
        <img width="100%" :src="originImageUrl" alt="">
    </el-dialog>

    <el-dialog :visible.sync="segVisible" style="top:-100px">
      <div @mousewheel="bbimg(this)" class = "img-display">
        <div class="setting_box img-footer">
          <div @click="rotate()">旋转</div>
          <div @click="imgOut()">还原</div>
			  </div>
        <img width="100%" :src="require('../../../../Fire/Fire-py/res.jpg')" alt="" class="imgclass" :style="test " @mousedown="imgMove">
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
        segVisible: false,


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
        })
      },
      onChange(file, fileList) {
        if (fileList.length > 0) {
          this.fileList = [file]//这一步，是 展示最后一次选择文件
        }
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
	}
  
  .img-display {
		display: flex;
		align-content: center;
		justify-content: center;
	}

  .img-footer div {
		background-color: #2696ee;
		padding: 10px 50px;
		color: #fff;
		margin: -10px 100px;
		cursor: pointer;
		border-radius: 10px;
    z-index: 10;
	}
  .imgclass {
		cursor: move;
    position: absolute;
    margin-top: 50px;
	}
</style>