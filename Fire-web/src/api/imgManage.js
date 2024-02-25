import request from '@/utils/request'

export default{
  uploadImg(data){
    return request({
      url: '/img/uploadImg',
      method: 'post',
      data : data,
    });
  },
  seg(scale){
    return request({
      url: '/img/seg',
      method: 'get',
      params: { 
        scale: scale
      }
    });
  },
  cavity(){
    return request({
      url: '/cavity/seg',
      method: 'get',
    });
  },
  exportCavity(scale){
    return request({
      url: '/cavity/export',
      method: 'get',
      params: { 
        scale: scale
      }
    });
  },
  grain(){
    return request({
      url: '/grain/seg',
      method: 'get',
    });
  },
  corpGrain(box){
    return request({
      url: '/grain/corp',
      method: 'post',
      data: box
    });
  },
  exportGrain(type, scale){
    return request({
      url: '/grain/export',
      method: 'get',
      params: { 
        type: type, 
        scale: scale
      }
    });
  },
  classify(){
    return request({
      url: '/img/classify',
      method: 'get',
    });
  },
  exportMelting(){
    return request({
      url: '/img/export',
      method: 'get',
    });
  },
  uploadScale(data){
    return request({
      url: '/img/uploadScale',
      method: 'post',
      data : data,
    });
  }
}
