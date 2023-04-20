function login(){
  wx.login({
    success: res => {
      console.log('获取到code:'+res.code)
      // 发送 res.code 到后台换取 openId, sessionKey, unionId
      if (res.code) {
        //发起网络请求
        wx.request({
          url: getApp().globalData.serverip+'/user/login',
          data: {
            code: res.code
          },
          success(res){
            const strres = JSON.stringify(res.data)
            const arr = JSON.parse(strres)
            console.log("开始设定用户值:"+arr[0].user_token+"\t"+arr[0].user_id+"\t"+arr[0].user_type)
            wx.setStorageSync("token", arr[0].user_token);
            wx.setStorageSync("user_id", arr[0].user_id);
            wx.setStorageSync("user_type", arr[0].user_type)
          },
          fail(){
            wx.showToast({title:"登录失败",icon:"error"});
          }
        })
      } else {
        wx.showToast({title:"登录失败",icon:"error"});
      }
    },
  })
}