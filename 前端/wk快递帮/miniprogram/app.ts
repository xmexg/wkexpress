// app.ts
// const wxLogin = require('./utils/Login')
App<IAppOption>({
  globalData: {
    serverip: 'http://192.168.47.23:8081',
  },
  onLaunch() {
    // 展示本地存储能力
    const logs = wx.getStorageSync('logs')
    logs.unshift(Date.now())
    wx.setStorageSync('logs', logs)
    console.log("token的值:"+wx.getStorageSync("token"))
    if(wx.getStorageSync("token") == ""){
      console.log("开始登录")
      wxLogin()
    }    
  },
  
})

function wxLogin(){
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
            wx.setStorageSync("token", arr.user_token);
            wx.setStorageSync("user_id", arr.user_id);
            wx.setStorageSync("user_type", arr.user_type)
          },
          fail(){
            wx.showToast({title:"登录失败",icon:"error"});
          },
          complete(){
            console.log('登录后获取到的token：'+wx.getStorageSync('token'))
          }
        })
      } else {
        wx.showToast({title:"登录失败",icon:"error"});
      }
    },
  })
}