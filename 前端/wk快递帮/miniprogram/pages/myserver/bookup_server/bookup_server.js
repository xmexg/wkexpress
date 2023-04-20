// pages/bookup/bookup.ts
Page({

  /**
   * 页面的初始数据
   */
  data: {
    // havetoken: false,
    PickListIndex: 0,
    PickList: ["西操场", "大学生创业园"],
    PriceNegChoose: [
      { name: '确认价格后取货', value: '0', checked: 'true' },
      { name: '直接取货', value: '1' }
    ]
  },

  /**
   * 取货点
   */
  Pick: function (e) {
    console.log('取货地点发送选择改变，携带值为', e.detail.value)
    this.setData({
      PickListIndex: e.detail.value
    })
  },

  /**
   * 价格协商单选
   */
  PriceNeg: function (e1) {
    console.log('价格协商携带value值为：', e1.detail.value)
  },

  /**
   * 点击提交按钮
   */
  formSubmit: (e) => {
    wx.request({
      url: getApp().globalData.serverip+'/order/addOrder',
      method: "POST",
      header:{
        token: wx.getStorageSync('token')
      },
      data: e.detail.value,
      success(res){
        const resu = JSON.stringify(res.data)
        console.log(resu)
        wx.showToast({title:JSON.stringify(res.data),icon:"none"})
      },
      fail(){
        wx.showToast({title:"提交失败",icon:"error"})
      },
      complete(){
      }
    })
  },
  formReset: function () {
    console.log('form发生了reset事件')
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad() {

  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady() {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow() {

  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide() {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload() {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh() {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom() {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage() {

  }
})