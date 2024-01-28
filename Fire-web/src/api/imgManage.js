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
}
