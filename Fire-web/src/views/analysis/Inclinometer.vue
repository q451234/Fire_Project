<template>
  <div id="test" style="user-select: none;">
    <!-- <button @click="magnify">放大</button>
    <button @click="shrink">缩小</button>
    <button @click="changeMode" v-show="isTrue">添加</button>
    <button @click="changeMode" v-show="!isTrue">移动</button>
    <button @click="reposition">复位</button>
    <button @click="grain">截取</button> -->
    <el-button icon = "el-icon-zoom-in" @click="magnify">放大</el-button>
    <el-button icon = "el-icon-zoom-out" @click="shrink">缩小</el-button>
    <el-button icon = "el-icon-edit" @click="changeMode" v-if="!mode">添加</el-button>
    <el-button icon = "el-icon-thumb" @click="changeMode" v-if="!mode">移动</el-button>
    <el-button icon = "el-icon-refresh" @click="reposition">复位</el-button>
    <el-button icon = "el-icon-crop" @click="grain">截取</el-button>
 
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
          src="../../assets/bg.jpg"
          alt=""
          @mousedown="isTrue ? null : move"
          ref = "img"
        />
      </div>
    </div>
  </div>
 </template>
 <script>
 export default {
  data() {
    return {
      zoom: 1,
      boxArray: [],
      isTrue: false,
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
    grain(){
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
        console.log([imgTop, imgLeft, imgWidth, imgHeight])
        return [imgTop, imgLeft, imgWidth, imgHeight]  
      }
    },
    reposition(){
      this.boxArray = [];
      this.$refs.box.style.left = "0px";
      this.$refs.box.style.top = "0px";
      this.zoom = 1;
    },
    changeMode() {
      this.isTrue = !this.isTrue;
    },
    moveMouse(e) {
      let odiv = e.target; //获取目标元素
 
      //算出鼠标相对元素的位置
      let disX = e.clientX - odiv.offsetLeft;
      let disY = e.clientY - odiv.offsetTop;

      if (this.isTrue) {
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
  },
 };
 </script>
 <style lang="less">
 #test {
  .mzindex{ 
        z-index:3000 !important; 
    }
  .content {
    width: 800px;
    height: 500px;
    background: red;
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
 </style>
 