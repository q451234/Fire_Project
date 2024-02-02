<template>
  <div>
    <button @click="downloadCSV">Download CSV</button>
  </div>
</template>

<script>
export default {
  name: 'DownloadButton',
  methods: {
    downloadCSV() {
      // 生成示例表格数据
      const tableData = [
        ['Name', 'Age', 'Country'],
        ['John', '25', 'USA'],
        ['Alice', '30', 'Canada'],
        ['Bob', '22', 'UK']
      ];

      // 将表格数据转换为CSV格式
      const csvContent = tableData.map(row => row.join(',')).join('\n');
      
      // 创建一个Blob对象，用于存储CSV内容
      const blob = new Blob([csvContent], { type: 'text/csv' });

      // 创建下载链接
      const link = document.createElement('a');
      link.href = window.URL.createObjectURL(blob);
      link.download = 'table_data.csv';

      // 将链接添加到DOM中，并模拟点击以触发下载
      document.body.appendChild(link);
      link.click();

      // 清理创建的链接
      document.body.removeChild(link);
    }
  }
}
</script>
