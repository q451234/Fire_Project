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

    <el-dialog :visible.sync="segVisible">
        <img width="100%" :src="require('../../../../Fire/Fire-py/res.jpg')" alt="">
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
      }
    }
}

</script>
