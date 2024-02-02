import request from '@/utils/request'

export default{
  upload(data){
    return request({
      url: '/img/upload',
      method: 'post',
      data : data,
    });
  },
  seg(){
    return request({
      url: '/img/seg',
      method: 'get',
    });
  },
  cavity(){
    return request({
      url: '/cavity/seg',
      method: 'get',
    });
  },
  exportCavity(){
    return request({
      url: '/cavity/export',
      method: 'get',
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
  exportGrain(){
    return request({
      url: '/grain/export',
      method: 'get',
    });
  }
}
